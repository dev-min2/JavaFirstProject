package PacketUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import CoreAcitive.AckInfo;
import CoreAcitive.Controller;

public class PacketUtil
{
	private PacketUtil() {}
	
	public static void addInfoPacket(Packet packet) throws IOException {
		int ackProtocol = Integer.parseInt(packet.getClass().getAnnotation(AckInfo.class).value());
		byte[] objectArray = Serialize(packet);
		
		final short PACKET_SIZE = (short)(Packet.PACKET_MIN_LEN + objectArray.length);
		
		packet.setPacketInfo(PACKET_SIZE, (short)ackProtocol);
	}
	
	public static byte[] genPacketBuffer(int protocols, Object object) throws Exception
	{
		short protocol = (short)protocols;
		
		if(protocol < 0 || object == null)
			throw new Exception("protocol < 0 || object == null");
		
		byte[] protocolArray = ByteBuffer.allocate(Short.BYTES).putShort(protocol).array();
		byte[] objectArray = Serialize(object);
		if(objectArray == null)
			throw new Exception("Object Array null");
		final short PACKET_SIZE = (short)(Packet.PACKET_MIN_LEN + objectArray.length);
		
		ByteBuffer buffer = ByteBuffer.allocate(2);
		byte[] packetLenBuffer = buffer.putShort(PACKET_SIZE).array();

		byte[] ret = new byte[PACKET_SIZE];
		System.arraycopy(protocolArray, 0, ret, 0, protocolArray.length);
		System.arraycopy(packetLenBuffer, 0, ret, Packet.PACKET_CHECK, packetLenBuffer.length);
		System.arraycopy(objectArray, 0, ret, Packet.PACKET_MIN_LEN, objectArray.length);
		
		return ret;
	}
	
	public static byte[] convertBytesFromPacket(Packet packet) throws Exception
	{
		if(packet == null || !packet.isValidPacket())
			return null;
		
		return genPacketBuffer(packet.getPacketInfo().getValue(),packet);
	}
	
	
	public static Packet convertPacketFromBytes(byte[] bytes) throws ClassNotFoundException, IOException
	{
		short packetSize = isValidPacket(bytes);
		if(packetSize < 0)
			return null;
		
		return GeneratePacket(bytes, packetSize);
	}
	
	
	public static byte[] Serialize(Object object) throws IOException {
		if(object == null)
			return null;
		
		if(!(object instanceof Packet))
			return null;
		
	    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream out = new ObjectOutputStream(bos)) {
	        out.writeObject(object);
	        return bos.toByteArray();
	    } 
	}
	
	public static Object Deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		if(bytes == null)
			return null;
		
	    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	        ObjectInputStream in = new ObjectInputStream(bis)) {
	        return in.readObject();
	    } 
	}
	
	private static Packet GeneratePacket(byte[] bytes, short packetSize) throws ClassNotFoundException, IOException
	{
		byte[] protocolBuffer = new byte[Packet.PACKET_CHECK];
		System.arraycopy(bytes, 0, protocolBuffer, 0, Packet.PACKET_CHECK);
		short protocol = ByteBuffer.wrap(protocolBuffer).getShort();
		
		int objectLen = packetSize - Packet.PACKET_MIN_LEN;
		byte[] objectBuffer = new byte[objectLen];
		System.arraycopy(bytes, Packet.PACKET_MIN_LEN, objectBuffer, 0, objectLen); 
		
		Packet packet = (Packet)Deserialize(objectBuffer);
		packet.setPacketInfo(packetSize,protocol);
		return packet;
	}
	
	private static short isValidPacket(byte[] bytes)
	{
		if(bytes == null)
			return -1;
		
		if(bytes.length < Packet.PACKET_MIN_LEN)
			return -1;
		
		byte[] packetLenBuffer = new byte[Packet.PACKET_CHECK];
		System.arraycopy(bytes, 2, packetLenBuffer, 0, Packet.PACKET_CHECK);
		short packetLen = ByteBuffer.wrap(packetLenBuffer).getShort();
		
		if(packetLen > bytes.length)
			return -1;
		
		return packetLen;
	}
}