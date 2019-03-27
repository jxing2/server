package cn.sagacloud.server;
/*
 * Author: Jxing
 * Create Time: 2019/3/20
 */

import cn.sagacloud.pojo.ChannelHandlerContextWrapper;
import cn.sagacloud.pojo.Config;
import cn.sagacloud.proto.MessageProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;

public class Server {
    ServerBootstrap bootstrap = new ServerBootstrap();
    private NioEventLoopGroup acceptGroup = new NioEventLoopGroup();
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel = null;
    Config config;
    ArrayList<ChannelHandlerContextWrapper> clientList = new ArrayList<>();
    public Server(){
        // 初始化serverSocket
        bootstrap.group(acceptGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ProtobufVarint32FrameDecoder());
                        // 设置解码
                        pipeline.addLast("decoder", new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));
                        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                        // 设置编码
                        pipeline.addLast("encoder", new ProtobufEncoder());
                        pipeline.addLast(new SchedulerHandler(clientList));

                    }
                })
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
        // 初始化配置
        config = new Yaml().loadAs(Server.class.getClassLoader().getResourceAsStream("config.yml"), Config.class);
        // 初始化Task(从sql到内存)
        DispatchTask dispatchTask = new DispatchTask();

    }

    public void start() throws InterruptedException {
        try {
            // 启动服务
            channel = bootstrap.bind(config.getPort()).sync().channel();


            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            acceptGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
//        int port = 6666;
//        if (args.length > 0) {
//            port = Integer.parseInt(args[0]);
//        }
        new Server().start();
    }

}
