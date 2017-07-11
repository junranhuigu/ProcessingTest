package com.processing.particle;

import processing.core.PApplet;
import shiffman.box2d.Box2DProcessing;

import com.processing.Start;

public class Box2Test extends PApplet{
	private Box2DProcessing box2d;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		box2d = new Box2DProcessing(this);
		box2d.createWorld();
		box2d.listenForCollisions();
	}
	
	@Override
	public void draw() {
		
	}
	
	public static void main(String[] args) {
		Start.start(Box2Test.class);
	}
}
