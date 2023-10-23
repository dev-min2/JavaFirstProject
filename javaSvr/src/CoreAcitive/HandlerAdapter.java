package CoreAcitive;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import PacketUtils.Packet;

public class HandlerAdapter {
	
	private Map<String,Method> requestMethodByProtocol = null;
	
	public HandlerAdapter() {
		requestMethodByProtocol = new HashMap<String,Method>();
	}
	
	public void init(Map<String,Object> controllerByRequestURL) {
		for(Entry<String,Object> entry : controllerByRequestURL.entrySet()) {
			Object controller = entry.getValue();
			Method[] methods = controller.getClass().getDeclaredMethods();
			for(Method m : methods) {
				String requestURLkey = m.getDeclaredAnnotation(RequestMapping.class).value();
				if(requestURLkey == null)
					continue;
				
				requestMethodByProtocol.put(requestURLkey, m);
			}
		}
	}

	public Packet requestProcessing(String protocol, Object controller, Packet requestPacket, MessageInfo msgInfo) throws Exception {
		// 앞에서 유효성 검사했으니 따로 매개변수 유효검사 x
		Method method = requestMethodByProtocol.get(protocol);
		if(method == null)
			return null;
		
		Packet ackPacket = null;
		try {
			ackPacket = (Packet)method.invoke(controller, requestPacket, msgInfo);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return ackPacket;
	}
}
