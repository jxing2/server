package cn.sagacloud.proto;
/*
 * Author: Jxing
 * Create Time: 2019/3/22
 */

public class MessageUtil {
    public static MessageProto.Message buildMessage(int cmd, String className, String content){
        MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
        builder.setCmd(cmd);
        builder.setClassName(className);
        builder.setContent(content);
        return builder.build();
    }

    public static void printMessage(MessageProto.Message msg){
        System.out.println("msg : cmd : " + msg.getCmd() + ", className : " + msg.getClassName() + ", content : " + msg.getContent());
    }
}
