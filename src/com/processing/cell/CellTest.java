package com.processing.cell;

import processing.core.PApplet;

import com.processing.Start;

public class CellTest extends PApplet{
	private CA ca;
	
	@Override
	public void settings() {
		size(800, 800);
	}
	
	@Override
	public void setup() {
		ca = new CA(5);
	}
	
	@Override
	public void draw() {
//		for(int i = 0; i < 256; ++ i){
//			String s = Integer.toBinaryString(i);
//			s = new StringBuffer(s).reverse().toString();
//			int[] set = new int[ca.ruleset.length];
//			for(int j = 0; j < set.length; ++ j){
//				set[j] = getF(s, j);
//			}
//			ca.ruleset = set;
			ca.display();
			while(ca.generate <= height / ca.w){
				ca.generate();
				ca.display();
			}
//			save("C:\\Users\\jiawei\\Desktop\\ff\\" + (i + 1) + ".jpg");
//		}
//		exit();
	}
	
	private int getF(String s, int num){
		if(num >= s.length()){
			return 0;
		} else {
			return Integer.parseInt(s.charAt(num) + "");
		}
	}
	
	private class CA{
		private int[] cells;
		private int[] ruleset = {0,1,0,1,1,0,1,0};
		private int w;
		private int generate;
		
		public CA(int w) {
			this.w = w;
			this.generate = 0;
			cells = new int[width / w];
			
			for(int i = 0; i < cells.length; ++ i){
				cells[i] = 0;
			}
			cells[cells.length / 2] = 1;
		}
		
		public void generate(){
			int[] next = new int[cells.length];
			for(int i = 1; i < cells.length - 1; ++ i){
				next[i] = rules(cells[i - 1], cells[i], cells[i + 1]);
			}
			cells = next;
			++ generate;
		}
		
		public int rules(int a, int b, int c){
			String s = "" + a + b + c;
			int index = Integer.parseInt(s, 2);
			return ruleset[index];
		}
		
		public void display(){
			for(int i = 0; i < cells.length; ++ i){
				if(cells[i] == 1){
					fill(0);
				} else {
					fill(255);
				}
				rect(i * w, generate * w, w, w);
			}
		}
	}
	
	private class Cell{
		
	}
	
	public static void main(String[] args) {
		Start.start(CellTest.class);
	}
}
