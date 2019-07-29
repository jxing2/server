package cn.sagacloud.pojo;

import cn.sagacloud.mybatis.model.TaskModel;
import cn.sagacloud.proto.MessageProto;
import cn.sagacloud.proto.MessageUtil;
import cn.sagacloud.server.DispatchTask;
import cn.sagacloud.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ChannelHandlerContextWrapper {
    private ChannelHandlerContext ctx;  // 通信handler
    private long lastRejectTime;        // unix时间片, 精确到秒
    private final long sendingTimeOut = 15;            // 发送给客户端任务, 如果超过该时间未响应, 则超时, 并将发送的任务状态置为初始状态
    private final long lastRefuseTimeOut = 3600;       // 如果客户端拒绝任务, 则等待lastRefuseTimeOut秒, 才会再分配任务给该客户端
    private String clientInfo;             // mac地址
    // 任务发送中状态map,     key --> taskid, value --> 发送任务时间点,    如果超过15s, 即为超时.
    public Map<Integer, Long> taskSendingStatusMap = new HashMap<>();

    public ChannelHandlerContextWrapper(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.lastRejectTime = 0;
        clientInfo = null;
    }

    public void sendTask(TaskModel toBeSent) {
        if(ctx.channel().isActive()){
            MessageProto.Message msg = MessageUtil.buildMessage(Command.SendTask.name(), toBeSent.getId(), JSON.toJSONString(toBeSent));
            ctx.writeAndFlush(msg);
            toBeSent.setTask_status(1);
            taskSendingStatusMap.put(toBeSent.getId(), CommonUtil.getTime());
        }
    }

    /**
     * 获取一个在发送任务时超时的任务id
     * @return 任务id
     */
    public int getOneSendingTimeOutTaskId(){
        if(taskSendingStatusMap.size() == 0)
            return -1;
        long currentTime = CommonUtil.getTime();
        for(Integer id : taskSendingStatusMap.keySet()){
            Long sentTime = taskSendingStatusMap.get(id);
            if(currentTime - sentTime > sendingTimeOut){
                return id;
            }
        }
        return -1;
    }
    
    public void resetSendingTimeOutTask(int id){
        if(id > 0){
            taskSendingStatusMap.remove(id);
        }
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public long getLastRejectTime() {
        return lastRejectTime;
    }

    public void setLastRejectTime(int taskId) {
        this.lastRejectTime = CommonUtil.getTime();
        DispatchTask.changeStatusByCmd(taskId, Command.RefuseTask, clientInfo);
    }

    public boolean isLastRefuseTimeOutPassed() {
        if(lastRejectTime == 0)
            return true;
        long currentTime = CommonUtil.getTime();
        if(currentTime - lastRefuseTimeOut > lastRejectTime)
            return true;
        return false;
    }

    public void acceptTask(int taskId) {
        TaskStatus isAllow = DispatchTask.isAllowCmd(taskId, Command.AcceptTask);
        if(isAllow==null)
            return;
        DispatchTask.changeStatusByCmd(taskId, Command.AcceptTask, clientInfo);
        taskSendingStatusMap.remove(taskId);
    }

    public void taskSuccess(MessageProto.Message message) {
        DispatchTask.changeStatusByCmdWithReturnJson(message.getTaskId(), Command.TaskSuccess, clientInfo, message.getContent());
    }

    public void commandError(MessageProto.Message message) {
        DispatchTask.changeStatusByCmdWithReturnJson(message.getTaskId(), Command.CommandError, clientInfo, message.getContent());
    }

    public void downloadError(MessageProto.Message message) {
        DispatchTask.changeStatusByCmdWithReturnJson(message.getTaskId(), Command.DownloadError, clientInfo, message.getContent());
    }
}
