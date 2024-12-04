package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /**
   * Konfiguriert einen Message Broker, also eine Komponente, die Nachrichten von einem oder mehreren 
   * Publishern entgegennimmt und diese an einen oder mehrere Subscriber weiterleitet. 
   * Als Publisher und Subscriber koennen die Clients oder auch der Application Server auftreten,
   * Als Messaging-Protokoll dient hier insb. STOMP (Streaming Text Oriented Messaging Protocol),
   * das auf WebSocket basiert. STOMP bietet z. B. "High-Level-"Befehle wie CONNECT, SEND, SUBSCRIBE, 
   * UNSUBSCRIBE und DISCONNECT.
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {

	  /* Einen einfachen eingebauten Nachrichtenbroker aktivieren, der Nachrichten 
	   * an bestimmte Ziele weiterleitet. Hier bedeutet "/topic", dass der Broker Nachrichten 
	   * an Ziele mit dem Praefix "/topic" weiterleiten kann. Dies wird in der Regel fuer die 
	   * Publish-Subscribe-Kommunikation verwendet, bei der mehrere Clients auf ein gemeinsames 
	   * Thema (Topic) zugreifen koennen. Beispiel: Wenn ein Client eine Nachricht an "/topic/greetings" 
	   * sendet, werden alle Clients, die dieses Thema abonniert haben, diese Nachricht erhalten.
	   */
	  config.enableSimpleBroker("/topic");
	
      /* Festlegen, dass alle Nachrichten, die vom Client an den Server gesendet werden und die Verarbeitung 
       * durch Controller erfordern, mit dem Praefix "/app" beginnen muessen. Wenn ein Client also eine Nachricht 
       * an "/app/message" sendet, wird diese Nachricht an einen entsprechenden Controller-Handler auf Serverseite 
       * weitergeleitet. Durch die verschiedenen Praefix-Typen koennen Nachrichten, die an den Broker gehen 
       * ("/topic"), von Nachrichten, die vom Server verarbeitet werden ("/app"), unterschieden werden.
	   */
	  config.setApplicationDestinationPrefixes("/app");
  }

  /** STOMP-Endpunkt konfigurieren, ueber den die Clients eine (WebSocket- bzw. STOMP-Verbindung) zum 
   *  Server herstellen koennen.
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {

	  /* Endpunkt hinzufuegen, ueber den sich ein Client mit dem WebSocket-Server verbinden kann, 
	   * um Nachrichten zu senden und zu empfangen.
	   * Beispiel: Wenn der Server auf http://localhost:8080 laeuft, koennen sich WebSocket-Clients ueber 
       * ws://localhost:8080/gs-guide-websocket mit dem Server verbinden
       */
	  registry.addEndpoint("/websocket")
	  .setAllowedOrigins("http://localhost:3000") // Allow these origins for WebSocket communication
    	.withSockJS();
	  	  
      /* Um Verbindungen von anderen Domains zu erlauben (z. B. bei Verwendung eines Frontend-Framework wie Vue.js),
       * koennen die zugelassenen Domains spezifiziert werden.
       * Optional kann der Endpunkt die SockJS-Bibliothek verwenden, um WebSocket-Kommunikation auch dann zu 
       * unterstuetzen, wenn WebSockets nativ nicht verfuegbar sind (z. B. bei aelteren Browsern oder 
       * bestimmten Netzwerkumgebungen, die WebSockets blockieren).
       */
//	  registry.addEndpoint("/gs-guide-websocket")
//		.setAllowedOrigins("http://localhost:8081") // Allow these origins for WebSocket communication
//		.withSockJS();

  }

}
