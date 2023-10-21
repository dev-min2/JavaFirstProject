package com.minkyo.bookManagementClient.bookService;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;
import com.minkyo.bookManagementClient.bookMain.Util;

/*
 로그인 후 보게되는 첫 패널.
 
 // 개인정보?
 // 현재 대여한 것 책 제목, 책대여날짜, 반납날짜.
 // footer에는 요청게시판과 조회게시판
 */

public class BookMainPanel extends EventPanel {
	private BookPanelType pnType;
	private JButton requestBoardBtn = new RoundedButton("도서 요청 게시판");
	private JButton rentBoardBtn = new JButton("도서 조회 게시판");
	private JButton logoutBtn = new JButton("로그아웃");
	
	private JButton returnBookPrevBtn = new JButton("◀");
	private JButton returnBookNextBtn = new JButton("▶");
	
	private JButton rentBookPrevBtn = new JButton("◀");
	private JButton rentBookNextBtn = new JButton("▶");
	
	public BookMainPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
		
		requestBoardBtn.setBounds(160,70, 200, 250);
		rentBoardBtn.setBounds(400,70, 200, 250);
		logoutBtn.setBounds(880, 10, 100, 50);
		
		returnBookPrevBtn.setBounds(180,435,45,45);
		returnBookNextBtn.setBounds(800,435,45,45);
		
		rentBookPrevBtn.setBounds(180,595,45,45);
		rentBookNextBtn.setBounds(800,595,45,45);
		
		setButtonListener();
		
		add(requestBoardBtn);
		add(rentBoardBtn);
		add(logoutBtn);
		
		add(returnBookPrevBtn);
		add(returnBookNextBtn);
		
		add(rentBookPrevBtn);
		add(rentBookNextBtn);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
		drawReturnBookRect((Graphics2D)g); // 하단 반납 Rect
		drawRentBookRect((Graphics2D)g);
		drawProfileBookRect((Graphics2D)g);
	}
	
	private void drawReturnBookRect(Graphics2D g) {
		Stroke stroke1 = new BasicStroke(5f);
		g.setStroke(stroke1);
		
		g.drawRoundRect(160, 500, 700, 150, 30, 30);
	}
	
	private void drawRentBookRect(Graphics2D g) {
		g.drawRoundRect(160, 340, 700, 150, 30, 30);
	}
	
	private void drawProfileBookRect(Graphics2D g) {
		g.drawRoundRect(660, 70, 200, 250, 30, 30);
	}
	
	private void setButtonListener() {
		ActionListener request_board_action = (ActionEvent e) -> {
			// 로그인 가능한지 패킷 송신
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.BookRequestBoardPanel);
		};
		
		ActionListener rent_board_action = (ActionEvent e) -> {
			BookManagementMainFrame.getInstance().changePanel(BookPanelType.BookListPanel);
		};
		
		requestBoardBtn.addActionListener(request_board_action);
		rentBoardBtn.addActionListener(rent_board_action);
	}

	@Override
	public void openEvent() {
		// TODO Auto-generated method stub
		
	}

}
