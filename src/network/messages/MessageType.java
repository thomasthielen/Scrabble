package network.messages;

/**
 * enum for all types a message can be an instance of
 *
 * @author tikrause
 */
public enum MessageType {
  CONNECT,
  DISCONNECT,
  ERROR,
  SUCCESS,
  SEND_CHAT,
  UPDATE_GAME_STATE;
}
