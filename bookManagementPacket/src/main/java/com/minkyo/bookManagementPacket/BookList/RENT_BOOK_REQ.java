package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("RENT_BOOK_REQ")
public class RENT_BOOK_REQ extends Packet implements Serializable {
	public int bookNo;
	public int memberUID;
}
