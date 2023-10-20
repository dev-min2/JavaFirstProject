package com.minkyo.bookManagementServer.service;

import java.util.List;

import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;

public interface BookService {
	public boolean registBook(ADMIN_REGIST_BOOK_REQ packet);
	public List<BookVO> selectAllBook(SELECT_ALL_BOOK_DATA_REQ packet);
	public byte[] getImageBuffer(BOOK_IMAGE_REQ packet);
}
