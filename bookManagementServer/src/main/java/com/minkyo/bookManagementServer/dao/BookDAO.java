package com.minkyo.bookManagementServer.dao;

import java.sql.Date;
import java.util.AbstractMap;
import java.util.List;

import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

import CommonUtils.TripleTuple;

public interface BookDAO {
	public boolean insertBook(BookVO vo);
	public List<BookVO> selectAllBook(); 
	public AbstractMap.SimpleEntry<BookVO, RentBookVO> selectOneBook(int bookNo, String bookTitle);
	public boolean insertRentBook(int memberUID, int bookNo, Date rentDate);
	public boolean existRentBook(int bookNo);
	public boolean deleteRentBook(int rentNo);
	
	public TripleTuple<String,List<String>,List<BookHistoryVO>> selectMemberBookHistory(int memberUID);
	public TripleTuple<List<BookHistoryVO>, List<BookVO>, List<String>> selectBookRentAndHistory(int bookNo);
}
