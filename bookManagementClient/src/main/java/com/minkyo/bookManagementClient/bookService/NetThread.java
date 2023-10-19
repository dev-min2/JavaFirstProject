package com.minkyo.bookManagementClient.bookService;

import java.util.List;

import PacketUtils.Packet;
import SockNet.NetClient;

public class NetThread extends Thread {
	NetClient net = new NetClient();
	Runnable run = null;
	
	public NetThread() {
		run = new Runnable() {
			@Override
			public void run() {
			}
		};
	}
//	
//	public void processRecv() {
//		List<Packet> packets = null;
//		try {
//			packets = net.recv();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
}
