package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("ADMIN_REGIST_BOOK_REQ")
public class ADMIN_REGIST_BOOK_REQ extends Packet implements Serializable {
	public String bookTitle;
	public String bookAuthor;
	public String bookPublisher;
	public byte[] imageBuffer;
}
