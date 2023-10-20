package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("SELECT_BOOK_DATA_REQ")
public class SELECT_BOOK_DATA_REQ extends Packet implements Serializable {
	public int bookNo;
}
