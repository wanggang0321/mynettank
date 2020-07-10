package com.mashibing.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TankFrame extends Frame {
	
	private static final TankFrame INSTANCE = new TankFrame();
	
	Random r = new Random();

	Tank mainTank = new Tank(r.nextInt(GAME_WIDTH), r.nextInt(GAME_HEIGHT), Dir.DOWN, Group.GOOD, this);

	List<Bullet> bullets = new ArrayList<>();
	//List<Tank> tanks = new ArrayList<>();
	Map<UUID, Tank> tanks = new HashMap<>(); //map�洢tank�����ҿ���
	List<Explode> explodes = new ArrayList<>();
	
	
	static final int GAME_WIDTH = 800, GAME_HEIGHT = 500;

	private TankFrame() {
		setSize(GAME_WIDTH, GAME_HEIGHT);
		setResizable(false);
		setTitle("tank war");
		setVisible(true);

		this.addKeyListener(new MyKeyListener());

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) { // bjmashibing/tank
				System.exit(0);
			}

		});
	}
	
	public static TankFrame getInstance() {
		return INSTANCE;
	}

	Image offScreenImage = null;
	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		g.drawString("�ӵ�������:" + bullets.size(), 10, 60);
		g.drawString("���˵�����:" + tanks.size(), 10, 80);
		g.drawString("��ը������:" + explodes.size(), 10, 100);
		g.setColor(c);

		mainTank.paint(g);
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint(g);
		}
		
		//JDK1.8 ��stream����
		tanks.values().stream().forEach((e)->e.paint(g));
		
		for (int i = 0; i < explodes.size(); i++) {
			explodes.get(i).paint(g);
		}

		//collision detect 
		for(int i=0; i<bullets.size(); i++) {
			for(int j = 0; j<tanks.size(); j++) 
				bullets.get(i).collideWith(tanks.get(j));
		}
	}

	class MyKeyListener extends KeyAdapter {

		boolean bL = false;
		boolean bU = false;
		boolean bR = false;
		boolean bD = false;

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_LEFT:
				bL = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break;

			default:
				break;
			}

			setMainTankDir();
			
			//new Thread(()->new Audio("audio/tank_move.wav").play()).start();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_LEFT:
				bL = false;
				break;
			case KeyEvent.VK_UP:
				bU = false;
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;
			case KeyEvent.VK_DOWN:
				bD = false;
				break;

			case KeyEvent.VK_CONTROL:
				mainTank.fire();
				break;

			default:
				break;
			}

			setMainTankDir();
		}

		private void setMainTankDir() {

			if (!bL && !bU && !bR && !bD)
				mainTank.setMoving(false);
			else {
				mainTank.setMoving(true);

				if (bL)
					mainTank.setDir(Dir.LEFT);
				if (bU)
					mainTank.setDir(Dir.UP);
				if (bR)
					mainTank.setDir(Dir.RIGHT);
				if (bD)
					mainTank.setDir(Dir.DOWN);
			}
		}
	}
	public Tank getMainTank() {
		return this.mainTank;
	}

	public Object findByUUID(UUID id) {
		return tanks.get(id);
	}

	public void addTank(Tank t) {
		tanks.put(t.getId(), t);
	}
}