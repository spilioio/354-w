package GUI;

import java.util.ArrayList;

import core.Project;
import core.Task;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PERTAnalysisPanel extends BaseDetailPanel {
	
private Project currentProject;
	
	private static JLabel taskName = new JLabel("Task Name");
	private static JLabel description = new JLabel("Description");
	private static JLabel precedent = new JLabel("Precedent Tasks");
	private static  JLabel optimistic = new JLabel("Optimistic Completion Time");
	private static  JLabel mostLikely = new JLabel("Most Likely Completion Time");
	private static JLabel Pessimistic = new JLabel("Pessimistic Completion Time");
	private static  JLabel expected = new JLabel("Expected Completion Time");
	private static JLabel StandardDeviation = new JLabel("Standard Deviation");
	
	public PERTAnalysisPanel(Project p){
		currentProject = p;		
	}
	
	
    
    public void setProject(Project proj){
        this.currentProject = proj;
    }
    
    public void populatePERTHeader(){
        this.addBodyHeader(taskName);
        this.addBodyHeader(description);
        this.addBodyHeader(precedent);
        this.addBodyHeader(optimistic);
        this.addBodyHeader(mostLikely);
        this.addBodyHeader(Pessimistic);
        this.addBodyHeader(expected);
        this.addBodyHeader(StandardDeviation);
    }
    
    public void populatePERTDetails(){
    	ArrayList<Task> Tasks =  currentProject.getTasks();
    	for(Task t : Tasks){
    		
    		//loop to get the list of prereqs for each task and turn it into a string
    		StringBuilder sb = new StringBuilder();
    		ArrayList<Integer> prereq = t.getPrereq(); 
    		for(Integer i : prereq)
    		{
    			sb.append(i.toString());
    			sb.append(", ");
    		}
    		
	        this.addBodyDetails(new JLabel(t.getName()));
	        this.addBodyDetails(new JLabel(t.getDescription()));
	        
	        //prereqs
	        this.addBodyDetails(new JLabel("" + sb.toString()));
	    
//	        this.addBodyDetails(new JLabel("" + t.getOptimisticEstimate()));
//	        this.addBodyDetails(new JLabel("" + t.getMostLikelyEstimate()));
//	        this.addBodyDetails(new JLabel("" + t.getPessimisticEstimate()));
//	        this.addBodyDetails(new JLabel("" + t.getExpectedEstimate()));
//	        this.addBodyDetails(new JLabel("" + t.getStandardDeviation()));
    	}
    }
    
    public void buildPERTPanel(){
    	this.initialize();
        this.populatePERTHeader();
        this.populatePERTDetails();
        this.buildHeader();
        this.buildBody();
        this.buildFooter();
        this.buildMain();
    }
    
    public JPanel displayPERTPanel(){
        this.buildPERTPanel();
        return this.mainPanel;
    }
    
    public static void main(String[] args){
    	
    	Project p1;
		//Create a Project
		p1 = new Project("b_jenkins", "Critical Path Test2");
				
		Task t1 = new Task("Task1", "must be completed before Task 2 ", 0, 5, p1.getId(), "b_jenkins");
		Task t2 = new Task("Task2", "must be completed before Task 3", 0, 5, p1.getId(), "b_jenkins" );
		Task t3 = new Task("Task3", "Last task in the project", 0, 5, p1.getId(), "b_jenkins" );
				
		t3.addPrereq(t2.getId());
		t2.addPrereq(t1.getId());
		//t1 should start first and has no prerequisites
		
    	JFrame PERTtest = new JFrame("PERT Test");
    	PERTtest.setVisible(true);
    	PERTAnalysisPanel PERTPanel = new PERTAnalysisPanel(p1);
    	PERTtest.add(PERTPanel.displayPERTPanel());

    }
    

}
