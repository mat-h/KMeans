package dnd.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dnd.model.Point;
import dnd.model.Processor;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		List<Point> data = readCsv();
		Processor proc = new Processor(data);
		proc.desc();
		proc.iterate(true);
	}

	private static List<Point> readCsv() throws FileNotFoundException {
		List<Point> data = new ArrayList<Point>();
		Scanner scanner = new Scanner(new File("C:\\r\\data\\hoge.csv"));
		while(scanner.hasNextLine()) {
		
			Point p = new Point();
			String line = scanner.nextLine();
			Scanner sub = new Scanner(line);
			sub.useDelimiter(",");
			while(sub.hasNextDouble()) {
				p.append(sub.nextDouble());
			}
			sub.close();
			
			data.add(p);
		}	
		scanner.close();
		return data;
	}

}
