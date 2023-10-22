package com.minkyo.bookManagementPacket.BookList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("ADMIN_REGIST_BOOK_REQ")
public class ADMIN_REGIST_BOOK_REQ extends Packet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 12001L;
	public String bookTitle;
	public String bookAuthor;
	public String bookPublisher;
	public byte[] imageBuffer;
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject(); // defaultWriteObject는 현재 자신 클래스의 멤버를 자동으로 직렬화
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject(); // defaultReadObject는 현재 자신 클래스의 멤버를 자동으로 역직렬화
	}
}
