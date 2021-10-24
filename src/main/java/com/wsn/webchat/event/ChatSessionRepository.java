package com.wsn.webchat.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * access Socket Session and  Http Session
 *
 * @author Wilson Cheng
 */
public class ChatSessionRepository {
	private Map<String, String> activeSessions = new ConcurrentHashMap<>();

	public void add(String socketId, String sessionId) {
		activeSessions.put(socketId, sessionId);
	}

	public void removeSession(String socketId) {
		activeSessions.remove(socketId);
	}

	public String getChatSession(String socketId) {
		return activeSessions.get(socketId);
	}

	public Map<String, String> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, String> activeSessions) {
		this.activeSessions = activeSessions;
	}
}
