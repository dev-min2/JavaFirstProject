package com.minkyo.bookManagementServer.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.Member.DUPLICATE_ID_CHECK_ACK;
import com.minkyo.bookManagementPacket.Member.ImagePACKET_TEST;
import com.minkyo.bookManagementServer.service.BookService;
import com.minkyo.bookManagementServer.service.BookServiceImpl;

import CommonUtils.Utils;
import CoreAcitive.Controller;
import CoreAcitive.MessageInfo;
import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@Controller
public class BookController {
	private BookService bookService;
	
	public BookController(BookServiceImpl service) {
		bookService = service;
	}
	
	@RequestMapping("ADMIN_REGIST_BOOK_REQ")
	public Packet registBookReqByAdmin(Packet requestPacket, MessageInfo msgInfo) {
		ADMIN_REGIST_BOOK_REQ reqPacket = (ADMIN_REGIST_BOOK_REQ)requestPacket;
		ADMIN_REGIST_BOOK_ACK ackPacket = new ADMIN_REGIST_BOOK_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		if(bookService.registBook(reqPacket)) {
			ackPacket.netError = NetError.NET_OK;
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
