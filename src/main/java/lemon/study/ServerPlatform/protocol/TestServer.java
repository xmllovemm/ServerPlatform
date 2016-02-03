/**
 * 
 */
package lemon.study.ServerPlatform.protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lemon
 *
 */
public class TestServer {
	private int port;
	
	public TestServer(int port) {
		this.port = port;
	}
	
	public void run() {
		EventLoopGroup bossLoop = new NioEventLoopGroup();
		EventLoopGroup workLoop = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossLoop, workLoop);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					// TODO Auto-generated method stub
					sc.pipeline().addFirst(new EchoTimeServerHandler());
				}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 5);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.channel(NioServerSocketChannel.class);
			
			ChannelFuture f = bootstrap.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			bossLoop.shutdownGracefully();
			workLoop.shutdownGracefully();
		}
	}
}
