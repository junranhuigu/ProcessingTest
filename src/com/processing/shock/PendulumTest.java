package com.processing.shock;

import com.processing.Start;

import processing.core.PApplet;
import processing.core.PVector;

public class PendulumTest extends PApplet{
	private Pendulum p;
	private Mover mover;
	private Spring spring;

	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		p = new Pendulum();
		mover = new Mover(width / 2, height / 2);
		spring = new Spring(width / 2, height / 2 - mover.mass * 16 - 10, 30);
	}
	
	@Override
	public void draw() {
		background(255);
//		p.update();
//		p.display();
		
		PVector gravity = new PVector(0, 1);
		mover.applyForce(gravity);
		spring.connect(mover);
		
		mover.update();
		mover.display();
		spring.displayLine(mover);
	}
	
	private class Spring{
		private PVector anchor;
		private float len;//弹簧静止长度
		private float length;//弹簧长度
		private float k = 0.1f;
		public Spring(float x, float y, float len) {
			this.anchor = new PVector(x, y);
			this.len = len;
			this.length = len;
		}
		
		public void connect(Mover m){
			PVector force = PVector.sub(m.location, anchor);
			float d = force.mag();
			float stretch = d - len;
			force.normalize().mult(-1 * k * stretch);
			
			m.applyForce(force);
		}
		
		public void display(){
			fill(100);
			rect(anchor.x, anchor.y, 10, length);
		}
		
		public void displayLine(Mover m){
			stroke(100);
			line(m.location.x, m.location.y, anchor.x, anchor.y);
		}
	}
	private class Mover{
		PVector location;//位置
		PVector velocity;//速度
		
		PVector acceleration;//加速度
		
		float mass;//质量
		
		float topSpeed;
		
		public Mover(float x, float y) {
			location = new PVector(x, y);
			velocity = new PVector(0, 0);//初始速度为0
			acceleration = new PVector(0, 0);
			mass = random(3, 5);
			topSpeed = 10;
		}
		
		public void update(){
			
			velocity.add(acceleration);
			velocity.limit(topSpeed);
			
			location.add(velocity);
			
			acceleration.mult(0);
		}
		
		public void applyForce(PVector force){
			PVector f = PVector.div(force, mass);
			acceleration.add(f);
		}
		
		public void display(){
			stroke(0);
			fill(175, 200);
			rect(location.x, location.y, mass * 16, mass * 16);
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
	private class Pendulum{
		private float r;//摆臂长度
		private float angle;//摆臂角度
		private float aVelocity;//角速度
		private float aAccelaration;//角加速度
		public Pendulum() {
			r = 125;
			angle = PI / 2;
			aVelocity = 0;
			aAccelaration = 0;
		}
		
		public void update(){
			float gravity = 0.4f;//重力加速度常数
			aAccelaration = -1 * gravity * sin(angle) / r;
			aVelocity += aAccelaration;
			angle += aVelocity;
			aVelocity *= 0.995;//减震
		}
		
		public void display(){
			PVector origin = new PVector(width / 2, height / 2);
			PVector location = new PVector(r * sin(angle), r * cos(angle));
			location.add(origin);
			
			stroke(0);
			line(origin.x, origin.y, location.x, location.y);
			fill(175);
			ellipse(location.x, location.y, 16, 16);
		}
	}
	
	public static void main(String[] args) {
		Start.start(PendulumTest.class);
	}
}
