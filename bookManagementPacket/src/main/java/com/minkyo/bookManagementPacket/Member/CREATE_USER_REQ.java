package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("CREATE_USER_REQ")
public class CREATE_USER_REQ extends Packet implements Serializable {
	public String userID;
	public String userPassword;
	public String userEmail;
	public String nickName;
}