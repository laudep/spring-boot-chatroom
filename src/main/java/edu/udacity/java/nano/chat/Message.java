package edu.udacity.java.nano.chat;

/** WebSocket message model */
public class Message {

  private String username;
  private MessageType type;
  private String msg;
  private int onlineCount;

  public Message(String username, MessageType type, String msg, int onlineCount) {
    this.username = username;
    this.type = type;
    this.msg = msg;
    this.onlineCount = onlineCount;
  }

  public Message() {}

  public Message(MessageType type, int onlineCount) {
    this.type = type;
    this.onlineCount = onlineCount;
  }

  public String getMsg() {
    return this.msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getOnlineCount() {
    return onlineCount;
  }

  public void setOnlineCount(int onlineCount) {
    this.onlineCount = onlineCount;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public enum MessageType {
    ENTER,
    SPEAK,
    LEAVE
  }
}
