package com.minkyo.bookManagementClient.bookService;

import javax.swing.JPanel;

import com.minkyo.bookManagementClient.bookMain.BookPanelType;

public class BookListPanel extends JPanel {
	private BookPanelType pnType;
	public BookListPanel(BookPanelType pnType) {
		this.pnType = pnType;
	}
}
