package com.mashibing.tank;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TankFrame tf = new TankFrame();
		
/*		int initTankCount = Integer.parseInt((String)PropertyMgr.get("initTankCount"));
		
		//��ʼ���з�̹��
		for(int i=0; i<initTankCount; i++) {
			tf.tanks.add(new Tank(50 + i*80, 200, Dir.DOWN, Group.BAD, tf));
		}*/
		
		new Thread(()->new Audio("audio/war1.wav").loop()).start();
		
		while(true) {
			Thread.sleep(25);
			tf.repaint();
		}
		
	}

}