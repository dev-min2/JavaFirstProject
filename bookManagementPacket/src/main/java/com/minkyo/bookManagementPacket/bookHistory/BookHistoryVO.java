package com.minkyo.bookManagementPacket.bookHistory;

import java.io.Serializable;
import java.sql.Date;

public class BookHistoryVO implements Serializable {
	public int bookNo;
	public int userUID;
	public Date rentDate;
	public Date returnDate;
}
