package com.wsn.webchat.event;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

/**
 * Listener HttpSession
 *
 * @author Wilson Cheng
 */
public class HttpSessionEventListener implements ApplicationListener<ApplicationEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpSessionEventListener.class);

	private SimpMessagingTemplate messagingTemplate;
	private ParticipantRepository participantRepository;
	private ChatSessionRepository chatSessionRepository;

	public HttpSessionEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository, ChatSessionRepository chatSessionRepository) {
		this.messagingTemplate = messagingTemplate;
		this.participantRepository = participantRepository;
		this.chatSessionRepository  = chatSessionRepository;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof HttpSessionCreatedEvent) {
			handleHttpSessionConnected((HttpSessionCreatedEvent) event);
		} else if(event instanceof HttpSessionDestroyedEvent) {
			handleHttpSessionDestroyed((HttpSessionDestroyedEvent) event);
		}
	}

	private void handleHttpSessionConnected(HttpSessionCreatedEvent event) {
		HttpSession httpSession = event.getSession(); //get session object
        String sessionId = httpSession.getId(); //get session id
        //httpSession.setMaxInactiveInterval(10);//set timeout 10 secs
        LOGGER.info(sessionId);
	};

	private void handleHttpSessionDestroyed(HttpSessionDestroyedEvent event) {
		HttpSession httpSession = event.getSession(); //get session object
        String sessionId = httpSession.getId(); //get session id
        LOGGER.info(sessionId);
	};

}
