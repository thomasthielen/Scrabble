package network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import network.messages.Message;
import network.messages.SendChatMessage;

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
		SendChatMessage message = (SendChatMessage) msg;
		System.out.println(message.getMessage());
	}

}
