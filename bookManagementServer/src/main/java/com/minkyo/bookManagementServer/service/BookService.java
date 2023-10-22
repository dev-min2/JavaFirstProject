package com.minkyo.bookManagementServer.service;

import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_ACK;
import com.minkyo.bookManagementPacket.BookList.ADDITIONAL_BOOK_INFO_REQ;
import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BOOK_HISTORY_RENT_ACK;
import com.minkyo.bookManagementPacket.BookList.BOOK_HISTORY_RENT_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.MY_BOOK_HISTORY_RENT_ACK;
import com.minkyo.bookManagementPacket.BookList.MY_BOOK_HISTORY_RENT_REQ;
import com.minkyo.bookManagementPacket.BookList.RENT_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.RETURN_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;

public interface BookService {
	public boolean registBook(ADMIN_REGIST_BOOK_REQ packet);
	public List<BookVO> selectAllBook(SELECT_ALL_BOOK_DATA_REQ packet);
	public boolean getAdditionalBookInfo(ADDITIONAL_BOOK_INFO_REQ packet, ADDITIONAL_BOOK_INFO_ACK ackPacket);
	public NetError rentBook(RENT_BOOK_REQ packet);
	public boolean returnBook(RETURN_BOOK_REQ packet);
	public boolean getMyRentAndBookHistory(MY_BOOK_HISTORY_RENT_REQ packet, MY_BOOK_HISTORY_RENT_ACK ackPacket);
	public boolean getBookRentAndHistory(BOOK_HISTORY_RENT_REQ packet, BOOK_HISTORY_RENT_ACK ackPacket);
}
