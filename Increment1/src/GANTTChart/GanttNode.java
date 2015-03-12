package GANTTChart;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;

import GUI.BrowseProjectsWindow;
import core.*;

public class GanttNode {
	
	private Task task;
	private int level;
	private int nodeHeight = 30;
	
	/**
	 * @author Destin Joyal 9576649
	 * @param task
	 * GanttNode basic constructor
	 */
	public GanttNode(Task task){
		this.setTask(task);
		this.setLevel(task.getPrereq().size());
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param task
	 * @return
	 * Builds a GANTT node for the GanttChart
	 */
	public JPanel buildGANTTContainer(Task task){
		JPanel panel = new JPanel();
		JButton label = new JButton(task.getName());
		label.setLocation(panel.getWidth()/2, panel.getHeight()/2);
		label.addActionListener(new nodeListener());
		label.setBorder(null);
		label.setBackground(Color.GRAY);
		panel.add(label);
		return panel;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 
	 * @author Destin
	 * An inner class to handle the case where a user clicks on a task in order to see the details of that task displayed.
	 */
	public class nodeListener implements ActionListener{

		public void actionPerformed(ActionEvent event) {
			//TODO: Hook this method up properly
			//task.displayTask();
			System.out.println(task.getDescription());
		}
		
	}
	
	
}
