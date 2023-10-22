package com.minkyo.bookManagementServer.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Date;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;

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
import com.minkyo.bookManagementPacket.bookHistory.BookHistoryVO;
import com.minkyo.bookManagementPacket.bookHistory.RentBookVO;
import com.minkyo.bookManagementServer.dao.BookDAO;
import com.minkyo.bookManagementServer.dao.BookDAOImpl;

import CommonUtils.TripleTuple;
import CommonUtils.Utils;

public class BookServiceImpl implements BookService {
	private BookDAO dao;
	private String localSaveImgPath;
	
	public BookServiceImpl(BookDAOImpl dao, String localSaveImgPath) {
		this.dao = dao;
		this.localSaveImgPath = localSaveImgPath;
	}
	
	@Override
	public boolean registBook(ADMIN_REGIST_BOOK_REQ packet) {
		// TODO Auto-generated method stub
		boolean ret = false;
		
		if(packet.bookTitle == null || packet.bookTitle.isBlank())
			return ret;
		
		if(packet.bookAuthor == null || packet.bookAuthor.isBlank())
			return ret;
		
		if(packet.bookPublisher == null || packet.bookPublisher.isBlank())
			return ret;
		
		try {
			//byte[] decompressedData = Utils.decompress(packet.imageBuffer, false);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(packet.imageBuffer));
		
			// 이미지 읽고, 저장은 잘된다.
			// 이미지 파일은 책 리스트 조회떄에 우선 이미지를 제외한 리스트만 받아감.
			// 해당 책을 클릭했을 때 이미지 파일을 전송. 
			// (전송시점에 클라이언트의 로컬환경에 이미지 파일을 저장해 다시 송신하지 않게끔처리 -> 클라이언트는 매번 책 클릭할때마다 해당 로컬에 데이터가 있는지 확인.)
			String savePath = localSaveImgPath + "\\" + packet.bookTitle + ".jpg";
			File outputfile = new File(savePath);
	    	
			if(!ImageIO.write(image, "jpg", outputfile))
				return ret;
			
			BookVO vo = new BookVO();
			vo.setBookTitle(packet.bookTitle);
			vo.setBookAuthor(packet.bookAuthor);
			vo.setBookPublisher(packet.bookPublisher);
			vo.setBookImgPath(savePath);
			
			ret = dao.insertBook(vo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ret;
		}
		
		return ret;
	}

	public List<BookVO> selectAllBook(SELECT_ALL_BOOK_DATA_REQ packet) {
		return dao.selectAllBook();
	}

	@Override
	public boolean getAdditionalBookInfo(ADDITIONAL_BOOK_INFO_REQ packet, ADDITIONAL_BOOK_INFO_ACK ackPacket) {
		if(packet.bookNo <= 0 || packet.bookTitle == null || packet.bookTitle.isEmpty())
			return false;
		
		AbstractMap.SimpleEntry<BookVO, RentBookVO> ret = dao.selectOneBook(packet.bookNo, packet.bookTitle);
		if(ret == null)
			return false;

		byte[] retImageBuffer = null;
		if(!packet.existImageFile) {
			String imagePath = ret.getKey().getBookImgPath();
			File imgFile = new File(imagePath);
			if(!imgFile.exists())
				return false;
	
			try {
				BufferedImage bfImg =ImageIO.read(imgFile);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(bfImg, "jpg", baos);
			
				retImageBuffer = baos.toByteArray();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if(ret.getValue() != null) {
			ackPacket.canRentBook = false;
			ackPacket.rentMemberUID = ret.getValue().getMemberUID();
			ackPacket.rentNo = ret.getValue().getRentNo();
		}
		else {
			ackPacket.canRentBook = true;
		}
		
		ackPacket.imageBuffer = retImageBuffer;
		return true;
	}
	
	@Override
	public NetError rentBook(RENT_BOOK_REQ packet) {
		if(packet.bookNo < 0 || packet.memberUID < 0)
			return NetError.NET_FAIL;
		
		// 이미 누군가가 Rent했다면
		if(dao.existRentBook(packet.bookNo)) {
			return NetError.NET_ALREADY_RENT_BOOK;
		}
		
		Date rentDate = new Date(System.currentTimeMillis());
		if(!dao.insertRentBook(packet.memberUID, packet.bookNo,rentDate)) {
			return NetError.NET_FAIL;
		}

		return NetError.NET_OK;
	}

	@Override
	public boolean returnBook(RETURN_BOOK_REQ packet) {
		// TODO Auto-generated method stub
	
		return dao.deleteRentBook(packet.rentNo);
	}

	@Override
	public boolean getMyRentAndBookHistory(MY_BOOK_HISTORY_RENT_REQ packet, MY_BOOK_HISTORY_RENT_ACK ackPacket) {
		// TODO Auto-generated method stub
		TripleTuple<String,List<String>,List<BookHistoryVO>> tupleVO = dao.selectMemberBookHistory(packet.memberUID);
		
		ackPacket.nickName = tupleVO.getFirstObj();
		ackPacket.bookTitleList = tupleVO.getSecondObj();
		ackPacket.historyVOList = tupleVO.getThirdObj();
		
		return true;
	}

	@Override
	public boolean getBookRentAndHistory(BOOK_HISTORY_RENT_REQ packet, BOOK_HISTORY_RENT_ACK ackPacket) {
		// TODO Auto-generated method stub
		
		TripleTuple<List<BookHistoryVO>, List<BookVO>, List<String>> result = dao.selectBookRentAndHistory(packet.bookNo);
		if(result == null)
			return false;
		
		ackPacket.historyVOList = result.getFirstObj();
		ackPacket.bookVOList = result.getSecondObj();
		ackPacket.memberNicknameList = result.getThirdObj();		
		return true;
	}
}
