package com.processing.shock;

import processing.core.PApplet;
import processing.core.PVector;

public class ShockTest extends PApplet{
	private float angle = 0;//位置
	private float aVelocity = 0;//角速度
	private float aAcceleration = 0.001f;//角加速度
	private float angle2 = 0;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		
	}
	
	@Override
	public void draw() {
		background(255);
		text("角速度：" + aVelocity, 0, 10);
		
		fill(175);
		stroke(0);
		rectMode(CENTER);
		pushMatrix();
		translate(width / 2, height / 2);//设置图形所在位置
		rotate(angle);
		line(-50, 0, 50, 0);
		ellipse(50, 0, 8, 8);
		ellipse(-50, 0, 8, 8);
		aVelocity += aAcceleration;
		angle += aVelocity;
		popMatrix();
		
		float amplitude = 100;
		float x = amplitude * cos(angle);
		stroke(0);
		fill(175);
		translate(width / 2, height / 2 - 200);
		line(0, 0, x, 0);
		ellipse(x, 0, 20, 20);
		
		for(int i = 0; i <= width; i += 24){
			float y = amplitude * sin(angle2);
			ellipse(i, y + height / 2, 48, 48);
			float n = noise(height / 2);
			angle2 += map(n, 0, 1, 0, 1);
		}
	}
	
	private class Oscillator{
		PVector angle;//角度
		PVector velocity;//角速度
		PVector amplitude;//振幅
		
		public Oscillator() {
			this.angle = new PVector();
			this.velocity = new PVector(random(-0.05f, 0.05f), random(-0.05f, 0.05f));
			this.amplitude = new PVector(random(width / 2), random(height / 2));
		}
		
		public void oscillate(){
			angle.add(velocity);
		}
		
		public void display(){
			float x = sin(angle.x) * amplitude.x;
			float y = sin(angle.y) * amplitude.y;
			
			pushMatrix();
			translate(width / 2, height / 2);
			stroke(0);
			fill(175);
			line(0, 0, x, y);
			ellipse(x, y, 20, 20);
			popMatrix();
		}
	}
	
	public static void main(String[] args) {
		String[] ss = new String[]{ "--present", ShockTest.class.getName()};
		PApplet.main(ss);
	}
}
