package com.wsn.webchat.event;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.wsn.webchat.vo.SingInVo;


/**
 * Listener Socket Session
 *
 * @author Wilson Cheng
 */
public class PresenceEventListener implements ApplicationListener<ApplicationEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(PresenceEventListener.class);

	private SimpMessagingTemplate messagingTemplate;
	private ParticipantRepository participantRepository;
	private ChatSessionRepository chatSessionRepository;

	public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository, ChatSessionRepository chatSessionRepository) {
		this.messagingTemplate = messagingTemplate;
		this.participantRepository = participantRepository;
		this.chatSessionRepository  = chatSessionRepository;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof SessionConnectEvent) {
			handleSessionConnected((SessionConnectEvent) event);
		} else if(event instanceof SessionDisconnectEvent) {
			handleSessionDisconnect((SessionDisconnectEvent) event);
		}
	}

	private void handleSessionConnected(SessionConnectEvent event) {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		LOGGER.info(headers.getSessionId());
	}


	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		LOGGER.info(event.getSessionId());
		SingInVo singInVo = null;
		//取得Http Session
		String httpSessoinId = chatSessionRepository.getChatSession(event.getSessionId());

		//取得SingVo
		if(StringUtils.isNotBlank(httpSessoinId)) {
			singInVo = participantRepository.getParticipant(httpSessoinId);
			messagingTemplate.convertAndSend("/ws/web/exit/remove/all", singInVo);

			//移除
			chatSessionRepository.removeSession(event.getSessionId());
			participantRepository.removeParticipant(httpSessoinId);
		}

	}


}
