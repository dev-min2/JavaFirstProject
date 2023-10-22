package com.minkyo.bookManagementPacket.BookList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("ADDITIONAL_BOOK_INFO_REQ")
public class ADDITIONAL_BOOK_INFO_REQ extends Packet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 13001L;
	public int bookNo;
	public String bookTitle;
	public boolean existImageFile;// 이미 Local에 이미지를 가지고 있을 때 true
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject(); // defaultWriteObject는 현재 자신 클래스의 멤버를 자동으로 직렬화
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject(); // defaultReadObject는 현재 자신 클래스의 멤버를 자동으로 역직렬화
	}
}
