package mynettank;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.net.TankJoinMsg;
import com.mashibing.tank.net.TankJoinMsgDecoder;
import com.mashibing.tank.net.TankJoinMsgEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

//一定一定一定要写Codec的单元测试
//这个重要
public class TankJoinMsgCodecTest {

	@Test
	public void testEncode() {
		EmbeddedChannel ch = new EmbeddedChannel();
		
		UUID id = UUID.randomUUID();
		TankJoinMsg msg = new TankJoinMsg(5, 10, Dir.DOWN, true, Group.BAD, id);
		ch.pipeline().addLast(new TankJoinMsgEncoder());
		
		//写出去
		ch.writeOutbound(msg);
		
		//读出来
		ByteBuf buf = (ByteBuf) ch.readOutbound();
		
		int x = buf.readInt();
		int y = buf.readInt();
		Dir dir = Dir.values()[buf.readInt()];
		boolean moving = buf.readBoolean();
		Group group = Group.values()[buf.readInt()];
		UUID uid = new UUID(buf.readLong(), buf.readLong());
		
		assertEquals(5, x);
		assertEquals(10, y);
		assertEquals(Dir.DOWN, dir);
		assertEquals(true, moving);
		assertEquals(Group.BAD, group);
		assertEquals(id, uid);
	}
	
	@Test
	public void testDecoder() {
		EmbeddedChannel ch = new EmbeddedChannel();
		
		UUID id = UUID.randomUUID();
		TankJoinMsg msg = new TankJoinMsg(5, 10, Dir.DOWN, true, Group.BAD, id);
		ch.pipeline().addLast(new TankJoinMsgDecoder());
		
		ByteBuf buf = Unpooled.buffer();
		buf.writeBytes(msg.toBytes());
		
		ch.writeInbound(buf.duplicate());
		
		//读出来
		TankJoinMsg msgR = (TankJoinMsg) ch.readInbound();
		
		assertEquals(5, msgR.x);
		assertEquals(10, msgR.y);
		assertEquals(Dir.DOWN, msgR.dir);
		assertEquals(true, msgR.moving);
		assertEquals(Group.BAD, msgR.group);
		assertEquals(id, msgR.id);
	}

}
