package SockNet;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

// Accept 처리
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void>, Closeable {
	private InetSocketAddress addr;
	private AsynchronousChannelGroup channelGroup;
	
	// 클라이언트 연결수락
	private AsynchronousServerSocketChannel asyncServerSocketChannel;
	private final AtomicInteger sessionCnt = new AtomicInteger(0);
	private HashMap<Integer,Session> sessionByID = new HashMap<Integer,Session>();
	
	private Object sessionLock = new Object();
	
	
	public AcceptCompletionHandler(InetSocketAddress addr, AsynchronousChannelGroup chanel, AsynchronousServerSocketChannel socketChannel) {
		this.addr = addr;
		this.channelGroup = chanel;
		this.asyncServerSocketChannel = socketChannel;
	}
	
	public void accept() {
		asyncServerSocketChannel.accept(null, this);
	}
	
	public void stop() {
		if(asyncServerSocketChannel != null && asyncServerSocketChannel.isOpen()) {
			synchronized(sessionLock) {
				for(Entry<Integer,Session> ety : sessionByID.entrySet()) {
					Session s = ety.getValue();
					if(s != null)
						closeSession(s.getSessionId());
				}
			}
			try {
				asyncServerSocketChannel.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void completed(AsynchronousSocketChannel result, Void attachment) {
		try {
			Session session = new Session();
			int sessionid = sessionCnt.incrementAndGet();
			synchronized(sessionLock) {
				sessionByID.put(sessionid, session);
			}
			
			System.out.println(result.getRemoteAddress() + " 연결 (" + new Date(System.currentTimeMillis()).toString() + ")");
			
			session.Init(result, sessionid, this);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			accept(); // 다시 accept처리.
		}
	}

	@Override
	public void failed(Throwable exc, Void attachment) {
		if(asyncServerSocketChannel.isOpen()) {
			try {
				stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		stop();
	}
	
	public void closeSession(int sessionId) {
		Session closeSession = null;
		synchronized(sessionLock) {
			closeSession = sessionByID.get(sessionId);
		}
		
		if(closeSession == null)
			return;
		
		closeSession.close();
	}
}
