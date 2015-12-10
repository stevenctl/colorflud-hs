package com.sugarware.leaderboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Leaderboard {

	
	public int getMax(){
		return max;
	}
	
	public ArrayList<Score> getScores(){
		return scores;
	}
	
	private ArrayList<Score> scores;
	private final int max = 10;
	
	public Leaderboard() {
		scores = new ArrayList<Score>();
		File f = new File("scores.dat");
		try {
			Scanner sc =new Scanner(f);
			Score score = new Score();
			boolean s = false;
			while(sc.hasNext()){
				if(!s){
					score = new Score();
					score.name = sc.next();
					s = true;
				}else{
					score.score = sc.nextInt();
					scores.add(score);
					s = false;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public void add(String n, int s){
		scores.add(new Score(n,s));
		scores.sort(new ScoreComparator());
		
		while(scores.size() > max){
			scores.remove(scores.size() -1);
		}
		
	}

}
