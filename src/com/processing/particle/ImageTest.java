package com.processing.particle;

import com.processing.Start;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class ImageTest extends PApplet{
	private PImage img;
	private Particle p;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		img = loadImage("C:\\Users\\jiawei\\Desktop\\10000992064020.png");
		p = new Particle(new PVector(width / 2, height / 2));
	}
	
	private class Particle{
		protected PVector location;
		protected PVector velocity;
		protected PVector acceleration;
		protected float lifesapn;
		
		public Particle(PVector location) {
			this.location = location.copy();
			this.acceleration = new PVector(random(-0.1f, 0.1f), random(-0.1f, 0.1f));
			this.velocity = new PVector(random(1, -1), random(2, -2));
			this.lifesapn = 255;
		}
		
		public void update(){
			velocity.add(acceleration);
			location.add(velocity);
			lifesapn -= 2;
		}
		
		public void applyForce(PVector force){
			acceleration.add(force);
		}
		
		public boolean isDead(){
			return lifesapn < 0;
		}
		
		public void display(){
//			stroke(0);
//			fill(random(255), lifesapn, random(255));
//			ellipse(location.x, location.y, 3, 3);
			imageMode(CENTER);
			tint(255, lifesapn);
			image(img, location.x, location.y);
		}
		
	}
	
	@Override
	public void draw() {
		background(255);
		p.update();
		p.display();
	}
	
	public static void main(String[] args) {
		Start.start(ImageTest.class);
	}
}
