package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.Greeting;
import com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto.HelloMessage;

/** Controller, der (WebSocket-)Nachrichten entgegennimmt und beantwortet.
 */
@Controller
public class GreetingController {

  //Endpunkt fuer Nachrichten an "/app/hello" (Praefix "/app" ist in WebSocketConfig festgelegt)
  @MessageMapping("/hello")
  //Festlegen, dass der Return-Wert der Methode in eine Message konvertiert und an alle Subscriber von
  //"/topic/greetings" verschickt werden soll
  @SendTo("/topic/greetings")
  public Greeting greeting(HelloMessage message) throws Exception {
    Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }

}
