package org.github.tjg.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server implements Runnable {

    private int port;

    private Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // how many threads are used can be determined by EventLoopGroup constructor
        try {
            // sets up a server by ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(workerGroup)
                    // NIO selector: to accept incoming connections
                    .channel(NioServerSocketChannel.class)
                    // always be evaluated by a newly accepted Channel, 使用DiscardServerHandler实现逻辑
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String args[]) {
        new Server(8080).run();
    }
}
