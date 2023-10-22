package com.minkyo.bookManagementPacket.BookList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("6")
public class ADDITIONAL_BOOK_INFO_ACK extends Packet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 13002L;
	public NetError netError;
	public byte[] imageBuffer; // 이미지 정보
	public boolean canRentBook; // 대여 가능 정보
	public int rentMemberUID; // 대여한 회원의 UID
	public int rentNo; // 아무도 대여하지않았다면 0
	
	private void writeObject(ObjectOutputStream out) throws IOException
	{
		out.defaultWriteObject(); // defaultWriteObject는 현재 자신 클래스의 멤버를 자동으로 직렬화
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject(); // defaultReadObject는 현재 자신 클래스의 멤버를 자동으로 역직렬화
	}
}
