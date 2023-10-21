package com.minkyo.bookManagementServer.controller;

import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_ACK;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_REQ;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.RENT_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.RENT_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.RETURN_BOOK_ACK;
import com.minkyo.bookManagementPacket.BookList.RETURN_BOOK_REQ;
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
	public Packet requestAllBook(Packet requestPacket, MessageInfo msgInfo) {
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
	
	@RequestMapping("ADDITIONAL_BOOK_INFO_REQ")
	public Packet requestOneBookInfo(Packet requestPacket, MessageInfo msgInfo) {
		ADDITIONAL_BOOK_INFO_REQ reqPacket = (ADDITIONAL_BOOK_INFO_REQ)requestPacket;
		ADDITIONAL_BOOK_INFO_ACK ackPacket = new ADDITIONAL_BOOK_INFO_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		if(bookService.getAdditionalBookInfo(reqPacket,ackPacket)) {
			ackPacket.netError = NetError.NET_OK;
		}
		
		return ackPacket;
	}
	
	@RequestMapping("RENT_BOOK_REQ")
	public Packet requestRentBook(Packet requestPacket, MessageInfo msgInfo) {
		RENT_BOOK_REQ reqPacket = (RENT_BOOK_REQ)requestPacket;
		RENT_BOOK_ACK ackPacket = new RENT_BOOK_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		ackPacket.netError = bookService.rentBook(reqPacket);
		return ackPacket;
	}
	
	@RequestMapping("RETURN_BOOK_REQ")
	public Packet requestReturnBook(Packet requestPacket, MessageInfo msgInfo) {
		RETURN_BOOK_REQ reqPacket = (RETURN_BOOK_REQ)requestPacket;
		RETURN_BOOK_ACK ackPacket = new RETURN_BOOK_ACK();
		ackPacket.netError = NetError.NET_FAIL;
		
		if(msgInfo.getParameter("login") == null)
			return ackPacket;
		
		if(bookService.returnBook(reqPacket)) {
			ackPacket.netError = NetError.NET_OK;
		}
		return ackPacket;
	}
}
