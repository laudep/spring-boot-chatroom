package edu.udacity.java.nano;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class WebSocketChatApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketChatApplication.class, args);
  }

  /** Login Page */
  @GetMapping("/")
  public ModelAndView login() {
    return new ModelAndView("login");
  }

  /** Chatroom Page */
  @GetMapping("/index")
  public ModelAndView index(String username, HttpServletRequest request)
      throws UnknownHostException {
    StringBuffer url = request.getRequestURL();
    String wsURL =
        "ws://"
            + InetAddress.getLocalHost().getHostName()
            + ":"
            + request.getServerPort()
            + request.getContextPath()
            + "/chat";

    ModelAndView modelAndView = new ModelAndView("chat");

    modelAndView.addObject("username", username);
    modelAndView.addObject("webSocketUrl", wsURL);

    return modelAndView;
  }
}
