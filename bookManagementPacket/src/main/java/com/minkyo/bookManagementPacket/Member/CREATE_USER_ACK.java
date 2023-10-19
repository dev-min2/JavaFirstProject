package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("1")
public class CREATE_USER_ACK extends Packet implements Serializable {
	private static final long serialVersionUID = 1L;
	public NetError error;
}

