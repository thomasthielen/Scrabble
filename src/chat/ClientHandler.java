package chat;

import chat.messages.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles the received messages of the client
 * 
 * @author tikrause
 *
 */
public class ClientHandler extends SimpleChannelInboundHandler<Message> {

	/**
	 * specifies and handles the messages received from the server
	 * 
	 * @author tikrause
	 * @param ctx
	 * @param msg
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
		System.out.println(msg);
	}

}
