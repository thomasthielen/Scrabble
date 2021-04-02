package chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Object> {

	/**
	 * @author tikrause
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
	}

}
