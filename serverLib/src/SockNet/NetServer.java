package SockNet;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

import CommonUtils.DBConnectionPool;
import CoreAcitive.DispatcherBot;

/*
 소켓 Async Server
 */

public final class NetServer implements Closeable {
	// accept 핸들러
	private AcceptCompletionHandler acceptHandler = null;
	
	// Core
	public NetServer(int port) throws Exception {
		DispatcherBot.getDispatcherBot().init();
		DBConnectionPool.getInstance().init();
		
		// Network 처리
		InetAddress temp = null;
		InetSocketAddress sockAddr = new InetSocketAddress(temp,port);
		AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withFixedThreadPool(
				Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
		
		AsynchronousServerSocketChannel asyncSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
		asyncSocketChannel.bind(sockAddr);
        
        acceptHandler = new AcceptCompletionHandler(sockAddr, channelGroup, asyncSocketChannel);
	}
	
	public void startServer() {
		acceptHandler.accept();
		
		System.out.println("Server OK.");
		
		while(true) {
			try {
				Thread.currentThread().sleep(1000000);
			}
			catch(Exception e) {
				
			}
		}
	}
	
	
	
	@Override
	public void close() throws IOException {
		acceptHandler.close();
	}
}
