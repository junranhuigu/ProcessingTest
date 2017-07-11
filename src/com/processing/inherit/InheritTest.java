package com.processing.inherit;

import java.util.Arrays;
import java.util.Comparator;

import processing.core.PApplet;

import com.processing.Start;

public class InheritTest extends PApplet{
	private DNA[] population;//个体数组
	private String target = "to be or not to be";
	private float mutationRate = 0.01f;//突变率
	private int totalPopulation = 1000;
	private int count = 0;
	
	@Override
	public void settings() {
		size(800, 800);
		population = new DNA[totalPopulation];
		for(int i = 0; i < population.length; ++ i){
			population[i] = new DNA();
		}
	}
	
	@Override
	public void draw() {
		background(0);
		float avgFitness = 0;
		for(DNA dna : population){
			dna.fitness();
			avgFitness += dna.fitness;
		}
		Arrays.sort(population, new Comparator<DNA>() {

			@Override
			public int compare(DNA o1, DNA o2) {
				if(o1.fitness < o2.fitness){
					return 1;
				} else if(o1.fitness == o2.fitness){
					return 0;
				} else {
					return -1;
				}
			}
			
		});
		text("最佳匹配 ：" + population[0].toString(), 50, 100);
		text("累计匹配次数 ：" + (++ count), 50, 150);
		text("平均匹配概率 ：" + (avgFitness / population.length), 50, 200);
		text("变异率 ：" + mutationRate, 50, 250);
		//生成交配池
		Line line = new Line();
		for(int i = 0; i < population.length; ++ i){
			line.add(population.length - i, population[i]);
		}
		//繁衍后代
		for(int i = 0; i < population.length; ++ i){
			DNA parentA = (DNA) line.getRandom();
			DNA parentB = (DNA) line.getRandom();
			//交叉 + 突变
			DNA child = parentA.crossover(parentB);
			child.mutate();
			population[i] = child;
		}
		if(population[0].toString().equals(target)){
			stop();
		}
//		try {
//			Thread.sleep(500);
//		} catch (Exception e) {}
	}
	
	private class DNA{
		
		private char[] genes = new char[target.length()];
		private float fitness;
		
		public DNA(){
			for(int i = 0; i < genes.length; ++ i){
				genes[i] = (char) random(32, 128);
			}
		}
		
		//获取适应度分值
		public void fitness(){
			int score = 0;
			for(int i = 0; i < genes.length; ++ i){
				if(genes[i] == target.charAt(i)){
					++ score;
				}
			}
			fitness = (score + 0.0f) / target.length();
		}
		
		//交叉
		public DNA crossover(DNA partner){
			DNA child = new DNA();
			
			int midpoint = (int) random(genes.length);
			for(int i = 0; i < genes.length; ++ i){
				if(i > midpoint){
					child.genes[i] = genes[i];
				} else {
					child.genes[i] = partner.genes[i];
				}
			}
			return child;
		}
		
		//突变
		public void mutate(){
			for(int i = 0; i < genes.length; ++ i){
				if(random(1) < mutationRate){
					genes[i] = (char) random(32, 128);
				}
			}
		}
		
		@Override
		public String toString() {
			return new String(genes);
		}
	}
	
	public static void main(String[] args) {
		Start.start(InheritTest.class);
	}
}
