package com.rigiresearch.lcastane.susgroceries.app;
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

import com.rigiresearch.lcastane.framework.Artefact;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.cactoos.io.InputAsBytes;
import org.cactoos.io.InputStreamAsInput;
import org.cactoos.text.BytesAsText;

/**
 * TODO
 * @date 2017-07-11
 * @version $Id$
 * @since 0.0.1
 */
public class SUSGroceriesClient {

    /**
     * A remote instance of Primor's manager.
     */
    private final Manager manager;

    /**
     * Default constructor.
     * @param primorManager A remote instance of Primor's manager
     */
    public SUSGroceriesClient(final Manager primorManager) {
        this.manager = primorManager;
    }

    /**
     * Generic example.
     * @throws ValidationException If there is a problem initialising the model
     * @throws IOException If there is a problem reading the notation file
     * @throws ModelNotFoundException If the model is not found
     */
    public void doSomething() throws IOException, ValidationException, ModelNotFoundException {
        final String modelId = "goalsmodel";
        // Do not register the model twice
        if (!this.manager.modelRegistered("goalsmodel")) {
            System.out.println("The goals model has not been registered yet");
            this.manager.registerModel(
                modelId,
                new GoalsModel(
                    new IStarPIT(
                        new BytesAsText(
                            new InputAsBytes(
                                new InputStreamAsInput(
                                    getClass()
                                        .getClassLoader()
                                        .getResourceAsStream("example.istarml")
                                )
                            )
                        ).asString()
                    ),
                    this.initialiseArtefact()
                )
            );
            System.out.println("Resulting model:");
            System.out.println(this.manager.modelArtefact(modelId));
        } else {
            System.out.println("The goals model is already registered");
        }
        this.manager.executeSentence(
            modelId,
            new Sentence(
                GoalsModelOp.ADD_TASK,
                new Task("T_print_receipt", "Print receipt"),
                new WildcardNode(Task.class, "T_proceed_checkout")
            )
        );
        this.manager.executeSentence(
            modelId,
            new Sentence(
                GoalsModelOp.ADD_DECOMPOSITION,
                new WildcardNode(Task.class, "T_proceed_checkout"),
                new WildcardNode(Task.class, "T_print_receipt")
            )
        );
        this.manager.executeSentence(
                modelId,
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
        System.out.println("Model after operations");
        System.out.println(this.manager.modelArtefact(modelId));
    }

    /**
     * TODO replace this method by implementing synchronisation between
     * artefact and notation.
     * @return An {@link Artefact}
     * @throws ValidationException If there is a problem initialising the artefact
     */
    private UCSAGoalGraph initialiseArtefact() throws ValidationException {
        final UCSAGoalGraph graph = new UCSAGoalGraph();
        Goal goal = new Goal("G_grocery_shopping", "Online grocery shopping");
        Actor SUSGroceries = new Actor("A_SUSGroceries", "SUSGroceries");
        Actor groceryListApp = new Actor("A_grocery_list_app", "Grocery list application");
        Actor user = new Actor("A_user", "User");
        Actor groceryWebService = new Actor("A_grocery_web_service", "Grocery store Web Service");
        Task shopGroceryTask = new Task("T_shop_grocery_task", "Shop grocery task");
        Task groceryListTask = new Task("T_grocery_list", "Get grocery items list");
        Task searchStoresTask = new Task("T_search_stores", "Search proper stores");
        Task purchaseItemsTask = new Task("T_purchase_items", "Purchase items per store");
        Task rankFilterTask = new Task("T_rank_filter", "Rank and filter store");
        Task checkoutTask = new Task("T_proceed_checkout", "Proceed with checkout");
        Task updateListTask = new Task("T_update_list", "Update grocery list");
        Task matchItemsTask = new Task("T_match_items", "Match items with catalogue");
        Resource listItemsResource = null;
        Resource storeCatalogueResource = null;
        Resource storeCartResource = null;
        try {
            listItemsResource = new Resource(
                "R_list_items",
                "WebService",
                new URL("http://primor/list"),
                "list",
                true
            );
            storeCatalogueResource = new Resource(
                "R_store_catalogue",
                "WebService",
                new URL("http://primor/catalogue"),
                "catalogue",
                false
            );
            storeCartResource = new Resource(
                "R_store_cart",
                "WebService",
                new URL("http://primor/cart"),
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
     
        // Search proper stores, Shop grocery task --dc--> Search proper stores
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, searchStoresTask, shopGroceryTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, shopGroceryTask, searchStoresTask));
     
        // Purchase items per store, Shop grocery task --dc--> Purchase items per store
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, purchaseItemsTask, shopGroceryTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, shopGroceryTask, purchaseItemsTask));
        
        // List items, Get grocery items list --> List items
        graph.apply(new Sentence(GoalsModelOp.ADD_RESOURCE, listItemsResource, groceryListTask));
        
        // Grocery list application, Grocery list application --r--> List items
        graph.apply(new Sentence(GoalsModelOp.ADD_ACTOR, groceryListApp, listItemsResource));
        
        // Rank and filter store, Search proper stores --dc--> Rank and filter store
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, rankFilterTask, searchStoresTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, searchStoresTask, rankFilterTask));
        
        // Proceed with checkout
        // Purchase items per store --dc--> Proceed with checkout
        // Proceed with checkout --dp--> Search proper stores
        // Proceed with checkout --r--> User
        // Proceed with checkout --dp--> Store shopping cart
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, checkoutTask, purchaseItemsTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, purchaseItemsTask, checkoutTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, checkoutTask, searchStoresTask));
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
        graph.apply(new Sentence(GoalsModelOp.ADD_TASK, matchItemsTask, rankFilterTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DECOMPOSITION, rankFilterTask, matchItemsTask));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, matchItemsTask, storeCatalogueResource));
        graph.apply(new Sentence(GoalsModelOp.ADD_DEPENDENCY, matchItemsTask, groceryListTask));
        return graph;
    }

    public static void main(String[] args)
        throws AccessException, RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(1099);
        try {
        	SUSGroceriesClient application = new SUSGroceriesClient(
                (Manager) registry.lookup(RemoteService.MANAGER.toString())
            );
            application.doSomething();
        } catch (ModelNotFoundException | ValidationException | IOException e) {
            e.printStackTrace();
        }
    }

}
