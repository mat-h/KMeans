package dnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Processor {
	
	private final List<Point> pts; // 各点
	private List<Point> clusters; // 平均が入る
	
	private final Random rand = new Random(System.currentTimeMillis());
	
	private int gen = 0; // abbrev: generation
	private static final int MAX_GEN = 10;

	public Processor(List<Point> data) {
		// 初期化
		this.pts = data;

		// cluster中心位置初期化
		int dimension = data.get(0).size();
		clusters = new ArrayList<Point>(2);
		for (int i=0; i<2; i++) {
			Point p = new Point();
			for (int j=0; j<dimension; j++) {
				p.append(rand.nextDouble());
			}
			clusters.add(p);
		}
		
		// clusterを持たせる
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
		double[] total_resp = pts.stream().map(p -> p.getResp()).reduce(new double[pts.get(0).getResp().length], (arr1, arr2) -> addArray(arr1, arr2));
		clusters = pts.stream().map(p -> p.getContribution()).reduce(Point.getEmptyPoints(clusters.size()), (p1,p2) -> addPoints(p1,p2));
		IntStream.range(0, clusters.size()).forEach(i -> clusters.get(i).divideBy(total_resp[i]));
	}

	private double[] addArray(double[] arr1, double[] arr2) {
		return IntStream.range(0, arr1.length).mapToDouble(i -> arr1[i] + arr2[i]).toArray();
	}

	private List<Point> addPoints(List<Point> p1, List<Point> p2) {
		return IntStream.range(0, p1.size()).mapToObj(i -> Point.emptyPoint().add(p1.get(i)).add(p2.get(i))).collect(Collectors.toList());
	}
	
	public void dump() {
		// 標準出力にdump
		System.out.println("clusters state (time=" + gen + "): ");

		// 各点のクラスタ帰属度を出力
		IntStream.range(0, clusters.size()).forEach(i -> 
				System.out.println("cluster " + i + "'s total weight is " + 
				pts.stream().mapToDouble(p -> p.getResp()[i]).sum()
				+ ". center coordinates are " + clusters.get(i)));

		System.out.println(""); // 見やすくするため
	}

	public void desc() {
		System.out.println("-----------------------");
		System.out.println("number of points: " + pts.size());
		System.out.println("number of clusters: " + clusters.size());
		System.out.println("-----------------------");
	}

}
