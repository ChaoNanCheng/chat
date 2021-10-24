package com.wsn.webchat.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.wsn.webchat.event.ChatSessionRepository;
import com.wsn.webchat.event.ParticipantRepository;
import com.wsn.webchat.util.HTMLFilter;
import com.wsn.webchat.vo.SingInVo;
import com.wsn.webchat.vo.WebChatVo;

@Controller
public class WebChatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebChatController.class);

	@Autowired
	private ParticipantRepository participantRepository;
	@Autowired
	private ChatSessionRepository chatSessionRepository;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	/**
	 * 進入chat.jsp
	 * @param model
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/")
	public String chat(Model model, HttpSession httpSession) {
		LOGGER.info(httpSession.getId());

		return "chat";
	}

	/**
	 * 寫入 NickName 登入系統
	 * @param singInVo
	 * @param headerAccessor
	 * @return
	 */
	@MessageMapping("/web/singin/add")
	public String singin(SingInVo singInVo, SimpMessageHeaderAccessor headerAccessor) {
		LOGGER.info(JSON.toJSONString(singInVo));
		LOGGER.info("用戶Id" + singInVo.getUserId() +
			    ",Nickname: " + singInVo.getUserName());
		LOGGER.info(headerAccessor.getSessionId());

		//存入使用者資訊
		participantRepository.add(singInVo.getUserId(), singInVo);
		//存入使用者Socket SessionId所對應的Http Session
		chatSessionRepository.add(headerAccessor.getSessionId(), singInVo.getUserId());

		simpMessagingTemplate.convertAndSend("/ws/web/singin/add/all", singInVo);

		return "success";
	}

	/**
	 * 離開聊天室
	 * @param singInVo
	 * @return
	 */
	@MessageMapping("/web/exit/remove")
	public String exitroom(SingInVo singInVo) {
		LOGGER.info(JSON.toJSONString(singInVo));
		LOGGER.info("用戶Id" + singInVo.getUserId() +
			    ",Nickname: " + singInVo.getUserName());
		//移除使用者資訊
		participantRepository.removeParticipant(singInVo.getUserId());

		simpMessagingTemplate.convertAndSend("/ws/web/exit/remove/all", singInVo);

		return "success";
	}



	/**
	 * 發送訊息
	 * @param webChatVo
	 * @return
	 */
	@MessageMapping("/web/chat")
	public String webChat(WebChatVo webChatVo) {
		LOGGER.info(JSON.toJSONString(webChatVo));
		LOGGER.info("用戶" + webChatVo.getFromUserId() +
				    "向用戶" + webChatVo.getToUserId() +
				    "發送了一條訊息：" + webChatVo.getMessage());
		if(!StringUtils.equals("N", webChatVo.getIsFilter())) {
			String convertMessage = HTMLFilter.filter(webChatVo.getMessage());
			webChatVo.setMessage(convertMessage);
		}
		simpMessagingTemplate.convertAndSend("/ws/web/chat/" + webChatVo.getToUserId(), webChatVo);

		return "success";
	}

	/**
	 * 取得目前線上用戶
	 * @return
	 */
	@MessageMapping("/web/chat/participants")
	public String retrieveParticipants() {
		simpMessagingTemplate.convertAndSend("/ws/web/chat/participants/get", participantRepository.getActiveSessions().values());

		return "success";
	}

	@MessageExceptionHandler
	@SendToUser(value = "/web/chat/errors", broadcast = false)
	public String handleException(Exception exception) {
		LOGGER.error(exception.getMessage(), exception);
		return exception.getMessage();
	}

}
