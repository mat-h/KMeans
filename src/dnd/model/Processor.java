package dnd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Processor {
	
	private final List<Point> pts; // �e�_
	private List<Point> clusters; // ���ς�����
	
	private final Random rand = new Random(System.currentTimeMillis());
	
	private int gen = 0; // abbrev: generation
	private static final int MAX_GEN = 10;

	public Processor(List<Point> data) {
		// ������
		this.pts = data;

		// cluster���S�ʒu������
		int dimension = data.get(0).size();
		clusters = new ArrayList<Point>(2);
		for (int i=0; i<2; i++) {
			Point p = new Point();
			for (int j=0; j<dimension; j++) {
				p.append(rand.nextDouble());
			}
			clusters.add(p);
		}
		
		// cluster����������
		pts.stream().forEach(p->p.setCluster(clusters));
	}

	public void iterate(boolean verbose) {
		for (gen=0; gen<MAX_GEN; gen++) {
			update();
			if (verbose) dump();
		}
	}

	private void update() {
		// �e�_���ǂ̃N���X�^�ɑ����邩���v�Z���Ȃ����B
		pts.forEach(p -> p.updateResponsibilities()); 

		// �N���X�^�̕��ψʒu���v�Z���Ȃ����B
		Double[] total_resp = pts.stream().map(p -> p.getResp()).reduce(new Double[pts.get(0).getResp().length], (arr1, arr2) -> addArray(arr1, arr2));
		clusters = pts.stream().map(p -> p.getContribution()).reduce(Point.getEmptyPoints(clusters.size()), (p1,p2) -> addPoints(p1,p2));
		IntStream.range(0, clusters.size()).forEach(i -> clusters.get(i).divideBy(total_resp[i]));
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
	
	public void dump() {
		// �W���o�͂�dump
		System.out.println("clusters state (time=" + gen + "): ");
		for (int i=0; i<clusters.size(); i++) {
			// �e�_�̃N���X�^�A���x���o��
			final int i_ = i;
			double total_weight = pts.stream().map(p -> p.getResp()[i_]).reduce(0.0, Double::sum);
			System.out.println("cluster " + i + "'s total weight is " + total_weight + ". center coordinates are " + clusters.get(i));
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
