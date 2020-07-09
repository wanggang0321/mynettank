package com.mashibing.tank.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.Tank;

//用于网络传输的消息
public class TankJoinMsg {
	
	public int x, y;
	public Dir dir;
	public boolean moving;
	public Group group;
	public UUID id;
	
	//计算一下TankJoinMsg消息有多长，用于TankJoinMsgDecoder中解决TCP拆包、粘包的问题
	//int x, y;			两个int，4 Byte * 2 = 8 Byte
	//Dir dir;			传递的是enum下标值，也是int，一共 4 Byte
	//boolean moving;	1 Byte
	//Group group;		传递的是enum下标值，也是int，一共 4 Byte
	//UUID id;			UUID是128位，占 16 Byte
	
	public TankJoinMsg() {
		
	}
	
//	public TankJoinMsg(Tank t) {
//		this.x = t.getX();
//		this.y = t.getY();
//		this.dir = t.getDir();
//		this.group = t.getGroup();
//		this.id = t.getId();
//		this.moving = t.isMoving();
//	}
	
	public TankJoinMsg(int x, int y, Dir dir, boolean moving, Group group, UUID id) {
		super();
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.group = group;
		this.id = id;
		this.moving = moving;
	}
	
	public void parse(byte[] bytes) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			//TODO: 先读TYPE信息，根据TYPE信息处理不同的信息
			//略过消息类型
			
			this.x = dis.readInt();
			this.y = dis.readInt();
			this.dir = Dir.values()[dis.readInt()];
			this.moving = dis.readBoolean();
			this.group = Group.values()[dis.readInt()];
			this.id = new UUID(dis.readLong(), dis.readLong());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//将整个消息转换成字节数组
	public byte[] toBytes() {
		ByteArrayOutputStream baos = null;
		//只能写基础数据类型
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			//相当于在内存里分配了一个字节数组，然后一个管道怼上去，准备往里面写内容
			baos = new ByteArrayOutputStream();
			//ByteArrayOutputStream 往外写不方便，DataOutputStream 比较好用
			dos = new DataOutputStream(baos);
			dos.writeInt(x);
			dos.writeInt(y);
			//ordinal 把枚举类型看成数组，它的元素的下标值
			dos.writeInt(dir.ordinal());
			//boolean往外写占一个字节
			dos.writeBoolean(moving);
			dos.writeInt(group.ordinal());
			//DataOutputStream只能写基础数据类型，所以UUID要拆分开写入网络
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			//*** 在网络的这种协议里面，没有人用字符串传消息 ***
			//因为字符串占字节比较大
			//一般情况下，网络上，能不传字符串就不传字符串
			//参考Netty官网中Fatorial的例子
			//95%的自定义协议里面，你都要写好你的消息头，写好你的消息有多长
			//dos.writeUTF(name);
			dos.flush();
			bytes = baos.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(baos!=null) {
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(dos!=null) {
					dos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName())
			.append("[")
			.append("uuid=" + id + " | ")
			.append("x=" + x +  " | ")
			.append("y=" + y +  " | ")
			.append("moving=" + moving +  " | ")
			.append("dir=" + dir +  " | ")
			.append("group=" + group +  " | ")
			.append("]");
		return builder.toString();
	}
	
}
