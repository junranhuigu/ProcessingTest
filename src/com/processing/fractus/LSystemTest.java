package com.processing.fractus;

import com.processing.Start;

import processing.core.PApplet;

public class LSystemTest extends PApplet{
	private LSystem lsys;
	private Turtle turtle;
	
	@Override
	public void settings() {
		size(800, 800);
		Rule[] rules = new Rule[1];
		rules[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");
		lsys = new LSystem("F", rules);
		turtle = new Turtle(lsys.current, height / 4, radians(25));
	}
	
	@Override
	public void draw() {
		background(255);
		translate(0, height / 2);
		turtle.display();
	}
	
	@Override
	public void mousePressed() {
		lsys.generate();
		turtle.setToDo(lsys.current);
		turtle.changeLen(0.5f);
	}
	
	private class Rule{
		private char ruleLeft;
		private String ruleRight;
		
		public Rule(char begin, String rule){
			this.ruleLeft = begin;
			this.ruleRight = rule;
		}
	}
	
	private class Turtle{
		private String s;
		private float len;
		private float theta;
		
		public Turtle(String s, float len, float theta) {
			this.s = s;
			this.len = len;
			this.theta = theta;
		}
		
		public void setToDo(String s){
			this.s = s;
			display();
		}
		
		public void changeLen(float multiple){
			this.len *= multiple;
		}

		public void display(){
			for(int i = 0; i < s.length(); ++ i){
				char c = s.charAt(i);
				switch (c) {
				case 'F':
					line(0, 0, len, 0);
					translate(len, 0);
					break;
				case 'G':
					translate(len, 0);
					break;
				case '+':
					rotate(theta);
					break;
				case '-':
					rotate(-theta);
					break;
				case '[':
					pushMatrix();
					break;
				case ']':
					popMatrix();
					break;
				}
			}
		}
	}
	
	private class LSystem{
		private int count = 0;//迭代次数
		private String current;//公理
		private Rule[] rules;
		
		public LSystem(String current, Rule[] rules) {
			this.current = current;
			this.rules = rules;
		}
		
		public void generate(){
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < current.length(); ++ i){
				char c = current.charAt(i);
				for(Rule r : rules){
					if(c == r.ruleLeft){
						builder.append(r.ruleRight);
					}
				}
			}
			++ count;
			current = builder.toString();
		}
		
	}
	
	public static void main(String[] args) {
		Start.start(LSystemTest.class);
	}
}
