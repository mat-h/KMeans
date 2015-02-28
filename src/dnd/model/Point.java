package dnd.model;

import java.util.ArrayList;
import java.util.List;

public class Point {
	private List<Double> coord = new ArrayList<Double>();
	
	public int append(double newCoord) {
		coord.add(newCoord);
		return coord.size();
	}
	
	public int size() {
		return coord.size();
	}

	public void add(Point point) {
		if (size() < point.size()) {
			fillZero(point.size());
		}
		for (int i=0, l=coord.size(); i<l; i++) {
			coord.set(i, coord.get(i) + point.get(i));
		}
	}
	
	private void fillZero(int size) {
		for (int i=size(),l=size; i<l; i++) {
			coord.add(0.0);
		}
	}

	public double get(int index) {
		return coord.get(index);
	}

	public Point divideBy(double num) {
		for (int i=0, l=coord.size(); i<l; i++) {
			coord.set(i, coord.get(i)/num);
		}
		return this;
	}

	/**
	 * n次元ユークリッド距離を採用
	 * @param p
	 * @param point
	 * @return
	 */
	public Double distance(Point point) {
		double s = 0;
		for (int i=0, l=coord.size(); i<l; i++) {
			s += Math.pow(coord.get(i) - point.get(i), 2);
		}
		return Math.sqrt(s);
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		for (int i=0, l=coord.size(); i<l; i++) {
			b.append(coord.get(i));
			if (i!=l-1)	b.append(",");
		}
		b.append("]");
		return b.toString();
	}
}
