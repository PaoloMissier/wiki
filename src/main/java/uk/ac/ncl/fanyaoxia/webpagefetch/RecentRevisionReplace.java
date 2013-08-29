package uk.ac.ncl.fanyaoxia.webpagefetch;

import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import uk.ac.ncl.fanyaoxia.monitor.StoredArticle;

public class RecentRevisionReplace {

	private String recentChangesTitleR;
	private String rcidR;
	private String recentChangesPageIdR;
	private String recentChangesRevidR;
	private String recentChangesOldrevidR;
	private String recentChangesTimestampR;
	private ArrayList<StoredArticle> eliminateStore;
	
	public RecentRevisionReplace() {
		eliminateStore = new ArrayList<StoredArticle>();
	}

	/**
	 * 
	 */
	public void getRecentChangesR() throws Exception {

		readChangesXmlR("http://en.wikipedia.org/w/api.php?format=xml&action=query&list=recentchanges");

	}

	/**
	 * 
	 */

	public void readChangesXmlR(String fileName) throws Exception {
		SAXBuilder xbuild = new SAXBuilder();
		Document doc = xbuild.build(fileName);
		getChangesInfoR(doc);
	}

	/**
	 * 
	 */
	public String getRecentChangesTitleR() {
		return recentChangesTitleR;
	}

	public String getRcidR() {
		return rcidR;
	}

	public String getRecentChangesPageIdR() {
		return recentChangesPageIdR;
	}

	public String getRecentChangesRevidR() {
		return recentChangesRevidR;
	}

	public String getRecentChangesOldrevidR() {
		return recentChangesOldrevidR;
	}

	public String getRecentChangesTimestampR() {
		return recentChangesTimestampR;
	}

	public ArrayList<StoredArticle> getEliminateStore() {
		return eliminateStore;
	}

	/**
	 * 
	 */

	public void getChangesInfoR(Document doc) throws Exception {
		Element root = doc.getRootElement();
		List query = root.getChildren("query");
		eliminateStore=new ArrayList<StoredArticle>();
		
		for (int i = 0; i < query.size(); i++) {
			Element queryElements = (Element) query.get(i);
			List recentchanges = queryElements.getChildren("recentchanges");

			for (int j = 0; j < recentchanges.size(); j++) {
				Element recentChangesElements = (Element) recentchanges.get(j);
				final List rc = recentChangesElements.getChildren("rc");

				for (int o = 0; o < rc.size(); o++) {
					
					        	Element rcElements = (Element) rc.get(o);

								recentChangesTitleR = rcElements.getAttribute("title")
										.getValue();

								rcidR = rcElements.getAttribute("rcid").getValue();
								recentChangesPageIdR = rcElements.getAttribute("pageid")
										.getValue();
								recentChangesRevidR = rcElements.getAttribute("revid")
										.getValue();
								recentChangesOldrevidR = rcElements
										.getAttribute("old_revid").getValue();

								recentChangesTimestampR = rcElements.getAttribute(
										"timestamp").getValue();

								if(recentChangesPageIdR.equals("0") == false
										&& recentChangesRevidR.equals("0") == false && recentChangesOldrevidR
										.equals("0") == false){
									StoredArticle sa2 = new StoredArticle(
											recentChangesTitleR, recentChangesTimestampR);
									eliminateStore.add(sa2);
									System.out.println(sa2.getStoredTitle());
								}
					
				}
			}

		}
	}

}
