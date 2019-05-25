package com.engine.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game_Engine extends Canvas implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 747072348233716558L;

	//9x12 Aspect Ratio
	private static final int WIDTH = 640, HEIGHT = WIDTH/12 * 9;
	
	private Thread thread;
	private boolean running = false;
	
	private int colorSwitchR = 1, colorSwitchG = 1, colorSwitchB = 1;
	private float R = (float) (Math.random()), G = (float) (Math.random()), B = (float) (Math.random());

	public Game_Engine() {
		new Window(WIDTH, HEIGHT, "Project Wheat v1.0", this);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running) {
				render();
			}
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
		
		
	}
	
	private void tick() {
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		//Game Window Background Color
		//g.setColor(Color.black);
		//g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Rainbow Background
		R += (float) (Math.random()*0.0001f)*colorSwitchR;
		G += (float) (Math.random()*0.00007f)*colorSwitchG;
		B += (float) (Math.random()*0.00004f)*colorSwitchB;
		
		//These limiters can be changed to alter the maximum and minimum brightness
		if(R > 1) {
			R = 1;
			colorSwitchR = -1;
		}
		if(G > 1) {
			G = 1;
			colorSwitchG = -1;
		}
		if(B > 1) {
			B = 1;
			colorSwitchB = -1;
		}
		if(R < 0.5f) {
			R = 0.5f;
			colorSwitchR = 1;
		}
		if(G < 0.5f) {
			G = 0.5f;
			colorSwitchG = 1;
		}
		if(B < 0.5f) {
			B = 0.5f;
			colorSwitchB = 1;
		}
		
		g.setColor(new Color(R, G, B));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.dispose();
		bs.show();
		
	}
	
	public static void main(String[]args) {
		new Game_Engine();
	}
}
