package com.minkyo.bookManagementServer.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.zip.Deflater;

import javax.imageio.ImageIO;

import com.minkyo.bookManagementPacket.BookList.ADMIN_REGIST_BOOK_REQ;
import com.minkyo.bookManagementPacket.BookList.BOOK_IMAGE_REQ;
import com.minkyo.bookManagementPacket.BookList.BookVO;
import com.minkyo.bookManagementPacket.BookList.SELECT_ALL_BOOK_DATA_REQ;
import com.minkyo.bookManagementServer.dao.BookDAO;
import com.minkyo.bookManagementServer.dao.BookDAOImpl;

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
			byte[] decompressedData = Utils.decompress(packet.imageBuffer, false);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(decompressedData));
		
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
			if(packet.bookIntroduce != null && !packet.bookIntroduce.isBlank())
				vo.setBookIntroduce(packet.bookIntroduce);
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
	public byte[] getImageBuffer(BOOK_IMAGE_REQ packet) {
		if(packet.bookNo <= 0 || packet.bookTitle == null || packet.bookTitle.isEmpty())
			return null;
		
		BookVO vo = dao.selectOneBook(packet.bookNo, packet.bookTitle);
		if(vo == null)
			return null;
		
		String imagePath = vo.getBookImgPath();
		File imgFile = new File(imagePath);
		if(!imgFile.exists())
			return null;
		
		byte[] retImageBuffer = null;
		try {
			BufferedImage bfImg =ImageIO.read(imgFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bfImg, "jpg", baos);
			
			retImageBuffer = Utils.compress(baos.toByteArray(), Deflater.BEST_COMPRESSION, false);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return retImageBuffer;
	}
}
