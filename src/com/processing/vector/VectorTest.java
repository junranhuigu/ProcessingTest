package com.processing.vector;

import processing.core.PApplet;
import processing.core.PVector;

public class VectorTest extends PApplet{
	private Mover[] movers;
	private Liquid liquid;
	
	@Override
	public void settings(){
		//3d
//		size(200, 200, P3D);
//		location = new PVector(100, 100, 100);
//		velocity = new PVector(1, 3.3f, 3.3f);
		//2d
		size(800, 800);
	}
	@Override
	public void setup(){
		smooth();
		movers = new Mover[10];
		PVector vector = new PVector(5, 10);//初始发射
		for(int i = 0; i < movers.length; ++ i){
			movers[i] = new Mover();
			movers[i].applyForce(vector.copy());
		}
		liquid = new Liquid(0.1f, 0, height / 2, width, height / 2);
	}
	@Override
	public void draw(){
		background(255);
		
		liquid.display();
		
		for(Mover mover : movers){
			mover.update_mouse();
			mover.checkEdges();
			mover.display();
		}
		
	}
	
	private class Mover {
		private PVector location;//位置
		private PVector velocity;//速度
		
		private PVector acceleration;//加速度
		
		private float mass;//质量
		
		private float topSpeed;
		
		public Mover() {
			location = new PVector(random(width), 100);
			velocity = new PVector(0, 0);//初始速度为0
			acceleration = new PVector(0, 0);
			mass = random(0.1f, 2);
			topSpeed = 10;
		}
		/**
		 * 计算摩擦力
		 * @param mew : 摩擦系数
		 * */
		public PVector calFriction(float mew){
			float normal = 1;//正向力
			return velocity.copy().mult(mew * normal);
		}
		/**
		 * 计算重力
		 * @param mew : 重力系数
		 * */
		public PVector calGravity(float mew){
			return new PVector(0, mew * mass);
		}
		
		/**
		 * 计算流体阻力
		 * @param c : 阻力系数
		 * @param rho : 流体密度
		 * @param area : 受力面积
		 * */
		public PVector calResistance(Liquid liquid){
			float speed = velocity.mag();
			PVector drag = velocity.copy().normalize();
			drag.mult( - 0.5f * liquid.getMultC() * speed * speed);
			return drag;
		}
		
		public boolean inSide(Liquid liquid){
			if(location.x > liquid.x && location.x < liquid.x + liquid.width_l
					&& location.y > liquid.y && location.y < liquid.y + liquid.height_l){
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * 计算物体在流体中的效果
		 * */
		public void drag(Liquid liquid){
			PVector resistance = calResistance(liquid);
			applyForce(resistance);
		}
		
		public void update(){
//			PVector wind = new PVector(0.01f, 0);//风力
//			applyForce(wind);
			PVector gravity = calGravity(0.1f);//重力
			applyForce(gravity);
//			PVector friction = calFriction(0.01f);//摩擦力
//			applyForce(friction);
			if(inSide(liquid)){//流体阻力
				drag(liquid);
			}
			
			velocity.add(acceleration);
			velocity.limit(topSpeed);
			
			location.add(velocity);
			acceleration.mult(0);
		}
		
		public void update_mouse(){
			if(mousePressed){
				PVector mouse = new PVector(mouseX, mouseY);
				PVector dir = PVector.sub(mouse, location);
				dir.normalize();
				dir.mult(0.5f);
				acceleration = dir;
			}
			
			if(inSide(liquid)){//流体阻力
				drag(liquid);
			}
			
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
			fill(175);
			ellipse(location.x, location.y, 16, 16);
			
//			text(velocity.mag() + "-" + acceleration.mag(), 0, 10);
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
	
	/**
	 * 流体
	 * */
	private class Liquid{
		private float rho;//流体密度
		private float area;//流体阻力面积
		private float c;//阻力系数
		private float x,y,width_l,height_l;
		public Liquid(float c, float x, float y, float width_l, float height_l) {
			this.rho = 1;
			this.area = 1;
			this.c = c;
			this.x = x;
			this.y = y;
			this.width_l = width_l;
			this.height_l = height_l;
		}
		public Liquid(float rho, float area, float c, float x, float y, float width, float height) {
			this.rho = rho;
			this.area = area;
			this.c = c;
			this.x = x;
			this.y = y;
			this.width_l = width;
			this.height_l = height;
		}
		public void display(){
			noStroke();
			fill(174, 238, 238);
			rect(x, y, width_l, height_l);
		}
		/**
		 * 获取若干参数的乘积
		 * */
		public float getMultC(){
			return c * area * rho;
		}
	}
	
	private class Attractor{
		private float mass;
		private PVector location;
		
		public Attractor() {
			location = new PVector(width / 2, height / 2);
			mass = 20;
		}
		
		public void display(){
			stroke(0);
			fill(175, 200);
			ellipse(location.x, location.y, mass * 2, mass * 2);
		}
	}
	
	public static void main(String[] args) {
		String[] ss = new String[]{ "--present", VectorTest.class.getName()};
		PApplet.main(ss);
	}
}
