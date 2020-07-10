package com.mashibing.tank;

import com.mashibing.tank.net.Client;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TankFrame tf = TankFrame.getInstance();
		tf.setVisible(true);
		
/*		int initTankCount = Integer.parseInt((String)PropertyMgr.get("initTankCount"));
		
		//��ʼ���з�̹��
		for(int i=0; i<initTankCount; i++) {
			tf.tanks.add(new Tank(50 + i*80, 200, Dir.DOWN, Group.BAD, tf));
		}*/
		
		//new Thread(()->new Audio("audio/war1.wav").loop()).start();
		
		//while(true) {
		//	Thread.sleep(25);
		//	tf.repaint();
		//}
		//�ڵ������߳�����repaint()�������ִ�в���������������
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