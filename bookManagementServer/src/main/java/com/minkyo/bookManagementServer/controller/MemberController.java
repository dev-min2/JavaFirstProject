package com.minkyo.bookManagementServer.controller;

import com.minkyo.bookManagementServer.service.MemberService;

import CoreAcitive.Controller;
import CoreAcitive.MessageInfo;
import PacketUtils.Packet;

@Controller
public class MemberController {
	
	private MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	public Packet createUser(Packet requestPacket, MessageInfo msgInfo) {
		return new Packet();
	}
	
}
