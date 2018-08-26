package hr.fer.zemris.ann.net;

import java.awt.Point;
import java.util.ArrayList;

public class TrainingData {

	private ArrayList<Integer> topology;
	private ArrayList<Pattern> X;
	private ArrayList<Classes> y;
	
	public TrainingData() {
		this.topology = new ArrayList<Integer>();
		// popuni topologiju
		this.topology.add(2);
		// TU TREBA NADODAT BROJ NEURONA U SKRIVENOM SLOJU
		this.topology.add(3);
		this.topology.add(3);
		//this.topology.add(3);
		//
		// 8*2*(2+1) + 4*1*(8+1) + 3*1*(4+1)
		this.X = new ArrayList<Pattern>();
		this.y = new ArrayList<Classes>();
	}
	
	public void add(Dot point, int[] y) {
		this.X.add(new Pattern(point));
		this.y.add(new Classes(y));
	}
	
	public ArrayList<Integer> getTopology() {
		return this.topology;
	}
	
	public ArrayList<Pattern> getX() {
		return this.X;
	}
	
	public ArrayList<Classes> getY() {
		return this.y;
	}
	
	public int getSize() {
		return this.X.size();
	}
	
}
