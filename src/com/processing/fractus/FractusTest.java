package com.processing.fractus;

import java.util.ArrayList;

import com.processing.Start;

import processing.core.PApplet;
import processing.core.PVector;

public class FractusTest extends PApplet{
	private ArrayList<KochLine> lines = new ArrayList<>();
	private float theta;
	
	@Override
	public void settings() {
		size(800, 800);
		
		PVector start = new PVector(0, height / 2);
		PVector end = new PVector(width, height / 2);
		lines.add(new KochLine(start, end));
		
	}
	
	@Override
	public void draw() {
		background(255);
//		drawCircle(width / 2, height / 2, 400);
		
//		for(KochLine l : lines){
//			l.display();
//		}
//		generateKochLines();
		
		theta = map(mouseX, 0, width, 0, PI / 2);
		translate(width / 2, height);
		stroke(0);
		branch(100, theta);
	}
	
	private void drawCircle(int x , int y, float radius){
		stroke(0);
		noFill();
		ellipse(x, y, radius, radius);
		if(radius > 10){
			drawCircle((int)(x + radius / 2), y, radius / 2);
			drawCircle((int)(x - radius / 2), y, radius / 2);
			drawCircle(x, (int)(y - radius / 2), radius / 2);
			drawCircle(x, (int)(y + radius / 2), radius / 2);
		}
	}
	
	private void branch(float len, float theta){
		theta = random(0, PI / 3);
		line(0, 0, 0, -len);
		translate(0, -len);//平移
		
		len *= 0.66f;
		
		if(len > 2){
			pushMatrix();
			rotate(theta);
			branch(len, theta);
			popMatrix();
			pushMatrix();
			rotate(-theta);
			branch(len, theta);
			popMatrix();
		}
	}
	
	private void generateKochLines(){
		ArrayList<KochLine> next = new ArrayList<>();
		for(KochLine l : lines){
			if(PVector.sub(l.end, l.start).mag() < 5){
				return;
			}
			PVector a = l.kochA();
			PVector b = l.kochB();
			PVector c = l.kochC();
			PVector d = l.kochD();
			PVector e = l.kochE();
			
			next.add(new KochLine(a, b));
			next.add(new KochLine(b, c));
			next.add(new KochLine(c, d));
			next.add(new KochLine(d, e));
		}
		
		lines = next;
	}
	
	private class KochLine{
		private PVector start;
		private PVector end;
		public KochLine(PVector start, PVector end) {
			this.start = start;
			this.end = end;
		}
		public void display(){
			stroke(0);
			line(start.x, start.y, end.x, end.y);
		}
		public PVector kochA(){
			return start.copy();
		}
		public PVector kochB(){
			PVector v = PVector.sub(end, start);
			v.div(3);
			v.add(start);
			return v;
		}
		public PVector kochC(){
			PVector b = kochB();
			PVector v = PVector.sub(b, start);
			v.rotate( - radians(60));
			v.add(b);
			return v;
		}
		public PVector kochD(){
			PVector v = PVector.sub(end, start);
			v.mult(2 / 3.0f);
			v.add(start);
			return v;
		}
		public PVector kochE(){
			return end.copy();
		}
	}
	
	public static void main(String[] args) {
		Start.start(FractusTest.class);
	}
}
