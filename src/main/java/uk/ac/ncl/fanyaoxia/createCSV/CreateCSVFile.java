package uk.ac.ncl.fanyaoxia.createCSV;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import uk.ac.ncl.fanyaoxia.monitor.MonitorRecentUpdates;
import uk.ac.ncl.fanyaoxia.monitor.StoredArticle;
import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXmlRanking;

public class CreateCSVFile {
	private static Map<StoredArticle, ReadXmlRanking> historyFile;

	public CreateCSVFile() {
		historyFile = new HashMap<StoredArticle, ReadXmlRanking>();
	}

	public void createFile(String filename) {
		generateCsvFile(filename + ".csv");
	}

	private static void generateCsvFile(String sFileName) {
		MonitorRecentUpdates csvFile = new MonitorRecentUpdates();
		historyFile = csvFile.getMap();
		try {
			FileWriter writer = new FileWriter(sFileName);
			writer.append("Title");
			Random random = new Random();
			List<StoredArticle> keys = new ArrayList<StoredArticle>(
					historyFile.keySet());
			StoredArticle sa4 = keys.get(random.nextInt(keys.size()));

			for (String k3 : sa4.getTimeAndPopularity().keySet()) {
				writer.append(',');
				writer.append(k3);
				// System.out.println(k3);
			}

			writer.append('\n');

			for (StoredArticle sa3 : historyFile.keySet()) {
				writer.append(sa3.getStoredTitle());
				for (String k3 : sa3.getTimeAndPopularity().keySet()) {
					writer.append(',');
					writer.append(sa3.getTimeAndPopularity().get(k3).toString());
				}
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
