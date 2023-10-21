package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("ADDITIONAL_BOOK_INFO_REQ")
public class ADDITIONAL_BOOK_INFO_REQ extends Packet implements Serializable {
	public int bookNo;
	public String bookTitle;
	public boolean existImageFile;// 이미 Local에 이미지를 가지고 있을 때 true
}
