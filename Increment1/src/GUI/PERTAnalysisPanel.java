package GUI;

import java.util.ArrayList;

import core.Project;
import core.Task;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PERTAnalysisPanel extends BaseDetailPanel {
	
private Project currentProject;
	
	JLabel taskName = new JLabel("Task Name");
	JLabel description = new JLabel("Description");
    JLabel precedent = new JLabel("Precedent Tasks");
    JLabel optimistic = new JLabel("Optimistic Completion Time");
    JLabel mostLikely = new JLabel("Most Likely Completion Time");
    JLabel Pessimistic = new JLabel("Pessimistic Completion Time");
    JLabel expected = new JLabel("Expected Completion Time");
    JLabel StandardDeviation = new JLabel("Standard Deviation");
	
	public PERTAnalysisPanel(Project p){
		currentProject = p;
		JPanel j = this.displayPERTPanel();
				
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
    public static void main(){
    	
    }
    

}
