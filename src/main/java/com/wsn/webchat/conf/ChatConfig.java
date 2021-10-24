package com.wsn.webchat.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.wsn.webchat.event.HttpSessionEventListener;
import com.wsn.webchat.event.ParticipantRepository;
import com.wsn.webchat.event.PresenceEventListener;
import com.wsn.webchat.event.ChatSessionRepository;



/**
 *
 *
 * @author Wilson Cheng
 */
@Configuration
public class ChatConfig {
	@Bean
	@Description("Keeps connected users")
	public ParticipantRepository participantRepository() {
		return new ParticipantRepository();
	}

	@Bean
	@Description("Keeps connected chatSession")
	public ChatSessionRepository chatSessionRepository() {
		return new ChatSessionRepository();
	}

	@Bean
	@Description("Stocket Session listener")
	public PresenceEventListener presenceEventListener(SimpMessagingTemplate messagingTemplate) {
		PresenceEventListener presence = new PresenceEventListener(messagingTemplate,
				participantRepository(), chatSessionRepository());

		return presence;
	}

	@Bean
	@Description("Http Session listener")
	public HttpSessionEventListener httpSessionEventListener(SimpMessagingTemplate messagingTemplate) {
		HttpSessionEventListener httpSession = new HttpSessionEventListener(messagingTemplate,
				participantRepository(), chatSessionRepository());

		return httpSession;
	}
}
