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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * TODO
 * @date 2017-07-12
 * @version $Id$
 * @since 0.0.1
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class InstanceB {

    private static final String MODEL_ID = "goalsmodel";
    
    public InstanceB(){
    	
    }
    
    public boolean containsNode(final Artefact artefact, final String identifier) {
        UCSAGoalGraph graph = (UCSAGoalGraph) artefact;
        return graph.origin()
            .nodes()
            .stream()
            .filter(node -> node.id().equals(identifier))
            .count() == 1;
    }
    
    public boolean containsConnector(final Artefact artefact,
        final String idOrigin, final String idDestiny) {
        UCSAGoalGraph graph = (UCSAGoalGraph) artefact;
        return graph.origin()
            .connectors()
            .stream()
            .filter(arc -> arc.origin().id().equals(idOrigin) && arc.destiny().id().equals(idDestiny))
            .count() == 1;
    }
    
    public static UCSAGoalGraph artefact() throws ValidationException {
        final UCSAGoalGraph graph = new UCSAGoalGraph();
        Goal goal = new Goal("G_grocery_shopping", "Online grocery shopping");    
       
        Actor SUSGroceries = new Actor("A_SUSGroceries", "SUSGroceries");
        Actor groceryListApp = new Actor("A_grocery_list_app", "Grocery list application");
        Actor user = new Actor("A_user", "User");
        Actor groceryWebService = new Actor("A_grocery_web_service", "Grocery store Web Service");
        
        Task shopGroceryTask 	= new Task("T_shop_grocery_task", "Shop grocery task");
        Task groceryListTask 	= new Task("T_grocery_list", "Get grocery items list");
        Task findBestDeals 		= new Task("T_find_deals", "Find the best deals in store");				//New Task
        Task matchItemsTask 	= new Task("T_match_items", "Match items with catalogue");
        Task purchaseItemsTask 	= new Task("T_purchase_items", "Purchase items per store");
        Task checkoutTask 		= new Task("T_proceed_checkout", "Proceed with checkout");
        Task updateListTask 	= new Task("T_update_list", "Update grocery list");
                
        
        Resource listItemsResource = null;
        Resource storeCatalogueResource = null;
        Resource storeCartResource = null;
        try {
            listItemsResource = new Resource(
                "R_list_items",
                "WebService",
                new URL("http://apps/list"),
                "list",
                true
            );
            storeCatalogueResource = new Resource(
                "R_store_catalogue",
                "WebService",
                new URL("http://apps/catalogue"),
                "catalogue",
                false
            );
            storeCartResource = new Resource(
                "R_store_cart",
                "WebService",
                new URL("http://apps/cart"),
                "cart",
                true
            );
        } catch (MalformedURLException e) {}
     
        // Online Grocery Shopping
        graph.apply(new Sentence(GoalsModelOp.ADD_GOAL, goal));
     
        // SUSGroceries, SUSGroceries --r--> Online Grocery Shopping
        graph.apply(new Sentence(GoalsModelOp.ADD_ACTOR, SUSGroceries, goal));
     
        // Shop Grocery Task, Shop Grocery Task --a--> Online Grocery Shopping
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, shopGroceryTask, goal));
        graph.apply(new Sentence(GoalsModelOp.ADD_ACHIEVEMENT, shopGroceryTask, goal));
     
        // Get grocery items list, Shop grocery task --dc--> Get grocery items list
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, groceryListTask, shopGroceryTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, shopGroceryTask, groceryListTask));
     
        // Find best deals, Shop grocery task --dc--> Find best deals
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, findBestDeals, shopGroceryTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, shopGroceryTask, findBestDeals));
     
        // Purchase items per store, Shop grocery task --dc--> Purchase items per store
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, purchaseItemsTask, shopGroceryTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, shopGroceryTask, purchaseItemsTask));
        
        // List items, Get grocery items list --> List items
        graph.apply(new Sentence(GoalsModelOp.ADD_RESOURCE, listItemsResource, groceryListTask));
        
        // Grocery list application, Grocery list application --r--> List items
        graph.apply(new Sentence(GoalsModelOp.ADD_ACTOR, groceryListApp, listItemsResource));
        
        
        // Proceed with checkout
        // Purchase items per store --dc--> Proceed with checkout
        // Proceed with checkout --dp--> Search proper stores
        // Proceed with checkout --r--> User
        // Proceed with checkout --dp--> Store shopping cart
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, checkoutTask, purchaseItemsTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, purchaseItemsTask, checkoutTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, checkoutTask, findBestDeals));
        graph.apply(new Sentence(GoalsModelOp.ADD_RESPONSIBILITY, checkoutTask, user));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, checkoutTask, storeCartResource));
        
        // Grocery store Web Service, Store shopping cart --r--> Grocery store Web Service
        // Store catalogue item --r--> Grocery store Web Service
        graph.apply(new Sentence(GoalsModelOp.ADD_ACTOR, groceryWebService, storeCartResource));
        graph.apply(new Sentence(GoalsModelOp.ADD_RESPONSIBILITY, storeCatalogueResource, groceryWebService));
        
        // Update grocery list, Proceed with checkout
        // Proceed with checkout --dc--> Update grocery list
        // Update grocery list --dp--> List items
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, updateListTask, checkoutTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, checkoutTask, updateListTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, updateListTask, listItemsResource));
        
        // Match items with catalogue
        // Rank and filter store --dc--> Match items with catalogue
        // Match items with catalogue --dp--> Store catalogue item
        // Match items with catalogue --dp--> Get grocery items list
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, matchItemsTask, findBestDeals));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, findBestDeals, matchItemsTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, matchItemsTask, storeCatalogueResource));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, matchItemsTask, groceryListTask));
        return graph;
    }

    protected static MART<?, ?> model() {
        try {
            return new GoalsModel(
                new IStarPIT(""),
                InstanceB.artefact()
            );
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }
    

}
