package uk.ac.ncl.fanyaoxia.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.ac.ncl.fanyaoxia.createCSV.CreateCSVFile;
import uk.ac.ncl.fanyaoxia.monitor.MonitorRecentUpdates;
import uk.ac.ncl.fanyaoxia.monitor.StoredArticle;
import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXml;
import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXmlRanking;
import uk.ac.ncl.fanyaoxia.webpagefetch.RecentRevision;

public class MonitorRecentUpdatesGUI extends JPanel implements ActionListener {
	JButton mb1;
	JButton mb2;
	JButton mb3;
	JButton mb4;
	JButton mb5;
	static JTextArea monitorTextArea;
	JScrollPane scrollArea;
	JScrollPane scrollAreaResults;
	MonitorRecentUpdates mrus;
	static JFrame frame;
	static JFrame frame2;
	static JTextArea tableR;
	static StoredArticle[] passArticle2;
	boolean checkMapStore;
	private RecentRevision monitoring;

	public MonitorRecentUpdatesGUI() {
		checkMapStore = false;
		monitoring = RecentUpdatesGUI.getRecentRevision();
		mrus = new MonitorRecentUpdates();

		JPanel mru1 = new JPanel();
		mru1.setLayout(new GridLayout());
		JPanel mru2 = new JPanel();
		mru2.setLayout(new GridLayout());
		JPanel mru3 = new JPanel();
		mru3.setLayout(new BoxLayout(mru3, BoxLayout.Y_AXIS));
		mru3.setBorder(BorderFactory.createEtchedBorder());

		JPanel rp6 = new JPanel();
		rp6.setLayout(new GridLayout(3, 3));

		JLabel ml1 = new JLabel("Monitor the recent updates pages");
		JLabel ml2 = new JLabel("Remove all stored recent updates pages");
		JLabel ml3 = new JLabel("Print updates history or save to CSV File");

		mb1 = new JButton("Monitor");
		mb1.addActionListener(this);
		mb2 = new JButton("Remove");
		mb2.addActionListener(this);
		mb3 = new JButton("Print");
		mb3.addActionListener(this);
		mb4 = new JButton("Clear");
		mb4.addActionListener(this);
		mb5 = new JButton("Save");
		mb5.addActionListener(this);

		monitorTextArea = new JTextArea(30, 38);
		monitorTextArea.setLineWrap(true);
		monitorTextArea.setEditable(false);
		scrollArea = new JScrollPane(monitorTextArea);
		scrollArea
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		tableR = new JTextArea(30, 35);
		tableR.setLineWrap(true);
		tableR.setEditable(false);
		scrollAreaResults = new JScrollPane(tableR);
		scrollAreaResults
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		rp6.add(ml1);
		mru1.add(mb1);
		mru1.add(mb4);
		rp6.add(mru1);
		rp6.add(ml2);
		rp6.add(mb2);
		rp6.add(ml3);
		mru2.add(mb3);
		mru2.add(mb5);
		rp6.add(mru2);
		mru3.add(rp6);
		mru3.add(scrollArea);
		add(mru3);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == mb2) {
			if (RecentUpdatesGUI.getRemoveCheck() == 1 && checkMapStore == true) {
				monitoring.removeAllStoredArticle();
				getRemoveMessage();
			} else if (checkMapStore == false
					|| MonitorRecentUpdates.getMap().isEmpty()) {
				JOptionPane
						.showMessageDialog(frame,
								"No record in the storage, please click the monitor button.");

			}
		}
		if (e.getSource() == mb1) {
			try {
				mrus.continueMonitor();
				checkMapStore = true;

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == mb3) {
			mrus.printAllResults();
			JOptionPane.showMessageDialog(frame, scrollAreaResults);
		}
		if (e.getSource() == mb4) {
			monitorTextArea.setText("");
		}
		if (e.getSource() == mb5) {
			JTextField field1 = new JTextField();
			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel("Enter File Name"));
			panel.add(field1);
			int result = JOptionPane.showConfirmDialog(null, panel,
					"Save as CSV File", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION && field1.equals("") == false) {
				CreateCSVFile csv = new CreateCSVFile();
				csv.createFile(field1.getText());
			} else {
				System.out.println("Cancelled");
			}
		}
	}

	public static void getLineSplit() {
		monitorTextArea.append("**************" + "\n");
		monitorTextArea.setCaretPosition(monitorTextArea.getDocument()
				.getLength());
	}

	public static void getResultsTable() {
		Map<StoredArticle, ReadXmlRanking> map1 = MonitorRecentUpdates.getMap();

		for (StoredArticle sa2 : map1.keySet()) {
			tableR.append("---" + sa2.getStoredTitle() + "\n");
			for (String k1 : sa2.getTimeAndPopularity().keySet()) {
				tableR.append("   Monitoring Time: "
						+ sa2.getTimeAndPopularity().get(k1) + "  Popularity: "
						+ k1 + "\n" + "\n");

			}
			tableR.append("\n");
		}

		tableR.setCaretPosition(tableR.getDocument().getLength());
	}

	public static void getRemoveMessage() {
		monitorTextArea.append("\n"
				+ "*All the stored article has been removed*" + "\n");
		monitorTextArea.setCaretPosition(monitorTextArea.getDocument()
				.getLength());
	}

	public static JTextArea getMonitorTextArea() {
		return monitorTextArea;
	}

	private static void showResults() {
		frame = new JFrame("Results");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(tableR);
		frame.pack();
		frame.setVisible(true);
	}
}
