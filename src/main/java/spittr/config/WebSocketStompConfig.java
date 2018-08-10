package spittr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

//@Configuration
@EnableWebSocketMessageBroker//启用STOMP消息
public class WebSocketStompConfig extends AbstractWebSocketMessageBrokerConfigurer{

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/macropolo").withSockJS();//将“/marcopolo”注册为STOMP端点
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry){
		//所有目的地前缀为“/topic”或“/queue”的消息都会发送到STOMP代理中
		registry.enableStompBrokerRelay("/queue","/topic");//代理中继
		//所有目的地以“/app”打头的消息都将会路由
		//到带有@MessageMapping注解的方法中， 而不会发布到代理队列或主题中
		registry.setApplicationDestinationPrefixes("/app");
	}
}
