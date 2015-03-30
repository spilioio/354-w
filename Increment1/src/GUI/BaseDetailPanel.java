package GUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BaseDetailPanel extends Component{
	
	protected JPanel mainPanel;
	protected JPanel headerPanel;
	protected JPanel bodyPanel;
	protected JPanel footerPanel;
	
	protected ArrayList<JButton> headerButtons;
	protected ArrayList<JButton> footerButtons;
	protected ArrayList<JLabel> headerLabels;
	protected ArrayList<JLabel> bodyHeaderLabels;
	protected ArrayList<JLabel> bodyDetailLabels;
	
	protected GridBagLayout headerLayout = new GridBagLayout();
	
	protected Dimension mainDim = new Dimension(500, 500);
	protected Dimension headerFooterDim = new Dimension(100, 500);
	protected Dimension bodyDim = new Dimension(300, 500);
	
	protected Dimension buttonDim = new Dimension(20, 40);
	protected Dimension labelDim = new Dimension(20, 40);
	
	public void assembleMainPanel(){
		this.mainPanel.add(headerPanel);
		this.mainPanel.add(bodyPanel);
		this.mainPanel.add(footerPanel);
	}
	
	public void buildHeader(){
		int buttonCount = this.headerButtons.size();
		int labelCount = this.headerLabels.size();
		GridBagConstraints headerConstraints = new GridBagConstraints();
		headerConstraints.gridy = 1;
		headerConstraints.gridx = 1;
		headerConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Add the headerLabels
		for(int i = 0; i < labelCount; i++){
			this.headerPanel.add(this.headerLabels.get(i), headerConstraints);
			headerConstraints.gridx++;
		}
		
		headerConstraints.gridx = 1;
		headerConstraints.gridy = 2;
		
		//Add the headerButtons
		for(int i = 0; i < buttonCount; i++){
			this.headerPanel.add(this.headerButtons.get(i), headerConstraints);
			headerConstraints.gridx++;
		}
		
		this.headerPanel.setVisible(true);
	}
	
	public void buildBody(){
		int labelCount = this.bodyHeaderLabels.size();
		GridBagConstraints bodyConstraints = new GridBagConstraints();
		
		bodyConstraints.gridx = 1;
		bodyConstraints.gridy = 1;
		bodyConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Add the headerLabels
		for(int i = 0; i < labelCount; i++){
			this.bodyPanel.add(this.bodyHeaderLabels.get(i), bodyConstraints);
			bodyConstraints.gridx++;
		}
		
		bodyConstraints.gridx = 1;
		bodyConstraints.gridy = 2;
		
		// Add the detailLabels
		for(int i = 0; i < this.bodyDetailLabels.size(); i++){
			int flag = 0;
			if(flag == 0){
				this.bodyDetailLabels.get(i).setBackground(Color.GRAY);
			} else {
				this.bodyDetailLabels.get(i).setBackground(Color.DARK_GRAY);
			}
			
			this.bodyPanel.add(this.bodyDetailLabels.get(i), bodyConstraints);
			bodyConstraints.gridx++;
			if((i + 1) % labelCount == 0){
				flag++;
				bodyConstraints.gridx = 1;
				bodyConstraints.gridy++;
			}
		}
		
		this.bodyPanel.setVisible(true);
	}
	
	public void buildFooter(){
		int buttonCount = this.footerButtons.size();
		GridBagConstraints footerConstraints = new GridBagConstraints();
		
		footerConstraints.gridx = 1;
		footerConstraints.gridy = 1;
		footerConstraints.fill = GridBagConstraints.HORIZONTAL;
		
		// Add the buttons
		for(int i = 0; i < buttonCount; i++){
			this.footerPanel.add(this.footerButtons.get(i), footerConstraints);
			footerConstraints.gridx++;
		}
		
		this.footerPanel.setVisible(true);
	}
	
	public void buildMain(){
		this.mainPanel.add(this.headerPanel);
		this.mainPanel.add(this.bodyPanel);
		this.mainPanel.add(this.footerPanel);
		
		this.mainPanel.setVisible(true);
	}
	
	public void addHeaderButton(JButton button){
		button.setSize(this.buttonDim);
		this.headerButtons.add(button);
	}
	
	public void removeHeaderButton(JButton button){
		this.headerButtons.remove(button);
	}
	
	public void addFooterButton(JButton button){
		button.setSize(this.buttonDim);
		this.footerButtons.add(button);
	}
	
	public void removeFooterButton(JButton button){
		this.footerButtons.remove(button);
	}
	
	public void addBodyHeader(JLabel label){
		label.setSize(labelDim);
		this.bodyHeaderLabels.add(label);
	}
	
	public void removeBodyHeader(JLabel label){
		this.bodyHeaderLabels.remove(label);
	}
	
	public void addBodyDetails(JLabel label){
		label.setSize(labelDim);
		this.bodyDetailLabels.add(label);
	}
	
	public void removeBodyDetails(JLabel label){
		this.bodyDetailLabels.remove(label);
	}
	
	public void hidePanel(){
		this.mainPanel.setVisible(false);
	}
	
	public void showPanel(){
		this.mainPanel.setVisible(true);
	}
	
}
