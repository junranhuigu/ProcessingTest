package com.processing.neuron;

import java.util.ArrayList;

import com.processing.Start;

import processing.core.PApplet;
import processing.core.PVector;


public class NeuronTest extends PApplet{
	private Network network;
	
	@Override
	public void settings() {
		size(800, 800);
		network = new Network(width / 2, height / 2);
		
		Neuron a = new Neuron(-200, 0);
		Neuron b = new Neuron(0, 100);
		Neuron c = new Neuron(0, -100);
		Neuron d = new Neuron(200, 0);
		
		network.addNeuron(a);
		network.addNeuron(b);
		network.addNeuron(c);
		network.addNeuron(d);
		
		network.connect(a, b);
		network.connect(a, c);
		network.connect(b, d);
		network.connect(c, d);
	}
	
	@Override
	public void draw() {
		background(255);
		network.update();
		network.display();
		if(frameCount % 30 == 0){
			network.feedforward(random(1));
		}
	}
	
	private class Connection{
		Neuron a;
		Neuron b;
		float weight;//权重
		
		PVector sender;
		boolean sending = false;
		float output;
		

		public Connection(Neuron from, Neuron to, float weight) {
			this.a = from;
			this.b = to;
			this.weight = weight;
		}
		
		public void display(){
			stroke(0);
			strokeWeight(weight * 4);
			line(a.location.x, a.location.y, b.location.x, b.location.y);
			if(sending){
				fill(0);
				strokeWeight(1);
				ellipse(sender.x, sender.y, 8, 8);
			}
		}

		public void feedforward(float val) {
			sending = true;
			sender = a.location.copy();
			output = val * weight;
		}
		
		public void update(){
			if(sending){
				sender.x = lerp(sender.x, b.location.x, 0.1f);
				sender.y = lerp(sender.y, b.location.y, 0.1f);
				
				float d = PVector.dist(sender, b.location);
				
				if(d < 1){
					b.feedforward(output);
					sending = false;
				}
			}
		}
	}
	
	private class Network{
		ArrayList<Neuron> neurons;
		PVector location;
		
		public Network(float x, float y) {
			location = new PVector(x, y);
			neurons = new ArrayList<>();
		}
		
		public void addNeuron(Neuron n){
			neurons.add(n);
		}
		
		public void connect(Neuron a, Neuron b){
			Connection c  = new Connection(a, b, random(1));
			a.addConnection(c);
		}
		
		public void feedforward(float input){
			Neuron start = neurons.get(0);
			start.feedforward(input);
		}
		
		public void update(){
			for(Neuron n : neurons){
				for(Connection c : n.connections){
					c.update();
				}
			}
		}
		
		public void display(){
			pushMatrix();
			translate(location.x, location.y);
			for(Neuron n : neurons){
				n.display();
			}
			popMatrix();
		}
	}
	
	private class Neuron{
		PVector location;
		ArrayList<Connection> connections;
		float sum = 0;
		
		public Neuron(float x, float y) {
			location = new PVector(x, y);
			connections = new ArrayList<>();
		}
		
		public void feedforward(float input) {
			sum += input;
			if(sum > 1){
				fire();
				sum = 0;
			}
		}

		private void fire() {
			for(Connection c : connections){
				c.feedforward(sum);
			}
		}

		public void addConnection(Connection c){
			connections.add(c);
		}
		
		public void display(){
			stroke(0);
			fill(0);
			ellipse(location.x, location.y, 16, 16);
			for(Connection c : connections){
				c.display();
			}
		}
	}
	
	public static void main(String[] args) {
		Start.start(NeuronTest.class);
	}
}
