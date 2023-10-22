package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("BOOK_HISTORY_RENT_REQ")
public class BOOK_HISTORY_RENT_REQ extends Packet implements Serializable {
	public int bookNo;
}
