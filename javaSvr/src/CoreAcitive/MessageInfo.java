package CoreAcitive;

import java.sql.Date;
import java.util.Calendar;
import java.util.Map;


public class MessageInfo {
	private Date packetArriveTime = null;
	// DispatchBot에서 던져주는 sessionData
	private Map<String,String> sessionDataByJsonKey = null;
	
	public MessageInfo(Map<String,String> sessionDataByJsonKey) {
		// 조금 느린거같아서 우선 주석처리.
		//packetArriveTime = Calendar.getInstance();
		this.sessionDataByJsonKey = sessionDataByJsonKey; 
	}
	
	public String getParameter(String key) {
		return sessionDataByJsonKey.get(key);
	}
	
	public void setParameter(String key, String value) {
		sessionDataByJsonKey.put(key, value);
	}
	
	public String getTime() {
		return packetArriveTime.toString();
	}
	
	public void deleteSession() {
		sessionDataByJsonKey = null;
	}
}