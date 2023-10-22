package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("MY_BOOK_HISTORY_RENT_REQ")
public class MY_BOOK_HISTORY_RENT_REQ extends Packet implements Serializable {
	public int memberUID;
}
