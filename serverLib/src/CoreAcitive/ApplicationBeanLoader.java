package CoreAcitive;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.AbstractMap;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import CommonUtils.Utils;


/*
IoC/DI를 위해 등록된 xml정보를 파싱
*/
public final class ApplicationBeanLoader {
	private ApplicationBeanLoader() {}
	
	@SuppressWarnings("null")
	public static HashMap<AbstractMap.SimpleEntry<String, Class>,Object> parseBean() throws Exception {
		var ret = new HashMap<AbstractMap.SimpleEntry<String, Class>,Object>();
		// Class 파싱용
		HashMap<String,Class> temp = new HashMap<String,Class>();

		// 클래스 기본경로
		Document xml = Utils.getXmlDoc("bean.xml");
		if(xml == null)
			throw new Exception("Not exist bean.xml");
		
		Element root = xml.getDocumentElement(); // 최상위 노드
		NodeList childNodelist = root.getElementsByTagName("bean");
		if(childNodelist.getLength() > 0) {
			for(int nodeIndex = 0; nodeIndex < childNodelist.getLength(); ++nodeIndex) {
				Node xmlNode = childNodelist.item(nodeIndex);
				
				if(xmlNode.getNodeName().equals("bean")) {
					NamedNodeMap nodeMap = xmlNode.getAttributes();
					
					String id = nodeMap.getNamedItem("id").getTextContent();
					String className = nodeMap.getNamedItem("class").getTextContent();
					Object obj = null;
					
					if(id == null || id.isEmpty() || className == null || className.isEmpty())
						throw new Exception("bean xml error(id == null || id.isEmpty() || className == null || className.isEmpty())");
					
					// 해당하는 클래스가 없다면 여기서 Exception.
					Class match = Class.forName(className);
					
					// Bean 객체의 하위 노드
					NodeList beanChildNodeList = xmlNode.getChildNodes();
					
					// 의존성 주입(DI / 현재는 생성자를 통한 주입만 있음)
					if(beanChildNodeList.getLength() > 0) {
						Class[] refClassArray = new Class[match.getDeclaredConstructors()[0].getParameterCount()];
						String[] refIDs = new String[refClassArray.length];
						Object[] primitiveAndStrObj = new Object[refIDs.length];
						int idx = 0;
						
						for(int beanChildNodeIndex = 0; beanChildNodeIndex < beanChildNodeList.getLength(); ++beanChildNodeIndex) {
							Node beanChildNode = beanChildNodeList.item(beanChildNodeIndex);
							NamedNodeMap beanNodeMap = beanChildNode.getAttributes();
							
							String nodeName = beanChildNode.getNodeName();
							if(nodeName.equals("constructor-arg")) {
								Node constructorArgumentAttribute = beanNodeMap.getNamedItem("ref");
								if(constructorArgumentAttribute != null) { // 참조타입
									String refId = constructorArgumentAttribute.getTextContent();
									if(!temp.containsKey(refId))
										throw new Exception("not containsKey refID");
									
									refClassArray[idx] = temp.get(refId);
									refIDs[idx++] = refId;	
								}
								else { // 기본형타입(int,double) 및 문자열
									constructorArgumentAttribute = beanNodeMap.getNamedItem("value");
									if(constructorArgumentAttribute == null)
										throw new Exception("Unknown attribute type");
									
									String value = constructorArgumentAttribute.getTextContent();
									if(Utils.isInt(value)) {
										primitiveAndStrObj[idx] = Integer.valueOf(value);
										refClassArray[idx] = int.class;
									}
									else if(Utils.isDouble(value)) {
										primitiveAndStrObj[idx] = Double.valueOf(value);
										refClassArray[idx] = double.class;
									}
									else {
										primitiveAndStrObj[idx] = value;
										refClassArray[idx] = String.class;
									}
									
									++idx;
								}
							}
						}
						Constructor constructor = match.getConstructor(refClassArray);
						Object[] argumentObject = new Object[refClassArray.length];
						for(int i = 0; i < argumentObject.length; ++i) {
							if(refClassArray[i] == int.class || refClassArray[i] == double.class || refClassArray[i] == String.class) 
								argumentObject[i] = primitiveAndStrObj[i];
							else
								argumentObject[i] = ret.get(new AbstractMap.SimpleEntry<String, Class>(refIDs[i],refClassArray[i]));
						}
						
						obj = constructor.newInstance(argumentObject);
					}
					else {
						// 기본 생성자(주입없는것들)
						obj = match.getDeclaredConstructor().newInstance();
					}
					
					temp.put(id, match);
					ret.put(new AbstractMap.SimpleEntry<String, Class>(id,match), obj);
				}
			}
		}
		
		
		return ret;
	}
}

