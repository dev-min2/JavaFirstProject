package com.minkyo.bookManagementServer.controller;

import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_ACK;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_ACK;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;
import com.minkyo.bookManagementServer.service.BookService;
import com.minkyo.bookManagementServer.service.BookServiceImpl;

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
	public Packet registBookRequestByAdmin(Packet requestPacket, MessageInfo msgInfo) {
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
	
	@RequestMapping("SELECT_ALL_BOOK_DATA_REQ")
	public Packet selectAllBook(Packet requestPacket, MessageInfo msgInfo) {
		SELECT_ALL_BOOK_DATA_REQ reqPacket = (SELECT_ALL_BOOK_DATA_REQ)requestPacket;
		SELECT_ALL_BOOK_DATA_ACK ackPacket = new SELECT_ALL_BOOK_DATA_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		List<BookVO> books = bookService.selectAllBook(reqPacket);
		if(books != null) {
			ackPacket.netError = NetError.NET_OK;
			ackPacket.bookList = books;
		}
		
		return ackPacket;
	}
	
	@RequestMapping("BOOK_IMAGE_REQ")
	public Packet requestBookImage(Packet requestPacket, MessageInfo msgInfo) {
		BOOK_IMAGE_REQ reqPacket = (BOOK_IMAGE_REQ)requestPacket;
		BOOK_IMAGE_ACK ackPacket = new BOOK_IMAGE_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		byte[] imageBuffer = bookService.getImageBuffer(reqPacket);
		if(imageBuffer != null) {
			ackPacket.netError = NetError.NET_OK;
			ackPacket.imageBuffer = imageBuffer;
		}
		
		return ackPacket;
	}
}
