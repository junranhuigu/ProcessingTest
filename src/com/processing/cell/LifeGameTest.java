package com.processing.cell;

import processing.core.PApplet;
import processing.event.MouseEvent;

import com.processing.Start;

public class LifeGameTest extends PApplet{
	private int[][] board;
	private int length = 5;
	private int count = 13;
	
	@Override
	public void settings() {
		size(800, 800);
		int col = width / length;
		int row = height / length;
		board = new int[col][row];
		for(int i = 0; i < col; ++ i){
			for(int j = 0; j < row; ++ j){
				board[i][j] = 0;
			}
		}
		int w = count;
		for(int i = -w; i <= w; ++ i){
			for(int j = -w; j <= w; ++ j){
				board[col / 2 + i][row / 2 + j] = 1;
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		if(event != null){
			++ count;
			int col = width / length;
			int row = height / length;
			int w = count;
			for(int i = 0; i < col; ++ i){
				for(int j = 0; j < row; ++ j){
					board[i][j] = 0;
				}
			}
			for(int i = -w; i <= w; ++ i){
				for(int j = -w; j <= w; ++ j){
					board[col / 2 + i][row / 2 + j] = 1;
				}
			}
		}
	}
	
	@Override
	public void draw() {
		int col = width / length;
		int row = height / length;
		for(int i = 0; i < col; ++ i){
			for(int j = 0; j < row; ++ j){
				if(board[i][j] == 1){
					fill(0);
				} else {
					fill(255);
				}
				
				stroke(0);
				rect(i * length, j * length, length, length);
			}
		}
		fill(0);
		text(count, 0, 10);
		try {
			Thread.sleep(20);
		} catch (Exception e) {}
		next();
	}
	
	private void next(){
		int col = width / length;
		int row = height / length;
		int[][] next = new int[col][row];
		for(int x = 1; x < col - 1; ++ x){
			for(int y = 1; y < row - 1; ++ y){
				int neighbors = 0;
				
				for(int i = -1; i <= 1; ++ i){
					for(int j = -1; j <= 1; ++ j){
						neighbors += board[x + i][y + j];
					}
				}
				
				neighbors -= board[x][y];
				
				if(board[x][y] == 1 && neighbors < 2){
					next[x][y] = 0;
				} else if(board[x][y] == 1 && neighbors > 3){
					next[x][y] = 0;
				} else if(board[x][y] == 0 && neighbors == 3){
					next[x][y] = 1;
				} else {
					next[x][y] = board[x][y];
				}
			}
		}
		
		board = next;
	}
	
	public static void main(String[] args) {
		Start.start(LifeGameTest.class);
	}
}
