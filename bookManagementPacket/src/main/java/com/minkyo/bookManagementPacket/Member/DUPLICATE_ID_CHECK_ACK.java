package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("3")
public class DUPLICATE_ID_CHECK_ACK extends Packet implements Serializable {
	public NetError netError;
	public boolean isDuplicateID; 
}
