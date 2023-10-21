package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("6")
public class ADDITIONAL_BOOK_INFO_ACK extends Packet implements Serializable {
	public NetError netError;
	public byte[] imageBuffer; // 이미지 정보
	public boolean canRentBook; // 대여 가능 정보
	public int rentMemberUID; // 대여한 회원의 UID
	public int rentNo; // 아무도 대여하지않았다면 0
}
