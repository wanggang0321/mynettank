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

//�������紫�����Ϣ
public class TankJoinMsg {
	
	public int x, y;
	public Dir dir;
	public boolean moving;
	public Group group;
	public UUID id;
	
	//����һ��TankJoinMsg��Ϣ�ж೤������TankJoinMsgDecoder�н��TCP�����ճ��������
	//int x, y;			����int��4 Byte * 2 = 8 Byte
	//Dir dir;			���ݵ���enum�±�ֵ��Ҳ��int��һ�� 4 Byte
	//boolean moving;	1 Byte
	//Group group;		���ݵ���enum�±�ֵ��Ҳ��int��һ�� 4 Byte
	//UUID id;			UUID��128λ��ռ 16 Byte
	
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
			//TODO: �ȶ�TYPE��Ϣ������TYPE��Ϣ����ͬ����Ϣ
			//�Թ���Ϣ����
			
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
	
	//��������Ϣת�����ֽ�����
	public byte[] toBytes() {
		ByteArrayOutputStream baos = null;
		//ֻ��д������������
		DataOutputStream dos = null;
		byte[] bytes = null;
		try {
			//�൱�����ڴ��������һ���ֽ����飬Ȼ��һ���ܵ����ȥ��׼��������д����
			baos = new ByteArrayOutputStream();
			//ByteArrayOutputStream ����д�����㣬DataOutputStream �ȽϺ���
			dos = new DataOutputStream(baos);
			dos.writeInt(x);
			dos.writeInt(y);
			//ordinal ��ö�����Ϳ������飬����Ԫ�ص��±�ֵ
			dos.writeInt(dir.ordinal());
			//boolean����дռһ���ֽ�
			dos.writeBoolean(moving);
			dos.writeInt(group.ordinal());
			//DataOutputStreamֻ��д�����������ͣ�����UUIDҪ��ֿ�д������
			dos.writeLong(id.getMostSignificantBits());
			dos.writeLong(id.getLeastSignificantBits());
			//*** �����������Э�����棬û�������ַ�������Ϣ ***
			//��Ϊ�ַ���ռ�ֽڱȽϴ�
			//һ������£������ϣ��ܲ����ַ����Ͳ����ַ���
			//�ο�Netty������Fatorial������
			//95%���Զ���Э�����棬�㶼Ҫд�������Ϣͷ��д�������Ϣ�ж೤
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
