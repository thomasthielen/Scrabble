package network.messages;

/**
 * enum for all types a message can be an instance of.
 *
 * @author tikrause
 */
public enum MessageType {
  CONNECT,
  DISCONNECT,
  START_GAME,
  END_GAME,
  SEND_CHAT,
  GAME_STATE,
  DICTIONARY,
  NOTIFY_AI,
  TOO_FEW;
}
