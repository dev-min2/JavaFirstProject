package com.minkyo.bookManagementClient.bookService;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookMain.BookManagementMainFrame;
import com.minkyo.bookManagementClient.bookMain.BookPanelType;

public class BookRequestBoardPanel extends EventPanel {
	private BookPanelType pnType;
	
	public BookRequestBoardPanel(BookPanelType pnType) {
		this.pnType = pnType;
		
		this.setSize(new Dimension(BookManagementMainFrame.SCREEN_WIDTH,BookManagementMainFrame.SCREEN_HEIGHT));
		this.setLayout(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BookManagementMainFrame.colorBackgroundImage,0,0, null);
	}

	@Override
	public void openEvent() {
		// TODO Auto-generated method stub
		
	}
}
