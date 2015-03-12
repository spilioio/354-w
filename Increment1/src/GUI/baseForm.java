package GUI;

import java.util.*;

import javax.swing.*;

import java.awt.*;

public class baseForm {
	
	protected JFrame mainFrame;
	protected JPanel mainPanel;
	protected JPanel headerButtonPanel;
	protected JPanel headerRadioPanel;
	protected JPanel displayPanel;
	
	protected String formName;
	
	public baseForm(String name){
		this.mainFrame = new JFrame(name);
		this.mainFrame.setDefaultCloseOperation(0);
		this.mainPanel = new JPanel();
		this.createMainPanel();
		this.mainFrame.setVisible(true);
		this.mainFrame.pack();
				
	}
	
	public void createHeaderButtonPanel(){
		this.headerButtonPanel = new JPanel();
		
		ArrayList<JButton> buttonList = new ArrayList<JButton>();
	
		buttonList.add(new JButton("Create " + this.formName));
		buttonList.add(new JButton("Open " + this.formName));
		buttonList.add(new JButton("Open " + this.formName));
		
		for(int i = 0; i < buttonList.size(); i++){
			buttonList.get(i).setLocation(i*30,10);
			headerButtonPanel.add(buttonList.get(i));
		}
		
	}
	
	public void createRadioPanel(){
		ButtonGroup radioButtons = new ButtonGroup();
		
		ArrayList<JRadioButton> radioList = new ArrayList<JRadioButton>();
		
		radioList.add(new JRadioButton("All " + this.formName));
		radioList.add(new JRadioButton("My " + this.formName));
		radioList.add(new JRadioButton("ID"));
		
		for(int i = 0; i < radioList.size(); i++){
			radioList.get(i).setLocation(i*30,10);
			//this.headerRadioPanel.add(radioList.get(i));
		}
				
	}
	
	public void createMainPanel(){
		this.createHeaderButtonPanel();
		this.createRadioPanel();
		this.mainPanel.add(headerButtonPanel);
		//this.mainPanel.add(headerRadioPanel);
	}
	
	   public static void main(String args[]) {
	        
	    	new baseForm("Project");
	  
	    }

}
