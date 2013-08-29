package uk.ac.ncl.fanyaoxia.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import org.jdom2.JDOMException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import uk.ac.ncl.fanyaoxia.Neo4jQuery.Delete;

public class MainGUI extends JFrame implements ActionListener{

	JMenuItem menuItem1;
	JMenuItem menuItem2;
	static JFrame frame;
	
	public void userControllFrame() {
		frame=new JFrame("User Center");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu deleteOption=new JMenu("Delete Option");
		menuItem1=new JMenuItem("Delete All nodes and relations");
		menuItem2=new JMenuItem("Delete All Indexes");
		menuItem1.addActionListener(this);
		menuItem2.addActionListener(this);
		deleteOption.add(menuItem1);
		deleteOption.add(menuItem2);

		menuBar.add(deleteOption);
		
		JTabbedPane tp=new JTabbedPane();
		tp.addTab("Generate revision with Title", new SearchTitleGUI());
		tp.addTab("Show recent updates", new RecentUpdatesGUI());
		tp.addTab("Monitor recent updates",new MonitorRecentUpdatesGUI());
		
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(tp);
		frame.pack();
		frame.setSize(550, 700);
		frame.setVisible(true);

	}
	public static JFrame getFrame(){
		return frame;
	}
	public void actionPerformed(ActionEvent event) {
		if(event.getSource()==menuItem1){
			Delete delete=new Delete();
			try {
				delete.deleteAllNodeOrRelation();
			} catch (ClientHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UniformInterfaceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(event.getSource()==menuItem2){
			Delete delete=new Delete();
			delete.deleteAllIndex();
		}
    }
	
}
