package com.mashibing.tank.net;

import java.util.List;
import java.util.UUID;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TankJoinMsgDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		//����һ��TankJoinMsg��Ϣ�ж೤������TankJoinMsgDecoder�н��TCP�����ճ��������
		//int x, y;			����int��4 Byte * 2 = 8 Byte
		//Dir dir;			���ݵ���enum�±�ֵ��Ҳ��int��һ�� 4 Byte
		//boolean moving;	1 Byte
		//Group group;		���ݵ���enum�±�ֵ��Ҳ��int��һ�� 4 Byte
		//UUID id;			UUID��128λ��ռ 16 Byte
		// 8 + 4 + 1 + 4 + 16 =33
		
		//TankJoinMsgDecoder�Ƚ��鷳�ˣ���Ϊ��Ҫ��TCP�����ճ���Ĵ���
		//TCP�����ճ����������ô�����أ�����Ҫ֪�������Ϣ�����������ж೤
		if(in.readableBytes() < 33) return; //�����TCP�����ճ��������
		
		TankJoinMsg msg = new TankJoinMsg();
		
		msg.x = in.readInt();
		msg.y = in.readInt();
		msg.dir = Dir.values()[in.readInt()];
		msg.moving = in.readBoolean();
		msg.group = Group.values()[in.readInt()];
		msg.id = new UUID(in.readLong(), in.readLong());
		
		out.add(msg);
	}
}
