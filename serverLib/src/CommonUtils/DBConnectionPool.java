package CommonUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Queue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class DBConnectionPool {
	private DBConnectionPool() {}
	
	private static DBConnectionPool instance = null;
	private static Object instLock = new Object();
	
	private Queue<Connection> dbConnections = new LinkedList<Connection>();
	private Object connectionLock = new Object();
	
	//jdbc
	private String dbDriver;
	private String dbUrl;
	private String dbId;
	private String dbPwd;
	private int	dbConnectionPoolSize;
	
	public static DBConnectionPool getInstance() {
		if(instance == null) {
			synchronized(instLock) {
				instance = new DBConnectionPool();
			}
		}
		
		return instance;
	}
	
	public void init() throws Exception {
		//dbConnections.add();
		Document xml = Utils.getXmlDoc("server.xml");
		if(xml == null)
			throw new Exception("server.xml is empty");
		
		try {
			Element root = xml.getDocumentElement(); // 최상위 노드
			NodeList childNodelist = root.getElementsByTagName("Resource");
			
			if(childNodelist.getLength() > 0) {
				for(int nodeIndex = 0; nodeIndex < childNodelist.getLength(); ++nodeIndex) {
					NamedNodeMap nodeMap = childNodelist.item(nodeIndex).getAttributes();
					if(nodeMap.getNamedItem("name").getTextContent().equals("jdbc")) {
						dbDriver = nodeMap.getNamedItem("driver").getTextContent();
						dbUrl = nodeMap.getNamedItem("url").getTextContent();
						dbId = nodeMap.getNamedItem("userId").getTextContent();
						dbPwd = nodeMap.getNamedItem("userPwd").getTextContent();
						dbConnectionPoolSize = Integer.parseInt(nodeMap.getNamedItem("poolSize").getTextContent());
					}
				}
			}
			
			Class.forName(dbDriver);
			for(int i = 0; i < dbConnectionPoolSize; ++i) {
				Connection conn = DriverManager.getConnection(dbUrl,dbId,dbPwd); 
				dbConnections.add(conn);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Connection getPoolConnection() {
		// 얻을 수 있을때까지 시도한다.
		Connection conn = null;
		do {
			synchronized(connectionLock) {
				conn = dbConnections.poll();
			}
		}while(conn == null);
		
		return conn;
	}
	
	public void returnConnection(Connection conn) {
		synchronized(connectionLock) {
			dbConnections.offer(conn);
		}
	}
}
