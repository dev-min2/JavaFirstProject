package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("DUPLICATE_ID_CHECK_REQ")
public class DUPLICATE_ID_CHECK_REQ extends Packet implements Serializable {
	public String id;
}
