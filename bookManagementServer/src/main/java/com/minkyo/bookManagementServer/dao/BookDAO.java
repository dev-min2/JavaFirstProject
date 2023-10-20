package com.minkyo.bookManagementServer.dao;

import java.util.List;

import com.minkyo.bookManagementPacket.BookList.BookVO;

public interface BookDAO {
	public boolean insertBook(BookVO vo);
	public List<BookVO> selectAllBook(); 
	public BookVO selectOneBook(int bookNo, String bookTitle);
}
