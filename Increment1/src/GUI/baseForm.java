/*<<<<<<< HEAD
package GUI;

import java.util.*;

import javax.swing.*;

import java.awt.*;

public class baseForm {
	
	protected JFrame mainFrame;
	protected JPanel mainPanel;
	protected JPanel headerPanel;
	protected JPanel footerPanel;
	
	protected String formName;
	
	public baseForm(String name){
		this.mainFrame = new JFrame(name);
		this.mainFrame.setDefaultCloseOperation(0);
		this.mainPanel = new JPanel();
		this.createMainPanel();
		this.mainFrame.setVisible(true);
		this.mainFrame.pack();
				
	}
	
	
	
	public void createMainPanel(){
		headerPanel.setVisible(true);
		footerPanel.setVisible(true);
		mainPanel.setVisible(true);
		mainFrame.setVisible(true);
		
		this.mainPanel.add(headerPanel);
		this.mainPanel.add(footerPanel);
		this.mainFrame.add(mainPanel);
	}
	
	   public static void main(String args[]) {
	        
	    	new baseForm("Project");
	  
	    }

}
=======*/
package GUI;

import java.util.*;

import javax.swing.*;

import java.awt.*;

public class baseForm {
	
	protected JFrame mainFrame;
	protected JPanel mainPanel;
	protected JPanel headerPanel;
	protected JPanel footerPanel;
	
	protected String formName;
	
	public baseForm(String name){
		this.mainFrame = new JFrame(name);
		this.mainFrame.setDefaultCloseOperation(0);
		this.mainPanel = new JPanel();
		this.createMainPanel();
		this.mainFrame.setVisible(true);
		this.mainFrame.pack();
				
	}
	
	
	
	public void createMainPanel(){
		headerPanel.setVisible(true);
		footerPanel.setVisible(true);
		mainPanel.setVisible(true);
		mainFrame.setVisible(true);
		
		this.mainPanel.add(headerPanel);
		this.mainPanel.add(footerPanel);
		this.mainFrame.add(mainPanel);
	}
	
	   public static void main(String args[]) {
	        
	    	new baseForm("Project");
	  
	    }

}
//>>>>>>> origin/master
