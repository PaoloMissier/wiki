package uk.ac.ncl.fanyaoxia.webpagefetch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import uk.ac.ncl.fanyaoxia.gui.RecentUpdatesGUI;
import uk.ac.ncl.fanyaoxia.monitor.MonitorRecentUpdates;
import uk.ac.ncl.fanyaoxia.monitor.StoredArticle;

public class RecentRevision {

	private String recentChangesTitle;
	private String rcid;
	private String recentChangesPageId;
	private String recentChangesRevid;
	private String recentChangesOldrevid;
	private String recentChangesTimestamp;
	private ArrayList<StoredArticle> articleStore;
	
	private Timer timer2;

	public RecentRevision() {
		articleStore = new ArrayList<StoredArticle>();
	}

	/**
	 * 
	 */
	public void getRecentChanges() throws Exception {

		readChangesXml("http://en.wikipedia.org/w/api.php?format=xml&action=query&list=recentchanges");

	}

	/**
	 * 
	 */

	public void readChangesXml(String fileName) throws Exception {
		SAXBuilder xbuild = new SAXBuilder();
		Document doc = xbuild.build(fileName);
		getChangesInfo(doc);
	}

	/**
	 * 
	 */
	public String getRecentChangesTitle() {
		return recentChangesTitle;
	}

	public String getRcid() {
		return rcid;
	}

	public String getRecentChangesPageId() {
		return recentChangesPageId;
	}

	public String getRecentChangesRevid() {
		return recentChangesRevid;
	}

	public String getRecentChangesOldrevid() {
		return recentChangesOldrevid;
	}

	public String getRecentChangesTimestamp() {
		return recentChangesTimestamp;
	}

	public ArrayList<StoredArticle> getStoredArticle() {
		return articleStore;
	}

	/**
	 * 
	 */

	public void getChangesInfo(Document doc) throws Exception {
		Element root = doc.getRootElement();
		List query = root.getChildren("query");
		
		for (int i = 0; i < query.size(); i++) {
			Element queryElements = (Element) query.get(i);
			List recentchanges = queryElements.getChildren("recentchanges");

			for (int j = 0; j < recentchanges.size(); j++) {
				Element recentChangesElements = (Element) recentchanges.get(j);
				final List rc = recentChangesElements.getChildren("rc");

				//for (int o = 0; o < rc.size(); o++) {
					
					timer2 = new Timer(1000, new ActionListener() {
					      private int o = 0;
					      public void actionPerformed(ActionEvent e) {
					        if (o >= rc.size()) {
					          timer2.stop();
					          RecentUpdatesGUI.getSuccessMessage();
					        } else {
					        	Element rcElements = (Element) rc.get(o);

								recentChangesTitle = rcElements.getAttribute("title")
										.getValue();

								rcid = rcElements.getAttribute("rcid").getValue();
								recentChangesPageId = rcElements.getAttribute("pageid")
										.getValue();
								recentChangesRevid = rcElements.getAttribute("revid")
										.getValue();
								recentChangesOldrevid = rcElements
										.getAttribute("old_revid").getValue();

								recentChangesTimestamp = rcElements.getAttribute(
										"timestamp").getValue();

								if (recentChangesPageId.equals("0") == false
												&& recentChangesRevid.equals("0") == false && recentChangesOldrevid
												.equals("0") == false) {
									StoredArticle sa = new StoredArticle(
											recentChangesTitle, recentChangesTimestamp);
									articleStore.add(sa);
									RecentUpdatesGUI.getRecentUpdatesResults();

									System.out.println("Title: " + recentChangesTitle
											+ "\n" + "RecentChangesPageId: "
											+ recentChangesPageId + "\n"
											+ "Recentchanges ID to patrol: " + rcid + "\n"
											+ "RecentChangesRevid: " + recentChangesRevid
											+ "\n" + "RecentChangesOldrevid: "
											+ recentChangesOldrevid + "\n"
											+ "RecentChangesTimestamp: "
											+ recentChangesTimestamp);
									System.out.println();
								}

					         
					          o++;
					         }
					      }
					   });
					timer2.start();
					
				//}
			}

		}
	}

	public void removeAllStoredArticle() {
		MonitorRecentUpdates.getMap().clear();
		for (ReadXmlRanking r : MonitorRecentUpdates.getMap().values()) {
			System.out.println(r);
		}
		for (StoredArticle a : articleStore) {
			System.out.println(a);
		}
	}

}
