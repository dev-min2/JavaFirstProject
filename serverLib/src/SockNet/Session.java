package SockNet;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.Queue;

import CoreAcitive.DispatcherBot;
import PacketUtils.Packet;
import PacketUtils.PacketUtil;

public class Session {
	private int sessionId = 0;
	private AsynchronousSocketChannel socketChannel = null;
	private RingBuffer recvBuffer = new RingBuffer();
	private AcceptCompletionHandler handler = null; // closeSession 호출용도로만 쓴다.
	
	// 데이터 덩치가 커서 한번에 합치기 혹은 파싱하기 어려운 경우 해당 List에 넣어준다. (아직 미완)
	private LinkedList<byte[]> assemblePacketList = new LinkedList<byte[]>();
	
	public void Init(AsynchronousSocketChannel socketChannel, int sessionId, AcceptCompletionHandler handler) {
		this.socketChannel = socketChannel;
		this.sessionId = sessionId;		
		this.handler = handler;
		registerReceive(); // 초기화하자마자 receive 걸어두기
	}
	
	// 연결된 Session 비동기 Recv요청.
	// 요청 -> 데이터 수신&처리 -> 재요청
	public void registerReceive() {
		ByteBuffer byteBuffer = recvBuffer.getBuffer();
		socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>(){
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				if(result < 0) {
					handler.closeSession(sessionId);
					return;
				}
				
				try {
					int readSize = 0; // 실제 읽은 총량(all amount)사이즈
					int recvLen = 0; // 남은 패킷 길이.
					attachment.flip();
					
					// 이전에 남은 데이터가 있다면
					int remainLen = recvBuffer.getRemainLen(); // 이전에 남은 데이터가 있는지 체크
					int pos = recvBuffer.getPosition() - remainLen; // 현재 읽어야할 위치.
					byte[] buffer = attachment.array(); // 원본 버퍼.
					while(true) {
						// 수신한 버퍼 데이터의 크기.(이미 읽은건 뺴준다)
						recvLen = attachment.limit() - readSize;
						
						// 더이상 읽을게 없다면
						if(recvLen <= 0)
							break;

						// 최소 4바이트는 왔는지.
						if((remainLen + recvLen) < Packet.PACKET_MIN_LEN)
							break;

						int packetLenIndex = pos + Packet.PACKET_CHECK; 
						short packetLen = 0;
						
						packetLen |= (((short) buffer[packetLenIndex]) << 8) & 0xFF00;
						packetLen |= (((short) buffer[packetLenIndex + 1])) & 0xFF;
						// 패킷길이보다 작다면.
						if((recvLen + remainLen) < packetLen)
							break;
						
						// 여기까지 오면 정상적인 하나의 패킷을 만들 수 있음.
						byte[] packetBuffer = new byte[packetLen];
						recvBuffer.readBuffer(packetLen - remainLen); 						
						System.arraycopy(buffer, pos, packetBuffer, 0, packetLen);
						
						Packet packet = PacketUtil.convertPacketFromBytes(packetBuffer);
						// 실제 패킷처리.
						if(packet != null) {
							Packet ackPacket = DispatcherBot.getDispatcherBot().dispatch(packet, getSessionId());
							if(ackPacket != null) {
								PacketUtil.addInfoPacket(ackPacket);
								send(ackPacket);
							}
						}
												
						readSize += packetLen;
						pos += packetLen;
						
						// remainLen > 0이었고, 여기까지왔다면 해당 패킷은 처리되었다는것이므로 0으로 수정
						if(remainLen > 0)
							remainLen = 0;
					}
					if(recvLen <= 0)
						recvBuffer.clean(); // 버퍼 초기화
					else {
						recvBuffer.setPosition(readSize + recvLen); // Position 이동해줘야 해당 Position뒤로 데이터가 들어온다.  
						recvBuffer.setRemainLen(recvLen); // 처리되지 못하고, 남은 데이터
					}
					
					ByteBuffer byteBuffer2 = recvBuffer.getBuffer();
					socketChannel.read(byteBuffer2, byteBuffer2, this); //데이터 다시 읽기 요청
				}
				catch(Exception e) {
					e.printStackTrace();
					handler.closeSession(sessionId);
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				handler.closeSession(sessionId);
			}
		});
	}
	
	public int getSessionId() {
		return sessionId;
	}

	// send는 언제든 호출될 수 있음.
	// Dispatch sendThread1 -> A Session Pop ~~>패킷처리
	// Dispatch sendThread2 -> A Session Pop ~~>패킷처리 
	// 위와 같은 경우때문에 lock을 걸어야하나.. 별도 Session이 들고있는 sendBuffer에 send한 내용물을 따로 저장하지않는다.
	// 즉 send가 실패한경우를 제외한 덜 송신된 경우는 우선 배제.
	public void send(Packet packet) {
		byte[] packetArray = null;
		try {
			packetArray = PacketUtil.convertBytesFromPacket(packet);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(packetArray.length);
		buffer.put(packetArray);
		buffer.flip(); // 해줘야 송신됨.
		
		socketChannel.write(buffer,buffer,new CompletionHandler<Integer, ByteBuffer>(){
			@Override
			public void completed(Integer result, ByteBuffer attachment) {
				try {
					if(result == -1) {
						handler.closeSession(sessionId);
						return;
					}
					
					int sendLen = attachment.limit();
					System.out.println("송신 size : " + sendLen);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				handler.closeSession(sessionId);
			}
		});
	}
	
	public void close() {
		try {
			DispatcherBot.getDispatcherBot().closeSession(sessionId);
			socketChannel.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public AsynchronousSocketChannel getChannel() {
		return socketChannel;
	}
}
