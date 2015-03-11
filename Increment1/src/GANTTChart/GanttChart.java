package GANTTChart;

import java.awt.Dimension;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import core.*;

public class GanttChart {
	
	private ArrayList<Task> taskArray;
	private ArrayList<GanttNode> nodeArray;
	
	// Values to adjust the distance between GANTT Chart components
	private final int factorX = 100;
	private final int factorY = 100;
	
	// Values to control the size of labels
	private final int labelY = 30;
	private final int labelX = 50;
	
	// Values to control the size of the diagram/detailsPanels
	private final int diagramPanelX = 500;
	private final int diagramPanelY = 500;	
	private final int detailsPanelX = 500;
	private final int detailsPanelY = 500;
	
	// The three components that make up the GANTT display
	private JFrame mainFrame;
	private JPanel detailsPanel;
	private JPanel diagramPanel;
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * Creates the GANTT Chart for a given project
	 */
	public Task[] GANTTAnalysis(Project project){
		mainFrame = new JFrame(project.getName());
		detailsPanel = new JPanel();
		detailsPanel.setSize(detailsPanelX, detailsPanelY);		
		
		diagramPanel = new JPanel();
		diagramPanel.setSize(diagramPanelX, diagramPanelY);
		diagramPanel.setLocation(detailsPanelX, 0); // Need to move the diagramPanel over by the size of the detailsPanel
	
		this.createDetailPanel(project, detailsPanel);
		this.createDiagramPanel(project, diagramPanel);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		Task[] taskList = (Task[]) project.getTasks().toArray();
		
		return taskList;	
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * @param detailsPanel
	 * Creates a detailsPanel and attaches it to the GANTT chart's mainPanel. This panel contains task detail information.
	 */
	public void createDetailPanel(Project project, JPanel detailsPanel){
		// Build the labels for the detailPanel header
		JLabel[] headerLabels = new JLabel[4];
		for(int i = 0; i < headerLabels.length; i++){
			switch (i){
			case 0:
				headerLabels[i].setText("ID");
				break;
			case 1:
				headerLabels[i].setText("Task Name");
				break;
			case 2:
				headerLabels[i].setText("Prerequisites");
				break;
			case 3:
				headerLabels[i].setText("Duration");
				break;
			}
			headerLabels[i].setSize(labelX, labelY);
			headerLabels[i].setLocation(i*labelX, 0);
			detailsPanel.add(headerLabels[i]);
		}
		
		// Populate the details panel with data
		taskArray = project.getTasks();
		for(int i = 0; i < taskArray.size(); i++){
			Task currentTask = taskArray.get(i);
			nodeArray.add(new GanttNode(currentTask));
			JLabel[] detailLabels = new JLabel[4];
			detailLabels[0].setText(String.valueOf(currentTask.getId()));
			detailLabels[1].setText(currentTask.getName());
			detailLabels[2].setText(currentTask.getPrereq().toString());
			detailLabels[3].setText(String.valueOf(currentTask.getStartTime()-currentTask.getEndTime()));
			for(int j = 0; j < detailLabels.length; j++){
				detailLabels[i].setSize(labelX, labelY);
				detailLabels[i].setLocation(j*labelX, i*labelY + labelY);
				detailsPanel.add(detailLabels[i]);
			}
		}
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * @param diagramPanel
	 * Creates a diagramPanel and attaches it to the GANTT chart's mainPanel. This panel contains the GANTT chart diagram.
	 */
	public void createDiagramPanel(Project project, JPanel diagramPanel){
		int headerDivisions = this.getProjectLength(project);
		
		for(int i = 0; i < headerDivisions; i++){
			JLabel durationMarker = new JLabel();
			durationMarker.setText(String.valueOf(i));
			durationMarker.setLocation(i * detailsPanelX, 0);
			diagramPanel.add(durationMarker);
		}
		
		for(int i = 0; i < nodeArray.size(); i++){
			GanttNode currentNode = nodeArray.get(i);
			Task currentTask = currentNode.getTask();
			int duration = currentTask.getStartTime() - currentTask.getEndTime();
			JPanel nodeFrame = currentNode.buildGANTTContainer(currentTask);
			nodeFrame.setLocation(currentNode.getLevel() * this.factorX, i * this.factorY);
			nodeFrame.setSize(duration, this.factorY);
			diagramPanel.add(nodeFrame);
		}
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * @return int
	 * Iterates through a project to find the earliest startTime and latest endTime. Returns the total duration of the project.
	 */
	public int getProjectLength(Project project){
		// Gets a list of tasks associated with the project
		ArrayList<Task> taskList = project.getTasks();
		
		// Sets two initial values for the upcoming search
		int min = taskList.get(1).getStartTime();
		int max = 0;
		
		// Iterates through the taskList to find the earliest startTime and latest endTime
		for(int i = 0; i < taskList.size(); i++){
			if(taskList.get(i).getStartTime() < min){
				min = taskList.get(i).getStartTime();
			} 
			if(taskList.get(i).getEndTime() > max){
				max = taskList.get(i).getEndTime();
			}
		}
		// Subtracts the values to find the project's total duration
		return (max - min);	
	}

}
