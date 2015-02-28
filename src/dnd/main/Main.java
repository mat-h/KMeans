package dnd.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dnd.model.Processor;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner scanner = new Scanner(new File("C:\\r\\data\\hoge.csv"));
		List<Double> data = new ArrayList<Double>();
		while(scanner.hasNextLine()) {
			if (!scanner.hasNextDouble()) break;
			
			data.add(scanner.nextDouble());
		}
		
		Processor proc = new Processor(data);
		
		scanner.close();
	}

}
