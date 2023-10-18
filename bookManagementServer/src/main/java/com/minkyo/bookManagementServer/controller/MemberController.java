package com.minkyo.bookManagementServer.controller;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_ACK;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementServer.service.MemberService;
import com.minkyo.bookManagementServer.service.MemberServiceImpl;

import CoreAcitive.Controller;
import CoreAcitive.MessageInfo;
import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@Controller
public class MemberController {
	private MemberService memberService;
	
	public MemberController(MemberServiceImpl memberService) {
		this.memberService = memberService;
	}
	
	@RequestMapping("CREATE_USER_REQ")
	public Packet createUser(Packet requestPacket, MessageInfo msgInfo) {
		CREATE_USER_REQ reqPacket = (CREATE_USER_REQ)requestPacket;
		CREATE_USER_ACK ackPacket = new CREATE_USER_ACK();
		
		memberService.createUser(reqPacket);
		ackPacket.error = NetError.NET_OK;
		
		return ackPacket;
	}
	
}
