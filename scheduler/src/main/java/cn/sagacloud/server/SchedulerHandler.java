package cn.sagacloud.server;
/*
 * Author: Jxing
 * Create Time: 2019/3/20
 */

import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.ClientMessage;
import cn.sagacloud.pojo.Command;
import cn.sagacloud.pojo.TaskStatus;
import cn.sagacloud.proto.MessageProto;
import cn.sagacloud.proto.MessageUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulerHandler extends ChannelInboundHandlerAdapter {
    ArrayList<ChannelHandlerContextWrapper> clientList;
    private ChannelHandlerContextWrapper ctxw;
    // Map<当前状态, Map<可接受的命令, 接受完命令后的状态>>
    public static Map<TaskStatus, Map<Command, TaskStatus>> statusTransitionMap = new ConcurrentHashMap<>();
    static {
        Map<Command, TaskStatus> waitingMap = new HashMap<>();
        Map<Command, TaskStatus> sendingMap = new HashMap<>();
        Map<Command, TaskStatus> sentMap = new HashMap<>();
        Map<Command, TaskStatus> fileDownloadExceptionMap = new HashMap<>();
        Map<Command, TaskStatus> commandExceptionMap = new HashMap<>();
        statusTransitionMap.put(TaskStatus.Waiting, waitingMap);//CommonUtil.createSet(Command.TaskSuccess));
        statusTransitionMap.put(TaskStatus.Sending, sendingMap);//CommonUtil.createSet(Command.AcceptTask, Command.RefuseTask, Command.TaskSuccess));
        statusTransitionMap.put(TaskStatus.Sent, sentMap);//CommonUtil.createSet(Command.CommandError, Command.DownloadError, Command.TaskSuccess));
        statusTransitionMap.put(TaskStatus.FileDownloadException, fileDownloadExceptionMap);//CommonUtil.createSet(Command.TaskSuccess));
        statusTransitionMap.put(TaskStatus.CommandExecuteException, commandExceptionMap);//CommonUtil.createSet(Command.TaskSuccess));
        statusTransitionMap.put(TaskStatus.Finished, new HashMap<>());

        waitingMap.put(Command.TaskSuccess, TaskStatus.Finished);
        waitingMap.put(Command.DownloadError, TaskStatus.FileDownloadException);
        waitingMap.put(Command.CommandError, TaskStatus.CommandExecuteException);
        waitingMap.put(Command.AcceptTask, TaskStatus.Sent);

        sendingMap.put(Command.AcceptTask, TaskStatus.Sent);
        sendingMap.put(Command.RefuseTask, TaskStatus.Waiting);
        sendingMap.put(Command.TaskSuccess, TaskStatus.Finished);
        sendingMap.put(Command.DownloadError, TaskStatus.FileDownloadException);
        sendingMap.put(Command.CommandError, TaskStatus.CommandExecuteException);

        sentMap.put(Command.TaskSuccess, TaskStatus.Finished);
        sentMap.put(Command.DownloadError, TaskStatus.FileDownloadException);
        sentMap.put(Command.CommandError, TaskStatus.CommandExecuteException);

        fileDownloadExceptionMap.put(Command.TaskSuccess, TaskStatus.Finished);
        commandExceptionMap.put(Command.TaskSuccess, TaskStatus.Finished);
    }
    public SchedulerHandler(ArrayList<ChannelHandlerContextWrapper> clientList) {
        this.clientList = clientList;
    }

    /**
     * 刚添加上handler的时候    1
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //SocketAddress addr = ctx.channel().remoteAddress();
        System.out.println("client incoming");
    }

    /**
     *  客户端活动状态         2
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctxw = new ChannelHandlerContextWrapper(ctx);
        this.clientList.add(ctxw);
        ctx.channel().writeAndFlush(MessageUtil.buildMessage(Command.ClientInfo.name(), 0, ""));
    }

    /**
     * 客户端服务器互发消息      3
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            // Do something with msg
            MessageProto.Message message = (MessageProto.Message)msg;
            MessageHandler.offer(new ClientMessage(ctxw, message));
        } finally {
            // 如果ctx write 或 writeAndFlush过的话, 就不用释放msg
            //ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 客户端异常原因  4
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("client exceptionCaught : " + cause.getMessage());
    }

    /**
     * 客户端非活动状态  5
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("client channelInactive");
    }

    /**
     * 客户端断开连接 6
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        this.clientList.remove(ctxw);
    }
}
