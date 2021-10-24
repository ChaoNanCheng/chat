package com.wsn.webchat.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.wsn.webchat.vo.SingInVo;

/**
 *
 *
 * @author Wilson Cheng
 */
//@Component
public class ParticipantRepository {
	private Map<String, SingInVo> activeSessions = new ConcurrentHashMap<>();

	public void add(String key, SingInVo singInVo) {
		activeSessions.put(key, singInVo);
	}

	public void removeParticipant(String key) {
		activeSessions.remove(key);
	}

	public SingInVo getParticipant(String key) {
		return activeSessions.get(key);
	}

	public Map<String, SingInVo> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, SingInVo> activeSessions) {
		this.activeSessions = activeSessions;
	}
}
