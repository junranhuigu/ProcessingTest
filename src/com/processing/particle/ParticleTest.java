package com.processing.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

import com.processing.Start;

public class ParticleTest extends PApplet{
	private ParticleSystem ps;
	private Area area;
	private boolean stop = false;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		smooth();
	}
	
	@Override
	public void keyPressed() {
		if(key == 'a' && area == null && ps == null){
			int w = 100;
			int h = 100;
			area = new Area(w, h, new PVector(random(width / 2 + w), random(height / 2 + h)));
			stop = false;
		}
	}
	
	@Override
	public void mouseClicked() {
		if(area != null && area.isAlive() && area.inSide(new PVector(mouseX, mouseY))){
			area.broken();
			ps = new ParticleSystem(area.getLocation());
		}
	}
	
	@Override
	public void draw() {
		background(0);
		if(ps != null && !stop){
			ps.addParticle();
			ps.addParticle();
			ps.addParticle();
		}
		if(ps != null && ps.isAlive()){
			ps.run();
		} else {
			ps = null;
		}
		if(area != null && area.isAlive()){
			area.display();
		} else {
			stop = true;
			area = null;
		}
	}
	
	private class Area{
		private float width;
		private float height;
		private PVector location;
		private boolean broken;
		
		public PVector getLocation() {
			return location.copy();
		}

		public boolean isAlive() {
			return width > 0 && height > 0;
		}

		public Area(float width, float height, PVector location) {
			this.width = width;
			this.height = height;
			this.location = location;
			this.broken = false;
		}

		public void display(){
			stroke(255);
			fill(255);
			if(broken){
				-- width;
				-- height;
			}
			rect(location.x, location.y, width, height);
		}
		
		public void broken(){
			this.broken = true;
		}
		
		public boolean inSide(PVector location){
			if(this.location.x  < location.x && this.location.x + width > location.x 
					&& this.location.y < location.y && this.location.y + height > location.y){
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	private class ParticleSystem{
		private List<Particle> plist;
		private PVector origin;
		
		public ParticleSystem(PVector location) {
			origin = location.copy();
			plist = new ArrayList<>();
		}
		
		public void addParticle(){
			Particle p = null;
			float random = random(1);
			if(random < 0.5){
				p = new Particle(origin.copy());
			} else {
				p = new Confetti(origin.copy());
			}
			plist.add(p);
		}
		
		public boolean isAlive(){
			return plist.size() > 0;
		}
		
		public void run(){
			Iterator<Particle> ite = plist.iterator();
			while(ite.hasNext()){
				Particle p = ite.next();
				if(p.isDead()){
					ite.remove();
				} else {
					p.update();
					p.display();
				}
			}
		}
	}
	
	private class Confetti extends Particle{

		public Confetti(PVector location) {
			super(location);
		}
		
		@Override
		public void display() {
			stroke(0);
			fill(random(255), lifesapn, random(255));
			rect(location.x, location.y, 6, 6);
		}
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
			stroke(0);
			fill(random(255), lifesapn, random(255));
			ellipse(location.x, location.y, 3, 3);
		}
		
	}
	
	public static void main(String[] args) {
		Start.start(ParticleTest.class);
	}
}
