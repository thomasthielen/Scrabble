package chat;

import chat.messages.SendChatMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<Object> {

	/**
	 * @author tikrause
	 */
	private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	
	/**
	 * @author tikrause
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel in = ctx.channel();
		for (Channel channel : channels) {
			channel.write("[SERVER] - " + in.remoteAddress() + " has joined!\n");
		}
		channels.add(ctx.channel());
	}
	
	/**
	 * @author tikrause
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel in = ctx.channel();
		for (Channel channel : channels) {
			channel.writeAndFlush("[SERVER] - " + in.remoteAddress() + " has left!\n");
		}
		channels.remove(ctx.channel());
	}
	
	/**
	 * @author tikrause
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		SendChatMessage message = (SendChatMessage) msg;
		Channel	in = ctx.channel();
		for (Channel channel : channels) {
			if (channel != in) {
				channel.writeAndFlush("[" + in.remoteAddress() + "] " + message.getMessage() + "\n");
			}
		}
	
	}

	
}
