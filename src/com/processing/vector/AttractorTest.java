package com.processing.vector;

import processing.core.PApplet;
import processing.core.PVector;

public class AttractorTest extends PApplet{
	private Mover[] mover;
	private Attractor attractor;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		mover = new Mover[10];
		for(int i = 0; i < mover.length; ++ i){
			mover[i] = new Mover();
		}
		attractor = new Attractor();
	}
	
	@Override
	public void draw() {
		background(255);
		attractor.display();
		
		for(int i = 0; i < mover.length; ++ i){
			PVector f = attractor.attract(mover[i]);
			mover[i].applyForce(f);
			mover[i].update();
			mover[i].display();
		}
		
	}
	
	private class Mover{
		PVector location;//位置
		PVector velocity;//速度
		
		PVector acceleration;//加速度
		
		float mass;//质量
		
		float topSpeed;
		
		float angle = 0;
		float aVelocity = 0.2f;
		float aAcceleration = 0f;
		
		public Mover() {
			location = new PVector(random(width), random(height));
			velocity = new PVector(0, 0);//初始速度为0
			acceleration = new PVector(0, 0);
			mass = random(0.1f, 2);
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

	
	private class Attractor{
		float mass;
		PVector location;
		float G = 0.4f;
		
		public Attractor() {
			location = new PVector(width / 2, height / 2);
			mass = 20;
		}
		
		public PVector attract(Mover mover) {
			PVector force = PVector.sub(location, mover.location);
			float distance = force.mag();
			distance = constrain(distance, 5, 25);
			
			force.normalize();
			float strength = G * mass * mover.mass / (distance * distance);
			force.mult(strength);
			return force;
		}

		public void display(){
			stroke(0);
			fill(175, 200);
			ellipse(location.x, location.y, mass * 2, mass * 2);
		}
	}
	
	public static void main(String[] args) {
		String[] ss = new String[]{ "--present", AttractorTest.class.getName()};
		PApplet.main(ss);
	}
}
