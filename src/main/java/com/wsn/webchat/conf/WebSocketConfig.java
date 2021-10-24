package com.wsn.webchat.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration //配置文件
@EnableWebSocketMessageBroker //開啟webstocket message 代理
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		//核對message 代理的url前綴
		config.enableSimpleBroker("/ws", "/user");
		config.setApplicationDestinationPrefixes("/ws");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 配置sockjs連接的入口
		registry.addEndpoint("/ws").withSockJS();
	}

}
