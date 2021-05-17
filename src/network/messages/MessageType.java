package network.messages;

/**
 * enum for all types a message can be an instance of.
 *
 * @author tikrause
 */
public enum MessageType {
  CONNECT,
  DISCONNECT,
  ERROR,
  START_GAME,
  SEND_CHAT,
  GAME_STATE,
  DICTIONARY;
}
