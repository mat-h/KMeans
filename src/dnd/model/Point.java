package dnd.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Point {
	private static final double BETA = 100;
	private List<Double> coord = new ArrayList<Double>();
	private Double[] resp = new Double[0]; // Responsibility = クラスタへの帰属度
	private List<Point> clusters; // 外から渡す
	
	public int append(double newCoord) {
		coord.add(newCoord);
		return coord.size();
	}
	
	public int size() {
		return coord.size();
	}

	public Point add(Point point) {
		if (size() < point.size()) {
			fillZero(point.size());
		}
		for (int i=0, l=coord.size(); i<l; i++) {
			coord.set(i, coord.get(i) + point.get(i));
		}
		return this;
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

	public void updateResponsibilities() {
		// 各クラスタとの距離を計算し、responsibilitiesに変換する
		resp = clusters.stream().map(p -> Double.valueOf(exponential(this.distance(p)))).collect(Collectors.toList()).toArray(new Double[0]);
		double sum = Arrays.asList(resp).stream().reduce(0.0, Double::sum);
		resp = Arrays.asList(resp).stream().map(r -> Double.valueOf(r/sum)).collect(Collectors.toList()).toArray(new Double[0]);
	}
	
	public Double[] getResp() {
		return resp;
	}

	public double exponential(double x) {
		return Math.exp((-1.0) * BETA * x);
	}

	/**
	 * 各クラスタに貢献ベクトルを返す。
	 * @return size=clusters.size()
	 */
	public List<Point> getContribution() {
		double sum = Arrays.asList(resp).stream().reduce(0.0, Double::sum);
		return Arrays.asList(resp).stream().map(r -> this.multiply(r/sum)).collect(Collectors.toList());
	}

	private Point multiply(double d) {
		Point p = emptyPoint();
		coord.stream().forEach(x -> p.append(x*d));
		return p;
	}

	public static Point emptyPoint() {
		return new Point();
	}

	public void setCluster(List<Point> clusters) {
		this.clusters = clusters;
	}

	public static List<Point> getEmptyPoints(int size) {
		List<Point> arr = new ArrayList<Point>();
		for (int i=0; i<size; i++) {
			arr.add(Point.emptyPoint());
		}
		return arr;
	}
}
