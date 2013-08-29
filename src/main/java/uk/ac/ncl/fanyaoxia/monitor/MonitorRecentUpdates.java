package uk.ac.ncl.fanyaoxia.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JTextArea;

import uk.ac.ncl.fanyaoxia.Neo4jQuery.CreateIndex;
import uk.ac.ncl.fanyaoxia.gui.MonitorRecentUpdatesGUI;
import uk.ac.ncl.fanyaoxia.gui.RecentUpdatesGUI;
import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXmlRanking;
import uk.ac.ncl.fanyaoxia.webpagefetch.RecentRevision;
import uk.ac.ncl.fanyaoxia.webpagefetch.RecentRevisionReplace;

public class MonitorRecentUpdates {
	private ArrayList<StoredArticle> passArticle;
	private static Map<StoredArticle, ReadXmlRanking> monitorCombination;
	private RecentRevision rr1;
	private int indexCreationCheck = 0;

	public MonitorRecentUpdates() {
		rr1 = RecentUpdatesGUI.getRecentRevision();
		passArticle = rr1.getStoredArticle();
	}

	public void continueMonitor() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		JTextArea jt = new JTextArea();
		jt = MonitorRecentUpdatesGUI.getMonitorTextArea();
		jt.append("Monitoring Time: " + dateFormat.format(date) + "\n");
		jt.setCaretPosition(jt.getDocument().getLength());

		if (monitorCombination == null) {
			monitorCombination = new HashMap<StoredArticle, ReadXmlRanking>();
		}
		if (monitorCombination.isEmpty()) {
			for (StoredArticle sa : passArticle) {// check each stored
													// article
													// in
													// array
				ReadXmlRanking rx1 = new ReadXmlRanking();// new ReadXml for each recent
											// update
											// article
				monitorCombination.put(sa, rx1);
			}
		}
		checkMaxPopularity();
		for (StoredArticle sa : monitorCombination.keySet()) {

			if (sa.getInitialTime() != null) {
				
				//monitorCombination.get(sa).changeGuiIndicatorTo1();
				monitorCombination.get(sa)
						.getPageInfoR(sa.getStoredTitle(), "50", "50", 50,
								sa.getInitialTime());
				CreateIndex.indexCreated();
				monitorCombination.get(sa).insertTimeR(sa.getInitialTime());
				monitorCombination.get(sa).replaceDateR();
				sa.emptyTime();
				
				if (indexCreationCheck == 0) {
					JTextArea jt4 = new JTextArea();
					jt4 = MonitorRecentUpdatesGUI.getMonitorTextArea();
					jt4.append("\n" + "Pass articles to monitor successful!!"
							+ "\n");
					jt4.setCaretPosition(jt.getDocument().getLength());
					indexCreationCheck = 1;
				}
				JTextArea jt5 = new JTextArea();
				jt5 = MonitorRecentUpdatesGUI.getMonitorTextArea();
				jt5.append("---" + sa.getStoredTitle() + "-No Updates..."
						+ "\n" + "   Popularity: " + sa.getPopularity()
						+ "\n");
				jt5.setCaretPosition(jt5.getDocument().getLength());

			} else {
				//monitorCombination.get(sa).changeGuiIndicatorTo1();
				monitorCombination.get(sa).replaceDateR();
				monitorCombination.get(sa)
						.getPageInfoR(sa.getStoredTitle(), "50", "50", 50,
								monitorCombination.get(sa).getSetDateR());

				if (monitorCombination.get(sa).getHasUpdatesR() == false) {
					sa.noUpdates();
					JTextArea jt2 = new JTextArea();
					jt2 = MonitorRecentUpdatesGUI.getMonitorTextArea();
					jt2.append("---" + sa.getStoredTitle() + "-No Updates..."
							+ "\n" + "   Popularity: " + sa.getPopularity()
							+ "\n");
					jt2.setCaretPosition(jt2.getDocument().getLength());
					sa.addTimeAndPopularity(dateFormat.format(date).toString(),
							sa.getPopularity());
				}
				if (monitorCombination.get(sa).getHasUpdatesR() == true) {
					// sa.hasupdates();
					JTextArea jt3 = new JTextArea();
					jt3 = MonitorRecentUpdatesGUI.getMonitorTextArea();
					jt3.append("---" + sa.getStoredTitle()
							+ "-Has new Updates..." + "\n" + "   Popularity: "
							+ sa.getPopularity() + "\n");
					jt3.setCaretPosition(jt3.getDocument().getLength());
					sa.addTimeAndPopularity(dateFormat.format(date).toString(),
							sa.getPopularity());
				}
			}

		}
		System.out.println("*********");
		// printAllResults();
		MonitorRecentUpdatesGUI.getLineSplit();
	}

	/**
	 * print the popularity history results of each article.
	 */
	public void printAllResults() {
		for (StoredArticle sa2 : monitorCombination.keySet()) {
			System.out.println(sa2.getStoredTitle());

			for (int i = 0; i < sa2.getTimeAndPopularity().size(); i++) {

				String key = (String) sa2.getTimeAndPopularity().keySet()
						.toArray()[i];

				int value = (Integer) sa2.getTimeAndPopularity().values()
						.toArray()[i];

				System.out.println("Monitoring Time: " + key + "  Popularity: "
						+ value);
			}

		}
		MonitorRecentUpdatesGUI.getResultsTable();
	}

	public static Map<StoredArticle, ReadXmlRanking> getMap() {
		return monitorCombination;
	}

	/**
	 * Check which article's popularity reaches 5, then this article will be
	 * eliminated and replaced by a new recent updated article
	 * 
	 * @throws Exception
	 */
	private void checkMaxPopularity() throws Exception {
		for (StoredArticle ckeckMax : new HashSet<StoredArticle>(
				monitorCombination.keySet())) {
			if (ckeckMax.getTimeAndPopularity().containsValue(5)) {
				monitorCombination.remove(ckeckMax);

				JTextArea jt4 = new JTextArea();
				jt4 = MonitorRecentUpdatesGUI.getMonitorTextArea();
				jt4.append("\n" + "-[ " + ckeckMax.getStoredTitle() + " ]"
						+ " has been Removed" + "\n");
				jt4.setCaretPosition(jt4.getDocument().getLength());
				System.out.print("\n" + "[ " + ckeckMax.getStoredTitle() + " ]"
						+ " has been Removed" + "\n");

				RecentRevisionReplace rr2 = new RecentRevisionReplace();
				Thread.sleep(2000, 0);
				rr2.getRecentChangesR();
				ArrayList<StoredArticle> eliminateArray = rr2
						.getEliminateStore();
				StoredArticle item = eliminateArray.get(0);

				ReadXmlRanking newReadXml = new ReadXmlRanking();
				monitorCombination.put(item, newReadXml);

				JTextArea jt5 = new JTextArea();
				jt5 = MonitorRecentUpdatesGUI.getMonitorTextArea();
				jt5.append("-[ " + item.getStoredTitle() + " ]"
						+ " has been added" + "\n");
				jt5.setCaretPosition(jt5.getDocument().getLength());

				System.out.println("[ " + item.getStoredTitle() + " ]"
						+ " has been added" + "\n");

			}

		}
	}
}
