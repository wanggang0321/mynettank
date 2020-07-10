package com.mashibing.tank;

import com.mashibing.tank.net.Client;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TankFrame tf = TankFrame.getInstance();
		tf.setVisible(true);
		
/*		int initTankCount = Integer.parseInt((String)PropertyMgr.get("initTankCount"));
		
		//初始化敌方坦克
		for(int i=0; i<initTankCount; i++) {
			tf.tanks.add(new Tank(50 + i*80, 200, Dir.DOWN, Group.BAD, tf));
		}*/
		
		//new Thread(()->new Audio("audio/war1.wav").loop()).start();
		
		//while(true) {
		//	Thread.sleep(25);
		//	tf.repaint();
		//}
		//在单独的线程里面repaint()，否则就执行不了再往下面的语句
		new Thread(()-> {
			while(true) {
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tf.repaint();
			}
		}).start();
		
		Client c = new Client();
		c.connect();
	}

}