package PacketUtils;

import java.util.AbstractMap;

public class Packet {
	public static final int PACKET_MIN_LEN = 4; // 패킷헤더
	public static final int PACKET_CHECK = 2;
	
	protected short packetSize = 0;
	protected short protocol = 0;
	
	public void setPacketInfo( short packetSize, short protocol )
	{
		this.packetSize = packetSize;
		this.protocol = protocol;
	}
	
	public boolean isValidPacket()
	{
		boolean ret = false;
		if(packetSize >= PACKET_MIN_LEN)
			ret = true;
		
		return ret;
	}

	public AbstractMap.SimpleEntry<Short, Short> getPacketInfo()
	{
		return new AbstractMap.SimpleEntry<Short, Short>(packetSize,protocol);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Packet cloneObj = new Packet();
		cloneObj.packetSize = this.packetSize;
		cloneObj.protocol = this.protocol;
		
		return cloneObj;
	}
	
}
