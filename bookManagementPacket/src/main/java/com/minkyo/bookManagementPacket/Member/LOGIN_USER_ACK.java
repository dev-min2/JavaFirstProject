package com.minkyo.bookManagementPacket.Member;

import java.io.Serializable;

import com.minkyo.bookManagementPacket.NetError;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("2")
public class LOGIN_USER_ACK extends Packet implements Serializable {
	public NetError netError;
	public MemberVO vo;
	
	// 기타 필요한 데이터
}
