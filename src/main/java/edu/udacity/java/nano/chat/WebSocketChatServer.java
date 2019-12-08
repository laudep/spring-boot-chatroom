package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session WebSocket Session
 */
@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

  /** All chat sessions. */
  private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

  private static void sendMessageToAll(String msg) {
    onlineSessions.values().stream()
        .filter(session -> session.isOpen())
        .forEach(
            session -> {
              try {
                session.getBasicRemote().sendText(msg);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  /** Open connection, 1) add session, 2) add user. */
  @OnOpen
  public void onOpen(Session session) {
    this.onlineSessions.put(session.getId(), session);

    Message message = new Message(Message.MessageType.ENTER, onlineSessions.size());
    sendMessageToAll(JSON.toJSONString(message));
  }

  /** Send message, 1) get username and session, 2) send message to all. */
  @OnMessage
  public void onMessage(Session session, String jsonStr) {
    this.sendMessageToAll(jsonStr);
  }

  /** Close connection, 1) remove session, 2) update user. */
  @OnClose
  public void onClose(Session session) {
    this.onlineSessions.remove(session.getId());

    try {
      session.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Message leaveMessage = new Message(Message.MessageType.LEAVE, onlineSessions.size());
    String jsonMessage = JSON.toJSONString(leaveMessage);
    sendMessageToAll(jsonMessage);
  }

  /** Print exception. */
  @OnError
  public void onError(Session session, Throwable error) {
    error.printStackTrace();
  }
}
