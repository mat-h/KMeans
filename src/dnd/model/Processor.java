package dnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Processor {
	
	private final List<Point> pts; // 各点
	private final List<Integer> belong_to; // 各点がどのクラスタに属するか
	private List<Point> clusters; // 平均が入る
	
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
		pts.stream().forEach(p->p.setCluster(clusters));
	}

	public void iterate(boolean verbose) {
		for (gen=0; gen<MAX_GEN; gen++) {
			update();
			if (verbose) dump();
		}
	}

	private void update() {
		// 各点がどのクラスタに属するかを計算しなおす。
		pts.forEach(p -> p.updateResponsibilities()); 

		// クラスタの平均位置を計算しなおす。
		Double[] total_resp = pts.stream().map(p -> p.getResp()).reduce(new Double[pts.get(0).getResp().length], (arr1, arr2) -> addArray(arr1, arr2));
		clusters = pts.stream().map(p -> p.getContribution()).reduce(getEmptyPoints(clusters.size()), (p1,p2) -> addPoints(p1,p2));
		for (int i=0,l=clusters.size(); i<l; i++) {
			clusters.get(i).divideBy(total_resp[i]);
		}
	}

	private Double[] addArray(Double[] arr1, Double[] arr2) {
		Double[] arr = new Double[arr1.length];
		for (int i=0,l=arr1.length; i<l; i++) {
			arr[i] = ((arr1[i]==null)? 0: arr1[i]) + ((arr2[i]==null)? 0: arr2[i]);
		}
		return arr;
	}

	private List<Point> addPoints(List<Point> p1, List<Point> p2) {
		List<Point> arr = new ArrayList<Point>();
		for (int i=0,l=p1.size(); i<l; i++) {
			arr.add(Point.emptyPoint().add(p1.get(i)).add(p2.get(i)));
		}
		return arr;
	}

	private List<Point> getEmptyPoints(int size) {
		List<Point> arr = new ArrayList<Point>();
		for (int i=0; i<size; i++) {
			arr.add(Point.emptyPoint());
		}
		return arr;
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
