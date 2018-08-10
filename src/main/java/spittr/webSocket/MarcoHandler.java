package spittr.webSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

public class MarcoHandler extends AbstractWebSocketHandler{

	private static final Logger logger = LoggerFactory.getLogger(MarcoHandler.class);
	
	protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception{
		logger.info("received message: "+ message.getPayload());
		
		Thread.sleep(2000);
		
		session.sendMessage(new TextMessage("Polo!"));
	}
	
	public void afterConnectionEstablished(WebSocketSession session){
		logger.info("Connection established");
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,CloseStatus status){
		logger.info("Connection closed. Status :" + status);
	}
}
