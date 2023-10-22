package com.minkyo.bookManagementPacket.BookList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("9")
public class MY_BOOK_HISTORY_RENT_ACK extends Packet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9002L;
	public NetError netError;
	public List<String> bookTitleList;
	public List<BookHistoryVO> historyVOList; 
	public String nickName;
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject(); // defaultWriteObject는 현재 자신 클래스의 멤버를 자동으로 직렬화
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject(); // defaultReadObject는 현재 자신 클래스의 멤버를 자동으로 역직렬화
	}
}
