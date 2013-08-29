package uk.ac.ncl.fanyaoxia.webpagefetch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import uk.ac.ncl.fanyaoxia.Neo4jQuery.AddNodeOrRelationToIndex;
import uk.ac.ncl.fanyaoxia.Neo4jQuery.CreateIndex;
import uk.ac.ncl.fanyaoxia.Neo4jQuery.QueryNode;
import uk.ac.ncl.fanyaoxia.gui.RecentUpdatesGUI;
import uk.ac.ncl.fanyaoxia.gui.SearchTitleGUI;

public class ReadXmlRanking {

	private int guiIndicator = 0;
	private String setDateR;
	private String timestampR;
	private String revidR;
	private String titleR;
	private String uclimitR;
	private String rvlimitR;
	private int bitDepthR;
	private String parentidR;
	private String userR;
	private String commentsR;
	private String pageidR;
	private String prevRevIdR;
	private Map<String, URI> revisionMapR;
	private boolean hasUpdatesR = true;
	private QueryNode querynode;

	// private ArrayList<String> revidList;

	public ReadXmlRanking(){
		querynode=new QueryNode();
	}
	public void getPageInfoR(String title, String uclimit, String rvlimit,
			int bitDepth, String startdate) throws Exception {
		this.uclimitR = uclimit;
		this.rvlimitR = rvlimit;
		this.bitDepthR = bitDepth;
		this.titleR = title;
		setDateR = startdate;
		if (bitDepth > 0) {
			readXMLR(
					"http://en.wikipedia.org/w/api.php?action=query&titles="
							+ title
							+ "&prop=revisions&rvlimit="
							+ rvlimit
							+ "&rvprop=ids|user|timestamp|comment&format=xml&rvdir=newer&rvstart="
							+ startdate, bitDepth, uclimit, rvlimit);

		}

	}

	public void changeGuiIndicatorTo1() {
		guiIndicator=1;
	}
	public String getSetDateR() {
		return setDateR;
	}

	public String getTimeR() {
		return timestampR;
	}

	public void replaceDateR() {
		setDateR = timestampR;
	}

	public String getTitleR() {
		return titleR;
	}

	public String getuclimitR() {
		return uclimitR;
	}

	public String getrvlimitR() {
		return rvlimitR;
	}

	public int getbitDepthR() {
		return bitDepthR;
	}

	public String getrevidR() {
		return revidR;
	}

	public String getUserR() {
		return userR;
	}

	public String getCommentsR() {
		return commentsR;
	}

	public String getParentIdR() {
		return parentidR;
	}

	public String getPageidR() {
		return pageidR;
	}

	public String getPrevRevIdR() {
		return prevRevIdR;
	}

	public void insertPrevRevIdR(String t) {
		prevRevIdR = t;
	}

	public boolean getHasUpdatesR() {
		return hasUpdatesR;
	}

	public void insertTimeR(String t) {
		this.timestampR = t;
	}

	public void readXMLR(String fileName, int bitDepth, String uclimit,
			String rvlimit) throws Exception {
		SAXBuilder xbuild = new SAXBuilder();
		Document doc = xbuild.build(fileName);

		fetchInfoR(doc, bitDepth, uclimit, rvlimit);
	}

	/**
	 * 
	 */
	public void selectArticleTogetinfoR(String selectTitle) {
		titleR = selectTitle;
	}

	/**
	 * public static void parseDate() throws Exception{ SimpleDateFormat
	 * formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
	 * date1=formatter.format(setDate); Date date2=formatter.parse(getTime());
	 * System.out.println(date1); }
	 */

	public void fetchInfoR(Document doc, int bitDepth, String uclimit,
			String rvlimit) throws Exception {
		Element root = doc.getRootElement();
		List query = root.getChildren("query");
		bitDepth--;

		final AddNodeOrRelationToIndex add = new AddNodeOrRelationToIndex();
		CreateIndex createindex = new CreateIndex();

		if (CreateIndex.getIndexCreation() == true) {
			createindex.createIndex();
		}

		for (int i = 0; i < query.size(); i++) {
			Element queryElements = (Element) query.get(i);
			List pages = queryElements.getChildren("pages");

			for (int j = 0; j < pages.size(); j++) {
				Element pagesElements = (Element) pages.get(j);
				List page = pagesElements.getChildren("page");

				for (int k = 0; k < page.size(); k++) {
					Element pageElements = (Element) page.get(k);
					// if(pageElements.getAttribute("pageid") != null)
					// {
					pageidR = pageElements.getAttribute("pageid").getValue();
					// }
					titleR = pageElements.getAttribute("title").getValue();
					// System.out.println("pageid:" + pageid + "  title:" +
					// title);

					List revisions = pageElements.getChildren("revisions");
					revisionMapR = new HashMap<String, URI>();

					for (int m = 0; m < revisions.size(); m++) {
						Element revisionsElements = (Element) revisions.get(m);

						List rev = revisionsElements.getChildren("rev");

						for (int n = 0; n < rev.size(); n++) {
							
							

							Element revElements = (Element) rev.get(n);
							revidR = revElements.getAttribute("revid")
									.getValue();

							parentidR = revElements.getAttribute("parentid")
									.getValue();
							userR = revElements.getAttribute("user").getValue();
							timestampR = revElements.getAttribute("timestamp")
									.getValue();

							commentsR = new String();

							try {
								commentsR = revElements.getAttribute("comment")
										.getValue();
							} catch (Exception ecomment) {
								commentsR = "";
							}

							if (revidR.equals(prevRevIdR)) {
								System.out.println("  Title:" + titleR
										+ "  No Updates... ");

								hasUpdatesR = false;// used to check and
													// set the
													// popularity of
													// stored article
							} else {
								hasUpdatesR = true;

								System.out.println("Timestamp: " + timestampR
										+ "  Pageid: " + pageidR + "  Title:"
										+ titleR + " Revid:" + revidR
										+ " Parentid:" + parentidR + " User:"
										+ userR + " Comments:" + commentsR);
								
								String queryNode=querynode.query("node", "articleNodeIndex", "revid", prevRevIdR);
								
								System.out.println("\n"+queryNode+" "+setDateR+""+timestampR+"\n");
								
								
								if(queryNode==null){
									URI revNode = new URI("");
									revNode = add.createNode();
									add.addProperty(revNode, "Revid", revidR);
									add.addProperty(revNode, "Title", titleR);
									add.addNodeOrRelationshipToIndex("node",
											"articleNodeIndex", "revid", revidR,
											revNode.toString());
									
									URI commentNode = add.createNode();
									add.addProperty(commentNode, "Comments",
											commentsR);
									add.addProperty(commentNode, "TimeStamp",
											timestampR);
									
									URI userNode = add.createNode();
									add.addProperty(userNode, "Username", userR);
									
									URI relation1 = add.addRelationship(revNode,
											commentNode, "wasGeneratedBy", "{}");
									URI relation2 = add.addRelationship(
											commentNode, userNode,
											"wasAssociatedWith", "{}");
									
									
									add.addNodeOrRelationshipToIndex("node",
											"editedNodeIndex", "timestamp",
											timestampR, commentNode.toString());
									add.addNodeOrRelationshipToIndex("node",
											"userNodeIndex", "userName", userR,
											userNode.toString());
									add.addNodeOrRelationshipToIndex(
											"relationship", "wasGeneratedBy",
											"Relationname", "wasGeneratedBy",
											relation1.toString());
									add.addNodeOrRelationshipToIndex(
											"relationship", "wasAssociatedWith",
											"Relationname", "wasAssociatedWith",
											relation2.toString());
								}
								else{
									URI parentNode=new URI(queryNode);
									
									URI revNode = add.createNode();
									add.addProperty(revNode, "Revid", revidR);
									add.addProperty(revNode, "Title", titleR);
									
									URI commentNode = add.createNode();
									add.addProperty(commentNode, "Comments",
											commentsR);
									add.addProperty(commentNode, "TimeStamp",
											timestampR);
									
									URI userNode = add.createNode();
									add.addProperty(userNode, "Username", userR);
									
									URI relation1 = add.addRelationship(revNode,
											commentNode, "wasGeneratedBy", "{}");
									URI relation2 = add.addRelationship(
											commentNode, userNode,
											"wasAssociatedWith", "{}");
									URI relation3 = add.addRelationship(revNode,
											parentNode,
											"wasRevisionOf", "{}");
									
									add.addNodeOrRelationshipToIndex("node",
											"articleNodeIndex", "revid", revidR,
											revNode.toString());
									add.addNodeOrRelationshipToIndex("node",
											"editedNodeIndex", "timestamp",
											timestampR, commentNode.toString());
									add.addNodeOrRelationshipToIndex("node",
											"userNodeIndex", "userName", userR,
											userNode.toString());
									add.addNodeOrRelationshipToIndex(
											"relationship", "wasGeneratedBy",
											"Relationname", "wasGeneratedBy",
											relation1.toString());
									add.addNodeOrRelationshipToIndex(
											"relationship", "wasAssociatedWith",
											"Relationname", "wasAssociatedWith",
											relation2.toString());
									
									
									add.addNodeOrRelationshipToIndex(
											"relationship", "wasRevisionOf",
											"Relationname", "wasRevisionOf",
											relation3.toString());
								}
									
								prevRevIdR = revidR;
								
								
								if(guiIndicator==0){
								RecentUpdatesGUI.getRecentSearchResults();
								}
								if(guiIndicator==1){
									SearchTitleGUI.getTextArea().append("Timestamp: " + timestampR + "\n" + "Pageid: " + pageidR
											+ "\n" + "Title:" + titleR + "\n" + "Revid:" + revidR + "\n"
											+ "Parentid:" + parentidR + "\n" + "User:" + userR + "\n"
											+ "Comments:" + commentsR + "\n" +"\n");
									SearchTitleGUI.getTextArea().setCaretPosition(SearchTitleGUI.getTextArea().getDocument().getLength());
								}
								System.out.println(prevRevIdR);
								System.out.println();

							}

						}
					}
				}

			}
		}
	}
}
