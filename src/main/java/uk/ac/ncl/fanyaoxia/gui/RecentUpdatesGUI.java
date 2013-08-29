package uk.ac.ncl.fanyaoxia.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXml;
import uk.ac.ncl.fanyaoxia.webpagefetch.RecentRevision;

public class RecentUpdatesGUI extends JPanel implements ActionListener {

	static JTextArea recentUpdatesResults;
	static JTextArea popupTextArea;
	static JTextArea numberOfStoredArticle;
	static RecentRevision rr;
	JButton clearResults;
	JFrame popupResults;
	JButton searchByTitle;
	JScrollPane sbr1;
	JScrollPane sbr2;
	JButton showUpdatesButton;
	JButton monitorAllarticles;
	JTextField typeTitleField;
	JTextField typeTimeField;
	static ReadXml recentDeepSearch;
	private static int removeCheck = 0;
	private String initialNumber = "0";
	private JScrollPane sbr3;

	public RecentUpdatesGUI() {
		rr = new RecentRevision();
		
		JPanel rp1 = new JPanel();
		rp1.setLayout(new BoxLayout(rp1, BoxLayout.Y_AXIS));
		rp1.setBorder(BorderFactory.createEtchedBorder());

		JPanel rp2 = new JPanel();
		rp2.setLayout(new GridLayout());
		JPanel rp3 = new JPanel();
		rp3.setLayout(new BoxLayout(rp3, BoxLayout.Y_AXIS));
		JPanel rp5 = new JPanel();
		rp5.setLayout(new GridLayout(0, 2));
		JPanel rp6 = new JPanel();
		rp6.setLayout(new GridLayout());

		JPanel rp7 = new JPanel();
		rp7.setLayout(new GridLayout(0, 2));

		JLabel l1 = new JLabel("Generate the recent updates of Wikipedia");
		JLabel l2 = new JLabel("Title");
		JLabel l3 = new JLabel("Start Time");
		JLabel l4 = new JLabel("Number of stored articles");

		showUpdatesButton = new JButton("Start");
		// add(showUpdatesButton);
		showUpdatesButton.addActionListener(this);

		clearResults = new JButton("Clear");
		clearResults.addActionListener(this);

		searchByTitle = new JButton("Search");
		// add(searchByTitle);
		searchByTitle.addActionListener(this);

		recentUpdatesResults = new JTextArea(28, 38);
		recentUpdatesResults.setLineWrap(true);
		recentUpdatesResults.setEditable(false);
		popupTextArea = new JTextArea(30, 35);
		popupTextArea.setLineWrap(true);
		popupTextArea.setEditable(false);
		sbr2 = new JScrollPane(popupTextArea);
		sbr2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbr1 = new JScrollPane(recentUpdatesResults);
		sbr1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// textField1.addActionListener(this);

		typeTitleField = new JTextField();
		typeTimeField = new JTextField("eg:20130722000000");
		// add(typeTitleField);

		numberOfStoredArticle = new JTextArea(initialNumber);
		numberOfStoredArticle.setEditable(false);
		sbr3 = new JScrollPane(numberOfStoredArticle);
		sbr3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		rp7.add(l4);
		rp7.add(sbr3);
		rp2.add(l1);
		rp6.add(showUpdatesButton);
		rp6.add(clearResults);
		rp2.add(rp6);
		rp5.add(l2);
		rp5.add(typeTitleField);
		rp5.add(l3);
		rp5.add(typeTimeField);
		rp1.add(rp2);
		rp1.add(rp7);
		rp1.add(sbr1);
		rp1.add(rp5);
		rp1.add(searchByTitle);
		add(rp1);
	}

	public void actionPerformed(ActionEvent evt) {
		recentDeepSearch = new ReadXml();
		String tf1 = typeTitleField.getText();
		String tf2 = typeTimeField.getText();

		if (evt.getSource() == showUpdatesButton) {
			try {
				rr.getRecentChanges();
				
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		if (evt.getSource() == clearResults) {
			recentUpdatesResults.setText("");
			numberOfStoredArticle.setText("");
			rr.getStoredArticle().clear();
			int number=rr.getStoredArticle().size();
			String number2=String.valueOf(number);
			numberOfStoredArticle.append(number2);
		}
		if (evt.getSource() == searchByTitle && tf1.equals("") == false
				&& tf2.equals("") == false) {
			String recentTitle = recentDeepSearch.getTitle();
			recentTitle = tf1;
			String recentTime = recentDeepSearch.getTime();
			recentTime = tf2;
			try {
				recentDeepSearch.changeResultType1();
				recentDeepSearch.getPageInfo(recentTitle, "50", "50", 50,
						recentTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(popupResults, sbr2);
		} else if ((tf1.equals("") == true || tf2.equals("") == true)
				&& evt.getSource() != showUpdatesButton
				&& evt.getSource() != clearResults) {
			JOptionPane.showMessageDialog(popupResults, "Please fill option");

		}
	}

	public static void getRecentUpdatesResults() {
		numberOfStoredArticle.setText("");			
		removeCheck = 1;
		int number=rr.getStoredArticle().size();
		String number2=String.valueOf(number);
		numberOfStoredArticle.append(number2);
		
		recentUpdatesResults.append("Title: " + rr.getRecentChangesTitle()
				+ "\n" + "RecentChangesPageId: " + rr.getRecentChangesPageId()
				+ "\n" + "Recentchanges ID to patrol: " + rr.getRcid() + "\n"
				+ "RecentChangesRevid: " + rr.getRecentChangesRevid() + "\n"
				+ "RecentChangesOldrevid: " + rr.getRecentChangesOldrevid()
				+ "\n" + "RecentChangesTimestamp: "
				+ rr.getRecentChangesTimestamp() + "\n" + "\n");

		recentUpdatesResults.setCaretPosition(recentUpdatesResults
				.getDocument().getLength());
	}

	public static void getRecentSearchResults() {
		String rtime = recentDeepSearch.getTime();
		String rpid = recentDeepSearch.getPageid();
		String rtitle = recentDeepSearch.getTitle();
		String rrevid = recentDeepSearch.getrevid();
		String rparentid = recentDeepSearch.getParentId();
		String ruser = recentDeepSearch.getUser();
		String rcomments = recentDeepSearch.getComments();

		popupTextArea.append("Timestamp: " + rtime + "\n" + "Pageid: " + rpid
				+ "\n" + "Title:" + rtitle + "\n" + "Revid:" + rrevid + "\n"
				+ "Parentid:" + rparentid + "\n" + "User:" + ruser + "\n"
				+ "Comments:" + rcomments + "\n" + "\n" + "\n");
		popupTextArea.setCaretPosition(popupTextArea.getDocument().getLength());
		// System.out.println(rtime);
	}

	public static void getSuccessMessage() {
		recentUpdatesResults
				.append("-All the recent page updates has been stored successfully."
						+ "\n" + "\n");
		recentUpdatesResults.setCaretPosition(recentUpdatesResults
				.getDocument().getLength());
	}

	public void popupResults() {
		popupResults = new JFrame("Updates results");
		popupResults.setSize(400, 400);
		popupResults.setVisible(true);
		popupResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static RecentRevision getRecentRevision() {
		return rr;
	}

	public static int getRemoveCheck() {
		return removeCheck;
	}
}
