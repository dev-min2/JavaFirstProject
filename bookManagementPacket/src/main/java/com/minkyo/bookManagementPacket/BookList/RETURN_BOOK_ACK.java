package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("8")
public class RETURN_BOOK_ACK extends Packet implements Serializable {
	public NetError netError;
}
