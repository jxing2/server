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
    private final long sendingTimeOut = 10;            // 发送给客户端任务, 如果超过该时间未响应, 则超时, 并将发送的任务状态置为初始状态
    private final long lastRefuseTimeOut = 3600;       // 如果客户端拒绝任务, 则等待lastRefuseTimeOut秒, 才会再分配任务给该客户端
    private long lastRefuseTime = 0L;
    private String macAddr;             // mac地址
    // 任务发送中状态map,     key --> taskid, value --> 发送任务时间点,    如果超过15s, 即为超时.
    private Map<Integer, Long> taskSendingStatusMap = new HashMap<>();

    public ChannelHandlerContextWrapper(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.lastRejectTime = 0;
        macAddr = null;
    }

    public void sendTask(TaskModel toBeSent) {
        if(ctx.channel().isActive()){
            MessageProto.Message msg = MessageUtil.buildMessage(Command.SendTask.name(), toBeSent.getId(), JSON.toJSONString(toBeSent));
            ctx.writeAndFlush(msg);
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

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public long getLastRejectTime() {
        return lastRejectTime;
    }

    public void setLastRejectTime(long lastRejectTime) {
        this.lastRejectTime = lastRejectTime;
    }


    public boolean isLastRefuseTimeOutPassed() {
        if(lastRefuseTime == 0)
            return true;
        long currentTime = CommonUtil.getTime();
        if(currentTime - lastRefuseTimeOut > lastRefuseTime)
            return true;
        return false;
    }
    // 是否加锁
    public void acceptTask(int taskId) {
        TaskStatus isAllow = DispatchTask.isAllowCmd(taskId, Command.AcceptTask);
        if(isAllow==null)
            return;
        DispatchTask.changeStatusByCmd(taskId, Command.AcceptTask);
    }
}
