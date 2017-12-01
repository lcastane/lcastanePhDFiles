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

import com.rigiresearch.lcastane.framework.Artefact;
import com.rigiresearch.lcastane.framework.MART;
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
public final class Test1 {

    private static final String MODEL_ID = "goalsmodel";
    
    private static Manager manager;
    private static InstanceA userTasking = new InstanceA();
    //private static InstanceB userTasking = new InstanceB();
    //private static InstanceC userTasking = new InstanceC();

    /**
     * TODO
     * @throws RemoteException 
     * @throws NotBoundException 
     */
    @BeforeClass
    public static void pre() throws RemoteException, NotBoundException {
    	
        Test1.manager = Test1.initialiseManager();
        Test1.manager.registerModel(MODEL_ID, userTasking.model());
       
    }
    
    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[10][0]);
    }


    @Test
    public final void testA_addTaskTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test1.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.ADD_TASK,
                new Task("T_print_receipt", "Print receipt"),
                new WildcardNode(Task.class, "T_proceed_checkout")
            )
        );
        //this.print();
        Assert.assertTrue(
            "Artefact should contain node T_print_receipt",
            userTasking.containsNode(
                Test1.manager.modelArtefact(MODEL_ID),
                "T_print_receipt"
            )
        );
    }

    @Test
    public final void testB_addDecompositionTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test1.manager.executeSentence(
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
                Test1.manager.modelArtefact(MODEL_ID),
                "T_proceed_checkout",
                "T_print_receipt"
            )
        );
    }

    @Test
    public final void testC_ddResourceTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test1.manager.executeSentence(
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
                Test1.manager.modelArtefact(MODEL_ID),
                "R_printer_1"
            )
        );
        Assert.assertTrue(
            "Artefact should contain relationship T_print_receipt --> R_printer_1",
            userTasking.containsConnector(
                Test1.manager.modelArtefact(MODEL_ID),
                "T_print_receipt",
                "R_printer_1"
            )
        );
    }

    @Test
    public final void testD_deleteTaskTest()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test1.manager.executeSentence(
            MODEL_ID,
            new Sentence(
                GoalsModelOp.DELETE_TASK,
                new WildcardNode(Task.class, "T_print_receipt")
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain node T_print_receipt",
            userTasking.containsNode(
                Test1.manager.modelArtefact(MODEL_ID),
                "T_print_receipt"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain node R_printer_1",
            userTasking.containsNode(
                Test1.manager.modelArtefact(MODEL_ID),
                "R_printer_1"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain relationship T_proceed_checkout --> T_print_receipt",
            userTasking.containsConnector(
                Test1.manager.modelArtefact(MODEL_ID),
                "T_proceed_checkout",
                "T_print_receipt"
            )
        );
        Assert.assertFalse(
            "Artefact should NOT contain relationship T_print_receipt --> R_printer_1",
            userTasking.containsConnector(
                Test1.manager.modelArtefact(MODEL_ID),
                "T_print_receipt",
                "R_printer_1"
            )
        );
    }

    
    @Test(expected = ValidationException.class)
    public final void testE_addGoalError()
        throws RemoteException, ValidationException,
            ModelNotFoundException, MalformedURLException {
        Test1.manager.executeSentence(
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
    	UCSAGoalGraph graph = (UCSAGoalGraph) Test1.manager.modelArtefact(MODEL_ID);
    	System.out.println("Nodes: " + graph.origin().nodes());
    	System.out.println("Conectors: " + graph.origin().connectors());
    }
    
 
    
    private static Manager initialiseManager() throws AccessException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        return (Manager) registry.lookup(RemoteService.MANAGER.toString());
    }

}
