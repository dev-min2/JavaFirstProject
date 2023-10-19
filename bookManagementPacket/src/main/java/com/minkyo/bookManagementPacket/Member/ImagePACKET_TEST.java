package com.minkyo.bookManagementPacket.Member;

import java.awt.Image;
import java.io.Serializable;

import CoreAcitive.RequestMapping;
import PacketUtils.Packet;

@RequestMapping("ImagePACKET_TEST")
public class ImagePACKET_TEST extends Packet implements Serializable {
	public byte[] imageBuffer;
}
