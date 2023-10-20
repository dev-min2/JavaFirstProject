package com.minkyo.bookManagementServer.dao;

import com.minkyo.bookManagementPacket.BookList.BookVO;

public interface BookDAO {
	public boolean insertBook(BookVO vo);
}
