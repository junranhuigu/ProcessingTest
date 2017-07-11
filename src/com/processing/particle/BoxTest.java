package com.processing.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PApplet;
import processing.core.PVector;
import shiffman.box2d.Box2DProcessing;

import com.processing.Start;

public class BoxTest extends PApplet{
	private Box2DProcessing box2d;
	private ArrayList<Box> list;
	private Surface surface;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		box2d = new Box2DProcessing(this);
		box2d.createWorld();
		
		list = new ArrayList<>();
		surface = new Surface();
	}
	
	@Override
	public void draw() {
		box2d.step();
		background(255);
		
		surface.display();
		
		if(mousePressed){
			Box b = new Box(mouseX, mouseY);
			list.add(b);
		}
		
		Iterator<Box> ite = list.iterator();
		while(ite.hasNext()){
			Box b = ite.next();
			b.display();
			if(b.isDead()){
				ite.remove();
				b.killBody();
			}
		}
		
	}
	
	private class Box{
		private float x,y,w,h;
		private Body body;

		public Box(float x, float y) {
			this.x = x;
			this.y = y;
			this.w = 16;
			this.h = 16;
			
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DYNAMIC;
			Vec2 center = box2d.coordPixelsToWorld(this.x, this.y);
			bd.position.set(center);
//			bd.fixedRotation = true;
//			bd.linearDamping = 0.8f;
//			bd.angularDamping = 0.9f;
			bd.bullet = true;
			body = box2d.createBody(bd);
//			body.setLinearVelocity(new Vec2(0, 3));
//			body.setAngularVelocity(1.2f);
			
			PolygonShape shape = new PolygonShape();
			float sw = box2d.scalarPixelsToWorld(w / 2);
			float sh = box2d.scalarPixelsToWorld(h / 2);
			shape.setAsBox(sw, sh);
			
			FixtureDef fixture = new FixtureDef();
			fixture.shape = shape;
			fixture.friction = 0.3f;//摩擦系数
			fixture.restitution = 0.5f;//弹性
			fixture.density = 1;//密度
			
			body.createFixture(fixture);
		}
		
		public void display(){
			PVector loc = box2d.coordWorldToPixelsPVector(body.getTransform().p);
			float a = body.getAngle();
			
			pushMatrix();
			fill(175);
			stroke(0);
			translate(loc.x, loc.y);
			rotate(-a);
			rectMode(CENTER);
			rect(0, 0, w, h);
			popMatrix();
		}
		
		public boolean isDead(){
			Vec2 position = body.getPosition();
			PVector p = box2d.coordWorldToPixelsPVector(position);
			if(p.x >= width || p.x <= 0 || p.y >= height || p.y <= 0){
				return true;
			} else {
				return false;
			}
		}
		
		public void killBody(){
			box2d.destroyBody(body);
		}
	}
	
	private class Surface{
		private List<Vec2> surface;
		
		public Surface() {
			surface = new ArrayList<>();
			float r = 1;
			for(int i = 1; i < width; i += 10){
				float rn = noise(r);
				r += 0.1f;
				float h = map(rn, 0, 1, 0, height / 2);
				surface.add(new Vec2(i, h + height * 0.5f));
			}
			
			ChainShape chain = new ChainShape();
			Vec2[] vs = new Vec2[surface.size()];
			for(int i = 0; i < vs.length; ++ i){
				vs[i] = box2d.coordPixelsToWorld(surface.get(i)); 
			}
			
			chain.createChain(vs, vs.length);
			
			BodyDef bd = new BodyDef();
			Body body = box2d.createBody(bd);
			body.createFixture(chain, 1);
		}
		
		public void display(){
			strokeWeight(1);
			stroke(0);
			noFill();
			beginShape();
			for(Vec2 v : surface){
				vertex(v.x, v.y);
			}
			endShape();
		}
	}
	
	public static void main(String[] args) {
		Start.start(BoxTest.class);
	}
}
