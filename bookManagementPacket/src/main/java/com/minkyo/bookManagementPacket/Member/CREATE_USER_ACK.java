package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;
import com.minkyo.bookManagementPacket.NetError;
import PacketUtils.Packet;

public class CREATE_USER_ACK extends Packet implements Serializable {
	public NetError error;
}

