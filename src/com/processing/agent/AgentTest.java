package com.processing.agent;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.dynamics.contacts.Velocity;

import processing.core.PApplet;
import processing.core.PVector;

import com.processing.Start;

public class AgentTest extends PApplet{
	private FlowField flow;
	private Path path;
	private List<Vehicle> vehicles;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		flow = new FlowField();
		path = new Path();
		vehicles = new ArrayList<>();
		
		path.addPoint(0, height / 5);
		path.addPoint(width * 0.25f, height / 3 * 2);
		path.addPoint(width * 0.5f, height / 3 * 2);
		path.addPoint(width  * 0.75f, height / 3);
		path.addPoint(width, height / 3 * 2);
		for(int i = 0 ; i < 100; ++ i){
			Vehicle v = new Vehicle(random(0, width), random(0, height));
			vehicles.add(v);
		}
	}
	
	@Override
	public void draw() {
		background(255);
//		path.display();
		for(Vehicle vehicle : vehicles){
			vehicle.applyBehaviors(vehicles);
			vehicle.update();
			vehicle.check();
			vehicle.display();
		}
	}
	
	private class Path{
		private List<PVector> points;
		
		private float radius;//宽度
		
		
		public Path() {
			this.radius = 20;
			this.points = new ArrayList<>();
		}
		
		public void addPoint(float x, float y){
			points.add(new PVector(x, y));
		}
		
		public void display(){
			stroke(0);
			noFill();
			beginShape();
			for(PVector p : points){
				vertex(p.x, p.y);
			}
			endShape();
		}
	}
	
	private class FlowField{
		private PVector[][] filed;
		private int cols, rows;
		private int resolution;//网格分辨率
		
		public FlowField() {
			this.resolution = 10;
			this.cols = width / this.resolution;
			this.rows = height / this.resolution;
			this.filed = new PVector[this.cols][this.rows];
			
			float xoff = 0;
			for(int i = 0; i < cols; ++ i){
				float yoff = 0;
				for(int j = 0; j < rows; ++ j){
//					filed[i][j] = PVector.random2D();
					float theta = map(noise(xoff, yoff), 0, 1, 0, TWO_PI);
					filed[i][j] = new PVector(cos(theta), sin(theta));
					yoff += 0.1f;
				}
				xoff += 0.1f;
			}
		}
		
		public PVector lookup(PVector lookup){
			int col = (int) constrain(lookup.x / resolution, 0, cols - 1);
			int row = (int) constrain(lookup.y / resolution, 0, rows - 1);
			return filed[col][row].copy();
		}
		
		public void display(){
			
		}
	}
	
	private class Vehicle{
		PVector location;
		private PVector velocity;
		private PVector acceleration;
		
		private float maxSpeed = 4;
		private float maxForce = 0.1f;
		private float mass = 3;
		
		public Vehicle(float x, float y) {
			this.location = new PVector(x, y);
			this.velocity = new PVector();
			this.acceleration = new PVector();
		}
		
		private void update(){
			velocity.add(acceleration);
			velocity.limit(maxSpeed);
			location.add(velocity);
			acceleration.mult(0);
		}
		
		public void applyForce(PVector force){
			force.div(mass);
			acceleration.add(force);
		}
		
		public PVector seperate(List<Vehicle> vehicles){
			float desiredSeparation = mass * 5;//最短距离
			
			PVector sum = new PVector();
			int count = 0;
			for(Vehicle v : vehicles){
				float d = PVector.dist(location, v.location);
				
				if(d > 0 && d < desiredSeparation){
					PVector diff = PVector.sub(location, v.location);
					diff.normalize();
					sum.add(diff);
					++ count;
				}
			}
			if(count > 0){
				sum.div(count);
				sum.setMag(maxSpeed);
				PVector steer = PVector.sub(sum, velocity);
				steer.limit(maxForce);
				return steer;
			} else {
				return new PVector();
			}
		}
		
		public void check(){
			if(location.x < 0){
				location.x = width;
			} else if(location.x > width){
				location.x = 0;
			}
			if(location.y < 0){
				location.y = height;
			} else if(location.y > height){
				location.y = 0;
			}
		}
		
		public void applyBehaviors(List<Vehicle> vehicles){
			PVector seperate = seperate(vehicles);
			PVector align = align(vehicles);
			PVector cohesion = cohesion(vehicles);
			
			seperate.mult(1.5f);
			align.mult(1);
			cohesion.mult(1);
			
			applyForce(seperate);
			applyForce(align);
			applyForce(cohesion);
		}
		
		public PVector align(List<Vehicle> vehicles){
			float neighbordist = 50;
			PVector sum = new PVector();
			int count = 0;
			for(Vehicle v : vehicles){
				float d = PVector.dist(location, v.location);
				if(d > 0 && d < neighbordist){
					sum.add(v.location);
					++ count;
				}
			}
			if(count > 0){
				sum.div(count);
				sum.normalize();
				sum.mult(maxSpeed);
				PVector steer = PVector.sub(sum, velocity);
				steer.limit(maxForce);
				return steer;
			} else {
				return new PVector();
			}
		}
		
		public PVector cohesion(List<Vehicle> vehicles){
			float neighbordist = 50;
			PVector sum = new PVector();
			int count = 0;
			for(Vehicle v : vehicles){
				float d = PVector.dist(location, v.location);
				if(d > 0 && d < neighbordist){
					sum.add(v.location);
					++ count;
				}
			}
			if(count > 0){
				sum.div(count);
				return seek(sum);
			} else {
				return new PVector();
			}
		}
		
		
		public PVector follow(FlowField flow){
			PVector desired = flow.lookup(location);
			desired.mult(maxSpeed);
			PVector steer = PVector.sub(desired, velocity);
			steer.limit(maxForce);
			return steer;
		}
		
		private PVector getNormalPoint(PVector predictLoc, PVector start, PVector end){
			PVector a = PVector.sub(predictLoc, start);
			PVector b = PVector.sub(end, start);
			b.normalize();
			b.mult(a.dot(b));
			PVector normalPoint = PVector.add(start, b);//在路径上寻找法线交点
			return normalPoint;
		}
		
		public PVector follow(Path path){
			PVector predict = velocity.copy();
			predict.normalize();
			predict.mult(25);
			PVector predictLoc = PVector.add(location, predict);
			
			PVector target = null;
			PVector normalPoint = null;
			float worldRecord = 1000000;
			
			for(int i = 0; i < path.points.size() - 1; ++ i){
				PVector start = path.points.get(i);
				PVector end = path.points.get(i + 1);
				normalPoint = getNormalPoint(predictLoc, start, end);
				if(normalPoint.x < start.x || normalPoint.y > end.x){
					normalPoint = end.copy();
				}
			}
			
			float distance = PVector.dist(predictLoc, normalPoint);
			if(distance < worldRecord){
				worldRecord = distance;
				target = normalPoint.copy();
			}
			return target;
		}
		
		/**
		 * 逐渐到达
		 * */
		public void arrive(PVector target){
			PVector desired = PVector.sub(target, location);//获取所需速度
			float d = desired.mag();
			desired.normalize();
			if(d < 100){
				float m = map(d, 0, 100, 0, maxSpeed);
				desired.mult(m);
			} else {
				desired.mult(maxSpeed);
			}
			PVector steer = PVector.sub(desired, velocity);//获取转向力
			steer.limit(maxForce);
			applyForce(steer);
		}
		/**
		 * 全力奔袭
		 * */
		public PVector seek(PVector target){
			PVector desired = PVector.sub(target, location);//获取所需速度
			desired.normalize();
			desired.mult(maxSpeed);
			
			PVector steer = PVector.sub(desired, velocity);//获取转向力
			steer.limit(maxForce);
			return steer;
		}
		
		public void display(){
			float theta = velocity.heading2D() + PI / 2;
			
			fill(175);
			stroke(0);
			pushMatrix();
			translate(location.x, location.y);
			rotate(theta);
			beginShape();
			vertex(0, -mass  * 2);
			vertex(-mass, mass  * 2);
			vertex(mass, mass  * 2);
			endShape(CLOSE);
			popMatrix();
		}
	}
	
	public static void main(String[] args) {
		Start.start(AgentTest.class);
	}
}
