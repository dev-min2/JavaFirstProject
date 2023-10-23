package CoreAcitive;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HandlerMapping {
	// 요청에 맞는 Controller
	private Map<String,Object> controllerByRequestURL = null;
	
	public HandlerMapping() {
		controllerByRequestURL = new HashMap<String,Object>();
	}
		
	public void init(HashMap<AbstractMap.SimpleEntry<String, Class>,Object> beanObjByIdClass, HandlerAdapter adpater) {
		// Controller인 Bean들을 찾고 HandlerMapping에 엮어준다.
		List<Object> controllerList = beanObjByIdClass.entrySet().
								 						 stream().
								 						 map(entry -> entry.getValue()).
								 						 filter(obj -> obj.getClass().getAnnotation(Controller.class) != null).
								 						 collect(Collectors.toList());
		
		// 요청 프로토콜에 맞는 컨트롤러 반환처리	
		for(Object controllerObj : controllerList) {
			Method[] methods = controllerObj.getClass().getDeclaredMethods();
			List<String> requestKeys = new ArrayList<String>(methods.length); // 최소 메소드수만큼의 Capacity
			for(Method m : methods) {
				var checkAnnotationMethod = m.getAnnotation(RequestMapping.class);
				if(checkAnnotationMethod != null) {
					requestKeys.add(checkAnnotationMethod.value()); // 요청 Value
				}
			}
			
			if(requestKeys.size() <= 0)
				continue;
			
			requestKeys.stream().forEach(str -> controllerByRequestURL.put(str, controllerObj));
		}
		
		adpater.init(controllerByRequestURL);
	}
	
	public Object requestControllerMapping(String protocol) {
		return controllerByRequestURL.get(protocol);
	}
}
