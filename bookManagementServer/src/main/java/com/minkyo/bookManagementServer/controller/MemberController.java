package com.minkyo.bookManagementServer.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_ACK;
import com.minkyo.bookManagementPacket.Member.CREATE_USER_REQ;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_ACK;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_REQ;
import com.minkyo.bookManagementPacket.Member.ImagePACKET_TEST;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_ACK;
import com.minkyo.bookManagementPacket.Member.LOGIN_USER_REQ;
import com.minkyo.bookManagementPacket.Member.MemberVO;
import com.minkyo.bookManagementServer.service.MemberService;
import com.minkyo.bookManagementServer.service.MemberServiceImpl;

import CommonUtils.Utils;
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
	
	@RequestMapping("ImagePACKET_TEST")
	public Packet test(Packet requestPacket, MessageInfo msgInfo) throws Exception {
		ImagePACKET_TEST reqPacket = (ImagePACKET_TEST)requestPacket;
		DUPLICATE_ID_CHECK_ACK ackPacket = new DUPLICATE_ID_CHECK_ACK();
		
		byte[] decompressedData = Utils.decompress(reqPacket.imageBuffer, false);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(decompressedData));
		
		// 이미지 읽고, 저장은 잘된다.
		// 이미지 파일은 책 리스트 조회떄에 우선 이미지를 제외한 리스트만 받아감.
		// 해당 책을 클릭했을 때 이미지 파일을 전송. 
		// (전송시점에 클라이언트의 로컬환경에 이미지 파일을 저장해 다시 송신하지 않게끔처리 -> 클라이언트는 매번 책 클릭할때마다 해당 로컬에 데이터가 있는지 확인.)
		File outputfile = new File("C:\\imgFolder\\saved.png");
	    ImageIO.write(image, "jpg", outputfile);

		return ackPacket;
	}
	
}
