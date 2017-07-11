package com.processing.shock;

import processing.core.PApplet;
import processing.core.PVector;

public class CarTest extends PApplet{
	private Mover mover;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		mover = new Mover();
	}
	
	@Override
	public void draw() {
		background(255);
		
		mover.update();
		mover.checkEdges();
		mover.display();
		
	}
	
	@Override
	public void keyPressed() {
		switch (key) {
		case '8':
			mover.acceleration.y -= 0.1;
			break;
		case '5':
			mover.acceleration.y += 0.1;
			break;
		case '4':
			mover.acceleration.x -= 0.1;
			break;
		case '6':
			mover.acceleration.x += 0.1;
			break;
		default:
			mover.acceleration.mult(0);
			break;
		}
	}
	
	private class Mover{
		PVector location;//位置
		PVector velocity;//速度
		
		PVector acceleration;//加速度
		
		float mass;//质量
		
		float topSpeed;
		
		float angle = 0;
		float aVelocity = 0;
		float aAcceleration = 0f;
		
		public Mover() {
			location = new PVector(random(width), random(height));
			velocity = new PVector(0, 0);//初始速度为0
			acceleration = new PVector(0, 0);
//			mass = random(0.1f, 2);
			mass = 2;
			topSpeed = 10;
		}
		
		public void update(){
			
			velocity.add(acceleration);
			velocity.limit(topSpeed);
			
			location.add(velocity);
			
			aVelocity += aAcceleration;
			angle += aVelocity;
			
			acceleration.mult(0);
		}
		
		public void applyForce(PVector force){
			PVector f = PVector.div(force, mass);
			acceleration.add(f);
		}
		
		public void display(){
			stroke(0);
			fill(175, 200);
			rectMode(CENTER);
			pushMatrix();
			translate(location.x, location.y);
			rotate(angle);
			rect(0, 0, mass * 8, mass * 8);
			popMatrix();
			
			text(velocity.mag(), 0, 10);
		}
		
		public void checkEdges(){
			if(location.x > width){
				location.x = 0;
			} else if(location.x < 0){
				location.x = width;
			}
			if(location.y > height){
				location.y = 0;
			} else if(location.y < 0){
				location.y = height;
			}
		}
		public void checkEdges2(){
			location.x = Math.min(location.x, width);
			location.x = Math.max(location.x, 0);
			location.y = Math.min(location.y, height);
			location.y = Math.max(location.y, 0);
			if(location.x >= width || location.x <= 0){
				velocity.x *= -1;
			}
			if(location.y >= height || location.y <= 0){
				velocity.y *= -1;
			}
		}
	}

	public static void main(String[] args) {
		String[] ss = new String[]{ "--present", CarTest.class.getName()};
		PApplet.main(ss);
	}
}
