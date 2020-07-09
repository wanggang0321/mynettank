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
		
		//计算一下TankJoinMsg消息有多长，用于TankJoinMsgDecoder中解决TCP拆包、粘包的问题
		//int x, y;			两个int，4 Byte * 2 = 8 Byte
		//Dir dir;			传递的是enum下标值，也是int，一共 4 Byte
		//boolean moving;	1 Byte
		//Group group;		传递的是enum下标值，也是int，一共 4 Byte
		//UUID id;			UUID是128位，占 16 Byte
		// 8 + 4 + 1 + 4 + 16 =33
		
		//TankJoinMsgDecoder比较麻烦了，因为主要是TCP拆包、粘包的处理
		//TCP拆包、粘包的问题怎么处理呢？你需要知道这个消息发过来到底有多长
		if(in.readableBytes() < 33) return; //解决了TCP拆包、粘包的问题
		
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
