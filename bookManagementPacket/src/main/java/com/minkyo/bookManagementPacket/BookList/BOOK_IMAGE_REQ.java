package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("BOOK_IMAGE_REQ")
public class BOOK_IMAGE_REQ extends Packet implements Serializable {
	public int bookNo;
	public String bookTitle;
}
