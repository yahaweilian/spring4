package spittr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import spittr.webSocket.MarcoHandler;

/**
 * WebSocket消息处理器配置
 * @author ynding
 *
 */
//@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//SockJs用于应对不支持WebSocket的场景
		registry.addHandler(marcoHandler(), "/marco").withSockJS();//映射;启用SockJs
		
	}

	@Bean
	public MarcoHandler marcoHandler(){
		return new MarcoHandler();
	}
}
