package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import PacketUtils.Packet;

public class SELECT_BOOK_DATA_ACK extends Packet implements Serializable {
	public NetError netError;
	public BookVO bookVO;
}
