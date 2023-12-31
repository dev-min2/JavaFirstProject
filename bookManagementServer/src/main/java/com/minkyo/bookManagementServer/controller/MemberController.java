package com.minkyo.bookManagementServer.controller;


import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_ACK;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_ACK;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_REQ;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_ACK;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;
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
		ackPacket.error = NetError.NET_FAIL;
		
		if(memberService.createUser(reqPacket)) {
			ackPacket.error = NetError.NET_OK;
		}
		
		return ackPacket;
	}
	
	@RequestMapping("LOGIN_USER_REQ")
	public Packet loginUser(Packet requestPacket, MessageInfo msgInfo) {
		LOGIN_USER_REQ reqPacket = (LOGIN_USER_REQ)requestPacket;
		LOGIN_USER_ACK ackPacket = new LOGIN_USER_ACK();
		
		MemberVO loginVO = memberService.loginUser(reqPacket);
		if(loginVO != null && loginVO.getMemberID().equals(reqPacket.userID)) {
			ackPacket.vo = loginVO;
			
			//세션에 저장.
			msgInfo.setParameter("login", String.valueOf(loginVO.getMemberUID()));
			ackPacket.netError = NetError.NET_OK;
		}
		else {
			ackPacket.netError = NetError.NET_FAIL;
		}
		
		// 클라에서 패킷 받는것 테스트위해서.(잘됨)
//		ackPacket.vo.setMemberEmail("asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+asdgkasdgpoasdkgpsdgksdpg++++"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				+ "asdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpgasdgkasdgpoasdkgpsdgksdpg+"
//				);
		
		return ackPacket;
	}
	
	@RequestMapping("DUPLICATE_ID_CHECK_REQ")
	public Packet checkDuplicateID(Packet requestPacket, MessageInfo msgInfo) {
		DUPLICATE_ID_CHECK_REQ reqPacket = (DUPLICATE_ID_CHECK_REQ)requestPacket;
		DUPLICATE_ID_CHECK_ACK ackPacket = new DUPLICATE_ID_CHECK_ACK();
		
		ackPacket.netError = NetError.NET_FAIL;
		
		if(!memberService.checkDuplicateID(reqPacket)) {
			ackPacket.netError = NetError.NET_OK;
			ackPacket.isDuplicateID = false;
		}
		else {
			ackPacket.isDuplicateID = true;
		}
		
		return ackPacket;
	}
	
}
