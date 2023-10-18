package com.minkyo.bookManagementClient.bookService;

import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookMain.BookPanelType;

/*
 로그인 후 보게되는 첫 패널.
 
 // 개인정보?
 // 현재 대여한 것 책 제목, 책대여날짜, 반납날짜.
 // footer에는 요청게시판과 조회게시판
 */

public class BookMainPanel extends JPanel {
	private BookPanelType pnType;
	
	public BookMainPanel(BookPanelType pnType) {
		this.pnType = pnType;
	}
}
