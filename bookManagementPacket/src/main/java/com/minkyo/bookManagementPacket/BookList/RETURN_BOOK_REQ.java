package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("RETURN_BOOK_REQ")
public class RETURN_BOOK_REQ extends Packet implements Serializable {
	public int rentNo;
}
