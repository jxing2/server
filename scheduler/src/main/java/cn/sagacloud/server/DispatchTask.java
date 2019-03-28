package cn.sagacloud.server;

import cn.sagacloud.guice.InjectorInit;
import cn.sagacloud.mybatis.model.TaskModel;
import cn.sagacloud.mybatis.service.TaskService;
import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.Command;
import cn.sagacloud.pojo.TaskStatus;
import cn.sagacloud.utils.CommonUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class DispatchTask implements Runnable {
    private static Logger log = Logger.getLogger(DispatchTask.class);
    private static TaskService service = InjectorInit.getInjector().getInstance(TaskService.class);
    ArrayList<ChannelHandlerContextWrapper> clientList;
    private int clientIndex = 0;
    public static Map<Integer, TaskModel> tasks;
    public DispatchTask(ArrayList<ChannelHandlerContextWrapper> clientList) throws Exception {
        this.clientList = clientList;
        tasks = service.getAllTaskMapByStatus(Arrays.asList(0, 1, 2));
    }

    /**
     * 是否当前状态下可以执行该命令
     * @param taskId task id
     * @param cmd command
     * @return 写一个状态, 如果不可执行该命令, 返回null
     */
    public static TaskStatus isAllowCmd(int taskId, Command cmd) {
        TaskModel task = DispatchTask.tasks.get(taskId);
        if(task == null)
            return null;
        TaskStatus status = TaskStatus.getTaskStatusById(task.getTask_status());
        if(status == null)
            return null;
        Map<Command, TaskStatus> allowCmd = SchedulerHandler.statusTransitionMap.get(status);
        if(allowCmd == null)
            return null;
        return allowCmd.get(cmd);
    }

    public static void changeStatusByCmd(int taskId, Command cmd) {
        TaskStatus nextStatus = isAllowCmd(taskId, cmd);
        if(nextStatus == null)
            return;
        TaskModel task = DispatchTask.tasks.get(taskId);
        if(task == null)
            return;
        task.setTask_status(TaskStatus.getIdByTaskStatus(nextStatus));
        updateTask(task);
    }

    @Override
    public void run() {
        // 考虑加锁
        for(long cnt = Long.MAX_VALUE; cnt > Long.MIN_VALUE; --cnt) {
            // 获取到一个在waiting状态的任务
            TaskModel toBeSent = getOneAvailableTask();
            if(toBeSent != null) {
                // 获取到一个潜在可以接受任务的客户端
                ChannelHandlerContextWrapper client = getOneAvailableClient();
                if(client != null) {
                    // 将获取到的任务发送给选中客户端
                    client.sendTask(toBeSent);
                    // 检测sending, sent状态的任务, 是否有超时需要重新下载的任务
                    checkSendingTimeOut();
                    checkExecuteTimeOut();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkExecuteTimeOut() {
        for(ChannelHandlerContextWrapper client : clientList){
            int id = getOneExecuteTimeOutTaskId();
            if(id > 0){
                // 是否加锁
                tasks.get(id).setTask_status(0);
            }
        }
    }
    /**
     * 获取一个在执行任务时超时的任务id
     * @return
     */
    private int getOneExecuteTimeOutTaskId() {
        if(tasks == null || tasks.size() == 0)
            return -1;
        long currentTime = CommonUtil.getTime();
        for(Integer id : tasks.keySet()){
            TaskModel task = tasks.get(id);
            long expectTime = task.getTask_expected_finish_time();
            long sendTime = task.getTask_sent_time();
            if(sendTime <= 0)
                continue;
            if(currentTime  - sendTime > expectTime){
                return id;
            }
        }
        return -1;
    }

    private void checkSendingTimeOut() {
        for(ChannelHandlerContextWrapper client : clientList){
            int id = client.getOneSendingTimeOutTaskId();
            if(id > 0){
                // 是否加锁
                tasks.get(id).setTask_status(0);
            }
        }
    }

    private ChannelHandlerContextWrapper getOneAvailableClient() {
        int clientCount ;
        if(clientList == null || clientList.size() == 0)
            return null;
        else {
            clientCount = clientList.size();
            if(clientCount == 0)
                return null;
            clientIndex %= clientCount;
        }
        ChannelHandlerContextWrapper wrapper = null;
        do{
            try {
                wrapper = clientList.get(clientIndex);
            }catch (Exception ex){
                wrapper = null;
                clientCount = clientList.size();
            }
            if(!wrapper.isLastRefuseTimeOutPassed()){
                wrapper = null;
            }
            if(clientCount == 0)
                return null;
            clientIndex = (clientIndex + 1) % clientCount;
        }while(wrapper == null && clientIndex != 0);
        return wrapper;
    }

    private TaskModel getOneAvailableTask() {
        //检测到在waiting状态的任务, 返回
        for(Integer taskId : tasks.keySet()){
            TaskModel task = tasks.get(taskId);
            if(task.getTask_status() == 0){
                return task;
            }
        }
        return null;
    }

    private static void updateTask(TaskModel task){
        try {
            service.updateTask(task);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("任务:" + task.getId() + "update失败");
        }
    }
}
