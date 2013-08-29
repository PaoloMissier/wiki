package uk.ac.ncl.fanyaoxia.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.ac.ncl.fanyaoxia.Neo4jQuery.CreateIndex;
import uk.ac.ncl.fanyaoxia.webpagefetch.ReadXml;

public class SearchTitleGUI extends JPanel implements ActionListener {

	private static ReadXml passdata;
	private JFrame framepopup;
	private JButton b1;
	private static JButton b2;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private static JTextArea textArea1;
	private static final String m1 = "Please fill up all the option";
	private final static String newline = "\n";
	private JScrollPane sbrText; // Scroll pane for text area

	public SearchTitleGUI() {

		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(0, 2));
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p1.setBorder(BorderFactory.createEtchedBorder());

		JPanel p3 = new JPanel();
		p3.setLayout(new BoxLayout(p3, FlowLayout.LEFT));

		JPanel p4 = new JPanel();
		p4.setLayout(new BoxLayout(p4, FlowLayout.LEADING));

		textArea1 = new JTextArea(26, 38);
		textArea1.setLineWrap(true);
		textArea1.setEditable(false);
		sbrText = new JScrollPane(textArea1);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JLabel l1 = new JLabel("Title");
		// add(l1);
		textField1 = new JTextField("england");
		// textField1.addActionListener(this);
		add(textField1);

		JLabel l2 = new JLabel("Maximum contribution");
		// add(l2);
		textField2 = new JTextField("50");
		add(textField2);

		JLabel l3 = new JLabel("Number of Revisions");
		// add(l3);
		textField3 = new JTextField("50");
		add(textField3);

		JLabel l4 = new JLabel("Bit depth of version");
		// add(l4);
		textField4 = new JTextField("32");
		add(textField4);

		JLabel l5 = new JLabel("Start-dates of search");
		// add(l5);
		textField5 = new JTextField("20130827000000");
		add(textField5);

		b1 = new JButton("Generate");
		// add(b1);
		b1.addActionListener(this);
		b2 = new JButton("Stop");
		// add(b1);
		b2.addActionListener(this);

		p2.add(l1);
		p2.add(textField1);
		p2.add(l2);
		p2.add(textField2);
		p2.add(l3);
		p2.add(textField3);
		p2.add(l4);
		p2.add(textField4);
		p2.add(l5);
		p2.add(textField5);
		p2.add(b1);
		p2.add(b2);
		p3.add(p2);
		p1.add(p3);
		p1.add(sbrText);
		add(p1);
	}

	public void actionPerformed(ActionEvent evt) {
		String s1 = textField1.getText();
		String s2 = textField2.getText();
		String s3 = textField3.getText();
		String s4 = textField4.getText();
		String s5 = textField5.getText();

		if (evt.getSource() == b2) {// stop the timer when press the button,
			b1.setEnabled(true);						// null pointer exception
			if (getReadXml() == null) {
				System.out.println("**Monitor is not running**" + "\n");
				System.out.println();

				textArea1.append("Monitor is not running" + "\n" + "\n");
				textArea1.setCaretPosition(SearchTitleGUI.getTextArea()
						.getDocument().getLength());

			} else {
				if (getReadXml().getTimer() == null) {
					System.out.println("**Monitor is not running**" + "\n");
					System.out.println();

					textArea1.append("Monitor is not running" + "\n" + "\n");
					textArea1.setCaretPosition(SearchTitleGUI.getTextArea()
							.getDocument().getLength());
				} else {
					if (getReadXml().getTimer().isRunning()) {
						getReadXml().getTimer().stop();
						System.out.println("**Monitor Stopped**" + "\n");
						System.out.println();

						textArea1.append("Monitor Stopped" + "\n" + "\n");
						textArea1.setCaretPosition(SearchTitleGUI.getTextArea()
								.getDocument().getLength());
					} else {
						System.out.println("**Monitor is not running**");
						System.out.println();

						textArea1
								.append("Monitor is not running" + "\n" + "\n");
						textArea1.setCaretPosition(SearchTitleGUI.getTextArea()
								.getDocument().getLength());
					}
				}

			}
		}

		if (s1.equals("") == false && s2.equals("") == false
				&& s3.equals("") == false && s4.equals("") == false
				&& s5.equals("") == false && evt.getSource() == b1) {
			b1.setEnabled(false);
			passdata = new ReadXml();
			String text1 = textField1.getText();
			String text2 = textField2.getText();
			String text3 = textField3.getText();
			int text4 = Integer.parseInt(textField4.getText());
			String text5 = textField5.getText();
			
			try {
				passdata.changeResultType0();// set the result type to zero
												// before execute of the first
												// tab results
				passdata.getPageInfo(text1, text2, text3, text4, text5);
				CreateIndex.indexCreated();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (s1.equals("") == true || s2.equals("") == true
				|| s3.equals("") == true || s4.equals("") == true
				|| s5.equals("") == true) {
			JOptionPane.showMessageDialog(framepopup, m1);
		}
	}

	public void popupFrame() {
		framepopup = new JFrame("Show popup Dialog Box");
		framepopup.setSize(400, 400);
		framepopup.setVisible(true);
		framepopup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void getStringText() {

		String ptime = passdata.getTime();
		String ppid = passdata.getPageid();
		String ptitle = passdata.getTitle();
		String previd = passdata.getrevid();
		String pparentid = passdata.getParentId();
		String puser = passdata.getUser();
		String pcomments = passdata.getComments();

		textArea1.append("Timestamp: " + ptime + "\n" + "Pageid: " + ppid
				+ "\n" + "Title:" + ptitle + "\n" + "Revid:" + previd + "\n"
				+ "Parentid:" + pparentid + "\n" + "User:" + puser + "\n"
				+ "Comments:" + pcomments + "\n" + newline);
		textArea1.setCaretPosition(textArea1.getDocument().getLength());
		// System.out.println(passToText.getPageid());
	}

	public static void printNoupdates() {
		textArea1.append("No new Updates..." + "\n" + newline);
		textArea1.setCaretPosition(textArea1.getDocument().getLength());
		// System.out.println("successed..");
	}

	public static JTextArea getTextArea() {
		return textArea1;
	}

	public static ReadXml getReadXml() {
		return passdata;
	}

	public static JButton getButtonB2() {
		return b2;
	}
}
