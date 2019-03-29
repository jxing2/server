package cn.sagacloud.server;

import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.ClientMessage;
import cn.sagacloud.pojo.Command;
import cn.sagacloud.proto.MessageProto;
import cn.sagacloud.proto.MessageUtil;
import cn.sagacloud.utils.CommonUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageHandler implements Runnable {
    public static ConcurrentLinkedQueue<ClientMessage> messageQueue = new ConcurrentLinkedQueue<>();

    public static void handle(MessageProto.Message message, ChannelHandlerContextWrapper clientWrapper) {
        Command cmd;
        try {
            cmd = Command.valueOf(message.getCmd());
        }catch (Exception ex){
            return;
        }
        switch (cmd){
            case AcceptTask:
                clientWrapper.acceptTask(message.getTaskId());
                break;
            case RefuseTask:
                clientWrapper.setLastRejectTime(message.getTaskId());
                break;
            case ClientInfo:
                clientWrapper.setClientInfo(message.getContent());
                break;
            case TaskSuccess:
                clientWrapper.taskSuccess(message);
                break;
            case CommandError:
                clientWrapper.commandError(message);
                break;
            case DownloadError:
                clientWrapper.downloadError(message);
                break;
        }
    }

    public static void offer(ClientMessage clientMessage) {
        MessageUtil.printMessage(clientMessage.getMessage());
        messageQueue.offer(clientMessage);
    }

    @Override
    public void run() {
        while(true){
            ClientMessage clientMessage = messageQueue.poll();
            if(clientMessage == null){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            handle(clientMessage.getMessage(), clientMessage.getCtxWrapper());
        }
    }
}




