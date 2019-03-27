package cn.sagacloud.server;
/*
 * Author: Jxing
 * Create Time: 2019/3/20
 */

import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.Command;
import cn.sagacloud.pojo.TaskStatus;
import cn.sagacloud.proto.MessageProto;
import cn.sagacloud.proto.MessageUtil;
import cn.sagacloud.utils.CommonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulerHandler extends ChannelInboundHandlerAdapter {
    ArrayList<ChannelHandlerContextWrapper> clientList;
    private ChannelHandlerContextWrapper ctxw;
    public static Map<TaskStatus, Set<Command>> allowedStatusCommand = new ConcurrentHashMap<>();
    static {
        allowedStatusCommand.put(TaskStatus.Waiting, CommonUtil.createSet(Command.TaskSuccess));
        allowedStatusCommand.put(TaskStatus.Sending, CommonUtil.createSet(Command.AcceptTask, Command.RefuseTask, Command.TaskSuccess));
        allowedStatusCommand.put(TaskStatus.Sent, CommonUtil.createSet(Command.CommandError, Command.DownloadError, Command.TaskSuccess));
        allowedStatusCommand.put(TaskStatus.FileDownloadException, CommonUtil.createSet(Command.TaskSuccess));
        allowedStatusCommand.put(TaskStatus.CommandExecuteException, CommonUtil.createSet(Command.TaskSuccess));
        allowedStatusCommand.put(TaskStatus.Finished, CommonUtil.createSet());
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
        SocketAddress addr = ctx.channel().remoteAddress();
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
        //ctx.channel().writeAndFlush("connected !"+System.currentTimeMillis());
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
            MessageUtil.printMessage(message);
            MessageProto.Message myMsg = MessageUtil.buildMessage(message.getCmd() + 1,
                    message.getTaskId(),
                    message.getContent() + " from server");

            ctx.writeAndFlush(myMsg);  // 异步
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
