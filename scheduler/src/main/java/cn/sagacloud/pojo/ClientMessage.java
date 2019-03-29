package cn.sagacloud.pojo;


import cn.sagacloud.proto.MessageProto;

public class ClientMessage {
    private ChannelHandlerContextWrapper ctxWrapper;
    private MessageProto.Message message;

    public ClientMessage(ChannelHandlerContextWrapper ctxWrapper, MessageProto.Message message) {
        this.ctxWrapper = ctxWrapper;
        this.message = message;
    }

    public ChannelHandlerContextWrapper getCtxWrapper() {
        return ctxWrapper;
    }

    public void setCtxWrapper(ChannelHandlerContextWrapper ctxWrapper) {
        this.ctxWrapper = ctxWrapper;
    }

    public MessageProto.Message getMessage() {
        return message;
    }

    public void setMessage(MessageProto.Message message) {
        this.message = message;
    }
}
