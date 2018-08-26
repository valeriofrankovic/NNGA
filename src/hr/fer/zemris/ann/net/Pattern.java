package hr.fer.zemris.ann.net;

public class Pattern {

	private Dot point;

	public Pattern(Dot point) {
		this.point = point;
	}
	
	public double getX() {
		return point.getX();
	}

	public double getY() {
		return point.getY();
	}
}
