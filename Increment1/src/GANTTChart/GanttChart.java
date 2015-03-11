package GANTTChart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

import GUI.MainWindow;
import core.*;

public class GanttChart {
	
	private ArrayList<Task> taskArray = new ArrayList<Task>();
	private ArrayList<GanttNode> nodeArray = new ArrayList<GanttNode>();
	
	// Values to adjust the distance between GANTT Chart components
	private final int factorX = 100;
	private final int factorY = 100;
	
	// Values to control the size of labels
	private final int labelY = 30;
	private final int labelX = 50;
	
	// Values to control the size of the diagram/detailsPanels
	private final int diagramPanelX = 500;
	private final int diagramPanelY = 300;	
	private final int detailsPanelX = 300;
	private final int detailsPanelY = 300;
	private final int mainFrameX = diagramPanelX + detailsPanelX;
	private final int mainFrameY = diagramPanelY + detailsPanelY;
	
	// The three components that make up the GANTT display
	private JFrame mainFrame;
	private JPanel detailsPanel;
	private JPanel diagramPanel;
	
	
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * Creates the GANTT Chart for a given project, returns a taskList for the JUnit test
	 */
	public void GANTTAnalysis(Project project){
		mainFrame = new JFrame("GANTT CHART for " + project.getName());
		
		GridLayout mainLayout = new GridLayout(1,2);
		mainFrame.setLayout(mainLayout);
		
		detailsPanel = new JPanel();
		detailsPanel.setVisible(true);
		
		diagramPanel = new JPanel();
		diagramPanel.setVisible(true);
	
		this.createDetailPanel(project, detailsPanel);
		this.createDiagramPanel(project, diagramPanel);
		
		mainFrame.add(detailsPanel);
		mainFrame.add(diagramPanel);
		
		detailsPanel.setSize(detailsPanelX, detailsPanelY);
		detailsPanel.setLocation(0,0);
		
		diagramPanel.setSize(diagramPanelX, diagramPanelY);
		diagramPanel.setLocation(detailsPanelX, 0); // Need to move the diagramPanel over by the size of the detailsPanel
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(mainFrameX, mainFrameY);
		mainFrame.setVisible(true);
		mainFrame.pack();
		
		
	//	Task[] taskList = (Task[]) project.getTasks().toArray();
		
	//	return taskList;	
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * @param detailsPanel
	 * Creates a detailsPanel and attaches it to the GANTT chart's mainPanel. This panel contains task detail information.
	 */
	public void createDetailPanel(Project project, JPanel detailsPanel){
		// Set the layout for the detailPanel
		GridLayout mainLayout = new GridLayout(2, 4);
		detailsPanel.setLayout(mainLayout);
		
		// Build the labels for the detailPanel header
		JPanel headerPanel = new JPanel();
		JPanel footerPanel = new JPanel();
		
		GridLayout footerLayout = new GridLayout(project.getTasks().size(), 4);
		GridLayout headerLayout = new GridLayout(1, 4);
		headerPanel.setLayout(headerLayout);
		footerPanel.setLayout(footerLayout);
		
		headerPanel.setVisible(true);
		footerPanel.setVisible(true);
		footerPanel.setBackground(Color.WHITE);
		
		JLabel[] headerLabels = new JLabel[4];
		for(int i = 0; i < headerLabels.length; i++){
			headerLabels[i] = new JLabel();
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
			headerPanel.add(headerLabels[i]);
		}
		
		// Populate the details panel with data
		taskArray = project.getTasks();
		for(int i = 0; i < taskArray.size(); i++){
			Task currentTask = taskArray.get(i);
			nodeArray.add(new GanttNode(currentTask));
			JLabel[] detailLabels = new JLabel[4];
			
			// Create buttons for all array entries
			for(int j = 0; j < detailLabels.length; j++){
				detailLabels[j] = new JLabel();
			}
			
			// Populate the array with data
			detailLabels[0].setText(String.valueOf(currentTask.getId()));
			detailLabels[1].setText(currentTask.getName());
			detailLabels[2].setText(this.stringifyPrereqs(taskArray));
			detailLabels[3].setText(String.valueOf(Math.abs(currentTask.getStartTime()-currentTask.getEndTime())));
			
			// Put the labels somewhere and size them
			for(int j = 0; j < detailLabels.length; j++){
				detailLabels[j].setSize(labelX, labelY);
				detailLabels[j].setLocation(j*labelX, i*labelY + labelY);
				footerPanel.add(detailLabels[j]);
			}
		}
		
		detailsPanel.add(headerPanel);
		detailsPanel.add(footerPanel);
	}
	/**
	 * @author Destin Joyal 9576649
	 * @param taskList
	 * @return
	 * Creates a string of all the taskIDs of prereqs for a task
	 */
	public String stringifyPrereqs(ArrayList<Task> taskList){
		String prereqList = "";
		for(int i = 0; i < taskList.size(); i++){
			ArrayList<Integer> tempList = taskList.get(i).getPrereq();
			for(int j = 0; j < tempList.size(); j++){
				if(j == taskList.size() - 1){
					prereqList += tempList.get(j).toString();
				} else {
					prereqList += tempList.get(j).toString() + ", ";
				}
			}
		}
		return prereqList;
	}
	
	/**
	 * @author Destin Joyal 9576649
	 * @param project
	 * @param diagramPanel
	 * Creates a diagramPanel and attaches it to the GANTT chart's mainPanel. This panel contains the GANTT chart diagram.
	 */
	public void createDiagramPanel(Project project, JPanel diagramPanel){
		int headerDivisions = this.getProjectLength(project);
		
		GridLayout mainLayout = new GridLayout(2, 1);
		GridLayout headerLayout = new GridLayout(1, headerDivisions);
		GridBagLayout footerLayout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(0,0,2,0);
				
		JPanel headerPanel = new JPanel();
		JPanel footerPanel = new JPanel();
		
		headerPanel.setVisible(true);
		footerPanel.setVisible(true);
		
		diagramPanel.setLayout(mainLayout);
		headerPanel.setLayout(headerLayout);
		footerPanel.setLayout(footerLayout);
		
		// Set up the headerPanel (contains the time labels at the top of the screen)
		for(int i = 0; i <= headerDivisions; i++){
			JLabel durationMarker = new JLabel();
			durationMarker.setText(String.valueOf(i+1));
			durationMarker.setLocation(i * detailsPanelX, 0);
			headerPanel.add(durationMarker);
			constraints.gridx = i;
			footerPanel.add(new JLabel(), constraints);
		}
		
		// Set up the footerPanel (contains the actual GANTT chart)
		for(int i = 0; i < nodeArray.size(); i++){
			GanttNode currentNode = nodeArray.get(i);
			
			Task currentTask = currentNode.getTask();
			
			int duration = currentTask.getEndTime() - currentTask.getStartTime();
			
			JPanel nodeFrame = currentNode.buildGANTTContainer(currentTask);
			nodeFrame.setBackground(Color.GRAY);
			nodeFrame.setVisible(true);
			
			constraints.gridwidth = duration;
			constraints.gridy = i + 1;
			constraints.gridx = currentNode.getTask().getStartTime() - 1;
			constraints.fill = constraints.HORIZONTAL;
			
			footerPanel.add(nodeFrame, constraints);
		}
		
		diagramPanel.add(headerPanel);
		diagramPanel.add(footerPanel);
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
		return Math.abs(max - min);	
	}
	
	public static void main(String[] args)
	{
		
		Project project = new Project("bob", "myProj", 1);
		
		Task[] task = new Task[3];
		task[0] = new Task(1, "CAT", "CATSSSS", 1, 5, 1, "bob");
		task[1] = new Task(2, "DOG", "DOGS", 1, 8, 1, "bob");
		task[2] = new Task(3, "Hamster", "Rodents", 5, 7, 1, "bob");
		task[2].setPreReqs(task[0]);
		GanttChart chart = new GanttChart();
		chart.GANTTAnalysis(project);
		for(int i = 0; i < task.length; i++){
			task[i].deleteTask();
		}
		
	}

}
