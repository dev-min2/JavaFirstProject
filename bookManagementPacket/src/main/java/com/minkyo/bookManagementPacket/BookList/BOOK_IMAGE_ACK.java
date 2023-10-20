package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("6")
public class BOOK_IMAGE_ACK extends Packet implements Serializable {
	public NetError netError;
	public byte[] imageBuffer;
}
