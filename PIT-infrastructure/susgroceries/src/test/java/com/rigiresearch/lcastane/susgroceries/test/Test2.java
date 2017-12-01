/**
 * Copyright 2017 University of Victoria
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.rigiresearch.lcastane.susgroceries.test;

import com.rigiresearch.lcastane.DirectedArc;
import com.rigiresearch.lcastane.framework.Artefact;
import com.rigiresearch.lcastane.framework.MART;
import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.framework.Sentence;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import com.rigiresearch.lcastane.galapagosmodel.GoalsModel;
import com.rigiresearch.lcastane.galapagosmodel.IStarPIT;
import com.rigiresearch.lcastane.galapagosmodel.UCSAGoalGraph;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Actor;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Goal;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Resource;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Task;
import com.rigiresearch.lcastane.galapagosmodel.nodes.WildcardNode;
import com.rigiresearch.lcastane.galapagosmodel.operations.GoalsModelOp;
import com.rigiresearch.lcastane.primor.Manager;
import com.rigiresearch.lcastane.primor.ModelNotFoundException;
import com.rigiresearch.lcastane.primor.RemoteService;

import java.awt.print.Printable;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

/**
 * TODO
 * @date 2017-07-12
 * @version $Id$
 * @since 0.0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public final class Test2 {

    private static final String MODEL_ID = "goalsmodel";
    
    private static Manager manager;
    //private static InstanceABig userTasking = new InstanceABig();
    private static InstanceA userTasking = new InstanceA();
    //private static InstanceB userTasking = new InstanceB();
    //private static InstanceC userTasking = new InstanceC();
    
    /**
     * TODO
     * @throws RemoteException 
     * @throws NotBoundException 
     * @throws ValidationException 
     * @throws MalformedURLException 
     * @throws ModelNotFoundException 
     */
    @BeforeClass
    public static void pre() throws RemoteException, NotBoundException, MalformedURLException, ValidationException, ModelNotFoundException {
    	
        Test2.manager = Test2.initialiseManager();        
        Test2.manager.registerModel(MODEL_ID, userTasking.model());
        //growGraph(1);   
        
    }
    
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[1][0]);
    }

    

    @Test
    public final void testA_addTaskTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test2.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.ADD_TASK,
                new Task("T_print_receipt", "Print receipt"),
                new WildcardNode(Task.class, "T_proceed_checkout")
            )
        );
        Assert.assertTrue(
            "Artefact should contain node T_print_receipt",
            userTasking.containsNode(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_print_receipt"
            )
        );
    }

    @Test
    public final void testB_addDecompositionTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test2.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.ADD_DECOMPOSITION,
                new WildcardNode(Task.class, "T_proceed_checkout"),
                new WildcardNode(Task.class, "T_print_receipt")
            )
        );
        Assert.assertTrue(
            "Artefact should contain relationship T_proceed_checkout --> T_print_receipt",
            userTasking.containsConnector(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_proceed_checkout",
                "T_print_receipt"
            )
        );
    }

    @Test
    public final void testC_ddResourceTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test2.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.ADD_RESOURCE,
                new Resource(
                    "R_printer_1",
                    "Printer",
                    new URL("http://localhost/printer1"),
                    "printer",
                    false
                ),
                new WildcardNode(
                    Task.class,
                    "T_print_receipt"
                )
            )
        );
        Assert.assertTrue(
            "Artefact should contain node R_printer_1",
            userTasking.containsNode(
                Test2.manager.modelArtefact(MODEL_ID),
                "R_printer_1"
            )
        );
        Assert.assertTrue(
            "Artefact should contain relationship T_print_receipt --> R_printer_1",
            userTasking.containsConnector(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_print_receipt",
                "R_printer_1"
            )
        );
    }

    @Test
    public final void testD_deleteTaskTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test2.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.DELETE_TASK,
                new WildcardNode(Task.class, "T_print_receipt")
            )
        );
        this.print();
        Assert.assertFalse(
            "Artefact should NOT contain node T_print_receipt",
            userTasking.containsNode(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_print_receipt"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain node R_printer_1",
            userTasking.containsNode(
                Test2.manager.modelArtefact(MODEL_ID),
                "R_printer_1"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain relationship T_proceed_checkout --> T_print_receipt",
            userTasking.containsConnector(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_proceed_checkout",
                "T_print_receipt"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain relationship T_print_receipt --> R_printer_1",
            userTasking.containsConnector(
                Test2.manager.modelArtefact(MODEL_ID),
                "T_print_receipt",
                "R_printer_1"
            )
        );
        
    }
    
   
    @Test(expected = ValidationException.class)
    public final void testE_addGoalError()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test2.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.ADD_GOAL,
                new Goal(
                		"G_unvalidGoal",
                		"this should not be added"
                )
            )
        );
     
    }
      
    
    public void print() throws RemoteException, ModelNotFoundException {
    	UCSAGoalGraph graph = (UCSAGoalGraph) Test2.manager.modelArtefact(MODEL_ID);
    	
//    	System.out.println("Nodes:" + graph.origin().nodes().size());
//    	System.out.println("Goal:" + graph.origin().nodes().stream().filter(node -> node instanceof Goal).count());
//    	System.out.println("Task:" + graph.origin().nodes().stream().filter(node -> node instanceof Task).count());
//    	System.out.println("Actor:" + graph.origin().nodes().stream().filter(node -> node instanceof Actor).count());
//    	System.out.println("Resources:" + graph.origin().nodes().stream().filter(node -> node instanceof Resource).count());
//
//    	System.out.println("Connectors:" + graph.origin().connectors().size());
//    	System.out.println("Decomposition:"+ graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.DECOMPOSITION)).count());
//    	System.out.println("Achievement:"+ graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.ACHIEVEMENT)).count());
//    	System.out.println("Depedendency:"+ graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.DEPENDENCY)).count());
//    	System.out.println("Responsibility:"+ graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.RESPONSIBILITY)).count());
//    	
    	/*System.out.println(graph.origin().nodes().size());
    	System.out.println(graph.origin().nodes().stream().filter(node -> node instanceof Goal).count());
    	System.out.println(graph.origin().nodes().stream().filter(node -> node instanceof Task).count());
    	System.out.println(graph.origin().nodes().stream().filter(node -> node instanceof Actor).count());
    	System.out.println(graph.origin().nodes().stream().filter(node -> node instanceof Resource).count());
    	System.out.println(graph.origin().connectors().size());
    	System.out.println(graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.DECOMPOSITION)).count());
    	System.out.println(graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.ACHIEVEMENT)).count());
    	System.out.println(graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.DEPENDENCY)).count());
    	System.out.println(graph.origin().connectors().stream().filter(connector -> connector.type().equals(DirectedArc.Type.RESPONSIBILITY)).count());
    	*/
    	//System.out.println("Nodes: " + graph.origin().nodes());
    	//System.out.println("Conectors: " + graph.origin().connectors());
    }
    
 
    
    private static Manager initialiseManager() throws AccessException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        return (Manager) registry.lookup(RemoteService.MANAGER.toString());
    }

    
    private static void growGraph(int limitNodes) throws MalformedURLException, ValidationException, RemoteException, ModelNotFoundException{
    	
    	UCSAGoalGraph graph = (UCSAGoalGraph) Test2.manager.modelArtefact(MODEL_ID);
        
    	// distributions
    	double dGoal = 0.1, dTask = 0.30, dActor=0.2, dResource = 0.2;
    	double dDependecy = 0.2;
    	
    	int lGoal=(int)Math.round(dGoal*limitNodes),
    		lTask=(int)Math.round(dTask*limitNodes),
    		lActor=(int)Math.round(dActor*limitNodes),
    		lResource=(int)Math.round(dResource*limitNodes); //number of nodes
    	int iGoal=1, iTask=1, iActor=1, iResource=1; //index of nodes (id purposes)
    	
    	
    	Task taskOrigin = null;
		do{
			int randPosition = (int)Math.round(Math.random()*graph.origin()
																	.nodes()
																	.stream()
																	.filter(node -> node instanceof Task)
																	.count()
												)-1;
			//System.out.println(randPosition);
			taskOrigin = (Task) graph.origin()
				.nodes()
				.stream()
				.filter(node -> node instanceof Task)
				.toArray()[(randPosition<0?0:randPosition)];

		}while(taskOrigin==null);
    	
    	for (int i = 0; i < limitNodes; i++) {  		
			if(iGoal < lGoal){
				Goal nGoal = new Goal("G_D"+iGoal, "Dummy Goal");
				
				iGoal++;
			}
			if(iTask < lTask){				
				Task nTask =  new Task("T_D"+iTask, "Dummy Task");
				
				//System.out.println(nTask.toString());
				//System.out.println(origin.toString());
//				graph.apply(new Sentence(GoalsModelOp.ADD_TASK, nTask, taskOrigin));
//		        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, taskOrigin, nTask));
		        
		        Test2.manager.executeSentence(
		            MODEL_ID,
			            new Sentence(
		                GoalsModelOp.ADD_TASK,
			                nTask,
			                taskOrigin
			            )
			        );
		        Test2.manager.executeSentence(
		                MODEL_ID,
		                new Sentence(
		                    GoalsModelOp.ADD_DECOMPOSITION,
		                    taskOrigin,
		                    nTask
		                )
		            );
//		        
				iTask++;
			}
			if(iActor < lActor){
				Actor nActor = new Actor("A_D"+iActor, "Dummy Actor");
				
				iActor++;
			}
			if(iResource < lResource){
				Resource nResource = new Resource("R_D"+iResource, "WebService",new URL("http://apps/dummy"), "dummy resource",false);				
//				graph.apply(new Sentence(GoalsModelOp.ADD_RESOURCE, nResource, taskOrigin));		        
			       Test2.manager.executeSentence(
				            MODEL_ID,
					            new Sentence(
				                GoalsModelOp.ADD_RESOURCE,
					                nResource,
					                taskOrigin
					            )
					        );
				
				iResource++;
			}
		}
    	
    	int lDepedency=(int)Math.round(dDependecy*limitNodes);
    	for(int j = 0 ; j < lDepedency ; j++){
    		Node nodeOrigin = null;
    		Node nodeDestiny = null;
    		do{
    			int randPosition = (int)Math.round(Math.random()*graph.origin()
    																	.nodes()
    																	.stream()
    																	.filter(node -> node instanceof Task)
    																	.count()
    												)-1;
    			//System.out.println(randPosition);
        		nodeOrigin = (Node) graph.origin()
    				.nodes()
    				.stream()
    				.filter(node -> node instanceof Task)
    				.toArray()[(randPosition<0?0:randPosition)];

    		}while(nodeOrigin==null);	
    		
    		do{
    			int randPosition = (int)Math.round(Math.random()*graph.origin()
    																	.nodes()
    																	.stream()
    																	.filter(node -> node instanceof Task)
    																	.count()
    												)-1;
    			//System.out.println(randPosition);
        		nodeDestiny = (Node) graph.origin()
    				.nodes()
    				.stream()
    				.filter(node -> node instanceof Task)
    				.toArray()[(randPosition<0?0:randPosition)];

    		}while(nodeDestiny==null || nodeDestiny.equals(nodeOrigin));	
    		
//    		System.out.println(nodeOrigin.toString());
  //  		System.out.println(nodeDestiny.toString());
    		//graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, nodeOrigin, nodeDestiny));
    	       Test2.manager.executeSentence(
   		            MODEL_ID,
   			            new Sentence(
   		                GoalsModelOp.ADD_DEPENDENCY,
   			                nodeDestiny,
   			                nodeOrigin
   			            )
   			        );
   		
            
    	}
    	  
    }

}
