package dnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Processor {
	
	private final List<Point> pts; // 各点
	private final List<Integer> belong_to; // 各点がどのクラスタに属するか
	private final List<Point> clusters; // 平均が入る
	
	private final Random rand = new Random(System.currentTimeMillis());
	
	private int gen = 0; // abbrev: generation
	private static final int MAX_GEN = 10;

	public Processor(List<Point> data) {
		this.pts = data;
		belong_to = new ArrayList<Integer>();
		for (int i=0, l=data.size(); i<l; i++) {
			belong_to.add(0);
		}
		
		int dimension = data.get(0).size();
		clusters = new ArrayList<Point>(2);
		for (int i=0; i<2; i++) {
			Point p = new Point();
			for (int j=0; j<dimension; j++) {
				p.append(rand.nextDouble());
			}
			clusters.add(p);
		}
	}

	public void iterate(boolean verbose) {
		for (gen=0; gen<MAX_GEN; gen++) {
			update();
			if (verbose) dump();
		}
	}

	private void update() {
		// 各点がどのクラスタに属するかを計算しなおす。
		// belong_toをアップデートする
		for (int i=0; i<pts.size(); i++) {
			double[] distances = distances(i);
			double max = 0;
			int max_idx = 0;
			for (int j=0; j<clusters.size(); j++) {
				if (distances[j] > max) {
					max = distances[j];
					max_idx = j;
				}
			}
			belong_to.set(i, max_idx);
		}
		
		// クラスタの平均位置を計算しなおす。
		// clustersをアップデートする
		for (int i=0; i<clusters.size(); i++) {
			// pts.stream().filter(p -> p > )
			Point sum = new Point();
			double num = 0;
			for (int j=0; j<pts.size(); j++) {
				// このクラスタに属するなら平均に加算
				if (belong_to.get(j) == i) {
					sum.add(pts.get(j));
					num++;
				}
			}
			if (num==0) continue; // 0このときはアップデートしない
			clusters.set(i, sum.divideBy(num));
		}
	}

	private double[] distances(int i) {
		return clusters.stream().mapToDouble(p -> distance(p, pts.get(i))).toArray();
	}

	private Double distance(Point p, Point point) {
		return p.distance(point);
	}
	
	public void dump() {
		// 標準出力にdump
		System.out.println("clusters state (time=" + gen + "): ");
		int count;
		for (int i=0; i<clusters.size(); i++) {
			count = 0;
			for (int j = 0; j < pts.size(); j++) {
				if (belong_to.get(j) == i) {
					count++;
				}
			}
			System.out.println("cluster " + i + " contains: " + count + " points. center coordinates: " + clusters.get(i));
		}
		System.out.println("");
	}

	public void desc() {
		System.out.println("-----------------------");
		System.out.println("number of points: " + pts.size());
		System.out.println("number of clusters: " + clusters.size());
		System.out.println("-----------------------");
	}

}
