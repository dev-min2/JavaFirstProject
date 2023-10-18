package com.minkyo.bookManagementClient.bookService;

import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookMain.BookPanelType;

public class BookRequestBoardPanel extends JPanel {
	private BookPanelType pnType;
	
	public BookRequestBoardPanel(BookPanelType pnType) {
		this.pnType = pnType;
	}
}
