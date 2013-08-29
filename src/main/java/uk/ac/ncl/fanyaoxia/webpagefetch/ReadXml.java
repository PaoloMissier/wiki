package uk.ac.ncl.fanyaoxia.webpagefetch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import uk.ac.ncl.fanyaoxia.Neo4jQuery.AddNodeOrRelationToIndex;
import uk.ac.ncl.fanyaoxia.Neo4jQuery.CreateIndex;
import uk.ac.ncl.fanyaoxia.gui.RecentUpdatesGUI;
import uk.ac.ncl.fanyaoxia.gui.SearchTitleGUI;

public class ReadXml {

	private int resultType = 3;
	private String setDate;
	private String timestamp;
	private String revid;
	private String title;
	private String uclimit;
	private String rvlimit;
	private int bitDepth;
	private String parentid;
	private String user;
	private String comments;
	private String pageid;
	private String prevRevId;
	private Map<String, URI> revisionMap;
	private boolean hasUpdates = true;
	
	private ReadXmlRanking ReadXmlForloop;
	private Timer timer;

	// private ArrayList<String> revidList;
	public ReadXml(){
		ReadXmlForloop=new ReadXmlRanking();
	}
	
	public void getPageInfo(String title, String uclimit, String rvlimit,
			int bitDepth, String startdate) throws Exception {
		this.uclimit = uclimit;
		this.rvlimit = rvlimit;
		this.bitDepth = bitDepth;
		this.title = title;
		setDate = startdate;
		prevRevId = null;
		if (bitDepth > 0) {
			readXML("http://en.wikipedia.org/w/api.php?action=query&titles="
					+ title
					+ "&prop=revisions&rvlimit="
					+ rvlimit
					+ "&rvprop=ids|user|timestamp|comment&format=xml&rvdir=newer&rvstart="
					+ startdate, bitDepth, uclimit, rvlimit);

		}

	}

	public void changeResultType1() {
		resultType = 1;
	}

	public void changeResultType0() {
		resultType = 0;
	}

	public String getSetDate() {
		return setDate;
	}

	public String getTime() {
		return timestamp;
	}

	public void replaceDate() {
		setDate = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public String getuclimit() {
		return uclimit;
	}

	public String getrvlimit() {
		return rvlimit;
	}

	public int getbitDepth() {
		return bitDepth;
	}

	public String getrevid() {
		return revid;
	}

	public String getUser() {
		return user;
	}

	public String getComments() {
		return comments;
	}

	public String getParentId() {
		return parentid;
	}

	public String getPageid() {
		return pageid;
	}

	public String getPrevRevId() {
		return prevRevId;
	}

	public boolean getHasUpdates() {
		return hasUpdates;
	}

	public void insertTime(String t) {
		this.timestamp = t;
	}

	public void readXML(String fileName, int bitDepth, String uclimit,
			String rvlimit) throws Exception {
		SAXBuilder xbuild = new SAXBuilder();
		Document doc = xbuild.build(fileName);

		fetchInfo(doc, bitDepth, uclimit, rvlimit);
	}

	/**
	 * 
	 */
	public void selectArticleTogetinfo(String selectTitle) {
		title = selectTitle;
	}
	public Timer getTimer(){
		return timer;
	}

	/**
	 * public static void parseDate() throws Exception{ SimpleDateFormat
	 * formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
	 * date1=formatter.format(setDate); Date date2=formatter.parse(getTime());
	 * System.out.println(date1); }
	 */

	public void fetchInfo(Document doc, int bitDepth, String uclimit,
			String rvlimit) throws Exception {
		Element root = doc.getRootElement();
		List query = root.getChildren("query");
		bitDepth--;

		final AddNodeOrRelationToIndex add = new AddNodeOrRelationToIndex();
		CreateIndex createindex = new CreateIndex();

		if (CreateIndex.getIndexCreation()== true) {
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
					pageid = pageElements.getAttribute("pageid").getValue();
					// }
					title = pageElements.getAttribute("title").getValue();
					// System.out.println("pageid:" + pageid + "  title:" +
					// title);
					

					List revisions = pageElements.getChildren("revisions");
					revisionMap = new HashMap<String, URI>();
					//System.out.println(revisions.size());
					if(revisions.size()==0){
						SearchTitleGUI.getTextArea().append("No revisions found, please type an earlier date"+"\n"+"\n");
						SearchTitleGUI.getTextArea().setCaretPosition(SearchTitleGUI.getTextArea().getDocument().getLength());
						
					}
					else{
					for (int m = 0; m < revisions.size(); m++) {
						Element revisionsElements = (Element) revisions.get(m);

						final List rev = revisionsElements.getChildren("rev");
						// this has been modified to solve relation issue
						// for (int n = 0; n < rev.size(); n++) {
						
						
						timer = new Timer(5000, new ActionListener() {
							private int n = 0;

							public void actionPerformed(ActionEvent e) {
								
								if (n>=rev.size()) {
									if(resultType==0){									
									try {
										SearchTitleGUI.printNoupdates();
										ReadXmlForloop.changeGuiIndicatorTo1();
										ReadXmlForloop.replaceDateR();
										ReadXmlForloop.getPageInfoR(getTitle(),getuclimit(),getrvlimit(),getbitDepth(),ReadXmlForloop.getSetDateR());
									} catch (Exception e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									}
									
								} else{
									Element revElements = (Element) rev.get(n);
									revid = revElements.getAttribute("revid")
											.getValue();

									parentid = revElements.getAttribute(
											"parentid").getValue();
									user = revElements.getAttribute("user")
											.getValue();
									timestamp = revElements.getAttribute(
											"timestamp").getValue();

									comments = new String();

									try {
										comments = revElements.getAttribute(
												"comment").getValue();
									} catch (Exception ecomment) {
										comments = "";
									}
									// revidList.add(n, getrevid());

									if (revid.equals(prevRevId)) {
										//SearchTitleGUI.printNoupdates();
										hasUpdates = false;// used to check and
															// set the
															// popularity of
															// stored article
									} else {
										hasUpdates = true;
										try {
											System.out.println("Timestamp: "
													+ timestamp + "  Pageid: "
													+ pageid + "  Title:"
													+ title + " Revid:" + revid
													+ " Parentid:" + parentid
													+ " User:" + user
													+ " Comments:" + comments);

											URI revNode = add.createNode();
											add.addProperty(revNode, "Revid",
													revid);
											add.addProperty(revNode, "Title",
													title);
											
											URI commentNode = add.createNode();
											add.addProperty(commentNode,
													"Comments", comments);
											add.addProperty(commentNode,
													"TimeStamp", timestamp);
											
											URI userNode = add.createNode();
											add.addProperty(userNode,
													"Username", user);
											
											URI relation1 = add
													.addRelationship(revNode,
															commentNode,
															"wasGeneratedBy",
															"{}");
											URI relation2 = add
													.addRelationship(
															commentNode,
															userNode,
															"wasAssociatedWith",
															"{}");
											
											add.addNodeOrRelationshipToIndex(
													"node", "articleNodeIndex",
													"revid", revid,
													revNode.toString());
											add.addNodeOrRelationshipToIndex(
													"node", "editedNodeIndex",
													"timestamp", timestamp,
													commentNode.toString());
											add.addNodeOrRelationshipToIndex(
													"node", "userNodeIndex",
													"userName", user,
													userNode.toString());
											add.addNodeOrRelationshipToIndex(
													"relationship",
													"wasGeneratedBy",
													"Relationname",
													"wasGeneratedBy",
													relation1.toString());
											add.addNodeOrRelationshipToIndex(
													"relationship",
													"wasAssociatedWith",
													"Relationname",
													"wasAssociatedWith",
													relation2.toString());

											if (revisionMap.size() > 0) {
												URI relation3;

												relation3 = add
														.addRelationship(
																revNode,
																revisionMap
																		.get(prevRevId),
																"wasRevisionOf",
																"{}");
												add.addNodeOrRelationshipToIndex(
														"relationship",
														"wasRevisionOf",
														"Relationname",
														"wasRevisionOf",
														relation3.toString());

											}
											revisionMap.put(revid, revNode);
											prevRevId = revid;
											ReadXmlForloop.insertPrevRevIdR(prevRevId);
											ReadXmlForloop.insertTimeR(getTime());

											
										} catch (URISyntaxException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}

										if (resultType == 0) {
											SearchTitleGUI.getStringText();
						
										}

										if (resultType == 1) {
											RecentUpdatesGUI
													.getRecentSearchResults();
										}

										System.out.println(prevRevId);
										System.out.println();
										// System.out.println(revidList);
									}

									n++;
								}
							}
						});
						timer.start();
						
						// }

					}
					}
				}
			}

		}
	}

}
