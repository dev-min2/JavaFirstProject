package com.minkyo.bookManagementServer.service;

import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;

public interface BookService {
	public boolean registBook(ADMIN_REGIST_BOOK_REQ packet);
}
