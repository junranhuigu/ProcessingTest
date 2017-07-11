package com.processing.vector;

import processing.core.PApplet;

public class PerlinTest extends PApplet{
	private float tx = 0;
	private float ty = 10086;
	
	@Override
	public void settings() {
		size(400, 400);
		super.settings();
	}
	
	@Override
	public void setup() {
		super.setup();
	}
	
	@Override
	public void draw() {
		background(255);
		float n = noise(tx);
		float n2 = noise(ty);
		tx += 0.01;
		ty += 0.01;
		float x = map(n, 0, 1, 0, width);
		float y = map(n2, 0, 1, 0, height);
		ellipse(x, y, 16, 16);
	}
	
	public static void main(String[] args) {
		String[] ss = new String[]{ "--present", PerlinTest.class.getName()};
		PApplet.main(ss);
	}
}
