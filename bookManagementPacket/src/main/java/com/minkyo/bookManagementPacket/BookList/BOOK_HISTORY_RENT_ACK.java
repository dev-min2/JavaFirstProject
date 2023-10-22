package com.minkyo.bookManagementPacket.BookList;

import java.io.Serializable;
import java.util.List;

import com.minkyo.bookManagementPacket.NetError;
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;

import CoreAcitive.AckInfo;
import PacketUtils.Packet;

@AckInfo("10")
public class BOOK_HISTORY_RENT_ACK extends Packet implements Serializable {
	public NetError netError;
	public List<BookHistoryVO> historyVOList;
	public List<BookVO> bookVOList;
	public List<String> memberNicknameList;  // 이름정보만 필요해서 이름만들고온다.
}
