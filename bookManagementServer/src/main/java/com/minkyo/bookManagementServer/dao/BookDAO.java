package com.minkyo.bookManagementServer.dao;

import java.sql.Date;
import java.util.AbstractMap;
import java.util.List;

import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

public interface BookDAO {
	public boolean insertBook(BookVO vo);
	public List<BookVO> selectAllBook(); 
	public AbstractMap.SimpleEntry<BookVO, RentBookVO> selectOneBook(int bookNo, String bookTitle);
	public boolean insertRentBook(int memberUID, int bookNo, Date rentDate);
	public boolean existRentBook(int bookNo);
	public boolean deleteRentBook(int rentNo);
}
