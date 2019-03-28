package cn.sagacloud.server;

import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.Command;
import cn.sagacloud.proto.MessageProto;
import cn.sagacloud.utils.CommonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.checkerframework.checker.formatter.FormatUtil;

public class MessageHandler {
    public static void handle(MessageProto.Message message, ChannelHandlerContextWrapper ctx) {
        Command cmd;
        try {
            cmd = Command.valueOf(message.getCmd());
        }catch (Exception ex){
            return;
        }
        switch (cmd){
            case AcceptTask:
                ctx.acceptTask(message.getTaskId());
                break;
            case RefuseTask:
                ctx.setLastRejectTime(CommonUtil.getTime());
                break;
            case ClientInfo:
                break;
            case TaskSuccess:
                break;
            case CommandError:
                break;
            case DownloadError:
                break;
        }
    }
}
