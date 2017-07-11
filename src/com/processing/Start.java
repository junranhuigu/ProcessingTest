package com.processing;

import processing.core.PApplet;

public class Start {
	public static void start(Class<? extends PApplet> cls){
		String[] ss = new String[]{ "--present", cls.getName()};
		PApplet.main(ss);
	}
}
