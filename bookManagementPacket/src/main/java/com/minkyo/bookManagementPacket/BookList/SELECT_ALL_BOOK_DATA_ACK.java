package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;
import java.util.ArrayList;

import com.minkyo.bookManagementPacket.NetError;

import PacketUtils.Packet;

// 우선 가지고있는 데이터 전부를 보내준다.
public class SELECT_ALL_BOOK_DATA_ACK extends Packet implements Serializable {
	public NetError netError;
	public ArrayList<BookVO> bookList;
}