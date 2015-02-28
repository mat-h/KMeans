package dnd.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dnd.model.Processor;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		List<Double> data = readCsv();
		Processor proc = new Processor(data);
		proc.desc();
		proc.iterate(true);
	}

	private static List<Double> readCsv() throws FileNotFoundException {
		List<Double> data = new ArrayList<Double>();
		Scanner scanner = new Scanner(new File("C:\\r\\data\\hoge.csv"));
		while(scanner.hasNextLine()) {
			if (!scanner.hasNextDouble()) break;
			
			data.add(scanner.nextDouble());
		}	
		scanner.close();
		return data;
	}

}
