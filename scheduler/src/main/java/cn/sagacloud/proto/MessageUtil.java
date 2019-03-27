package cn.sagacloud.proto;
/*
 * Author: Jxing
 * Create Time: 2019/3/22
 */

public class MessageUtil {
    public static MessageProto.Message buildMessage(String cmd, int taskId, String content){
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        builder.setCmd(cmd);
        builder.setTaskId(taskId);
        builder.setContent(content);
        return builder.build();
    }

    public static void printMessage(MessageProto.Message msg){
        System.out.println("msg : cmd : " + msg.getCmd() + ", taskId : " + msg.getTaskId() + ", content : " + msg.getContent());
    }
}
