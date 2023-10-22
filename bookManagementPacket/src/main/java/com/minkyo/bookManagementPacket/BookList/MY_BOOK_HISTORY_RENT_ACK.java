package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("9")
public class MY_BOOK_HISTORY_RENT_ACK extends Packet implements Serializable {
	public NetError netError;
	public List<String> bookTitleList;
	public List<BookHistoryVO> historyVOList; 
	public String nickName;
}
