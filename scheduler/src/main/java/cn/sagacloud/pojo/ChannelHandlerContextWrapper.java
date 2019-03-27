package cn.sagacloud.pojo;

import io.netty.channel.ChannelHandlerContext;

public class ChannelHandlerContextWrapper {
    private ChannelHandlerContext ctx;  // 通信handler
    private long lastRejectTime;        // unix时间片, 精确到秒
    private String macAddr;             // mac地址

    public ChannelHandlerContextWrapper(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.lastRejectTime = 0;
        macAddr = null;
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

//    public void setCtx(ChannelHandlerContext chx) {
//        this.ctx = chx;
//    }

    public long getLastRejectTime() {
        return lastRejectTime;
    }

    public void setLastRejectTime(long lastRejectTime) {
        this.lastRejectTime = lastRejectTime;
    }
}
