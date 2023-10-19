package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("LOGIN_USER_REQ")
public class LOGIN_USER_REQ extends Packet implements Serializable {
	public String userID;
	public String userPassword;
}
