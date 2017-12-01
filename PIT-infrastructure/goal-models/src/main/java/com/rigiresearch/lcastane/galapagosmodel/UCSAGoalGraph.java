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
package com.rigiresearch.lcastane.galapagosmodel;

import static com.rigiresearch.lcastane.DirectedArc.NodeType.DESTINY;
import static com.rigiresearch.lcastane.DirectedArc.NodeType.ORIGIN;
import static com.rigiresearch.lcastane.framework.Rule.Type.POST;
import static com.rigiresearch.lcastane.framework.Rule.Type.PRE;
import com.rigiresearch.lcastane.DirectedArc;
import com.rigiresearch.lcastane.DirectedArc.Type;
import com.rigiresearch.lcastane.Graph;
import com.rigiresearch.lcastane.framework.Artefact;
import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.framework.Sentence;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Actor;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Goal;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Resource;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Task;
import com.rigiresearch.lcastane.galapagosmodel.nodes.WildcardNode;
import com.rigiresearch.lcastane.galapagosmodel.operations.AddNode;
import com.rigiresearch.lcastane.galapagosmodel.operations.AddRelation;
import com.rigiresearch.lcastane.galapagosmodel.operations.DeleteNode;
import com.rigiresearch.lcastane.galapagosmodel.operations.GoalsModelOp;
import com.rigiresearch.lcastane.galapagosmodel.operations.UpdateGoal;
import com.rigiresearch.lcastane.galapagosmodel.validation.AlternativeRule;
import com.rigiresearch.lcastane.galapagosmodel.validation.ArcCreation;
import com.rigiresearch.lcastane.galapagosmodel.validation.ArcRemovalValidation;
import com.rigiresearch.lcastane.galapagosmodel.validation.RootNodeCheck;
import com.rigiresearch.lcastane.galapagosmodel.validation.TypesValidation;
import com.rigiresearch.lcastane.galapagosmodel.validation.WaterfalRemoval;

import java.util.Arrays;
import java.util.Iterator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
@Accessors(fluent = true)
@EqualsAndHashCode
@Getter
public final class UCSAGoalGraph implements Artefact {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 7078677059012928312L;

    /**
     * The decorated (generic) graph.
     */
    private Graph<DirectedArc> origin;

    /**
     * Default constructor.
     */
    public UCSAGoalGraph() {
        this.origin = new Graph<DirectedArc>(
            Arrays.asList(
                new AddNode(
                    GoalsModelOp.ADD_GOAL,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Goal.class),
                        new TypesValidation(Goal.class, Goal.class)
                    ),
                    new RootNodeCheck(PRE),
                    new ArcCreation(POST, Type.DECOMPOSITION, true)
                ),
                new AddNode(
                    GoalsModelOp.ADD_TASK,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Task.class, Task.class),
                        new TypesValidation(Task.class, Goal.class)
                    )
                ),
                new AddNode(
                    GoalsModelOp.ADD_RESOURCE,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Resource.class, Actor.class),
                        new TypesValidation(Resource.class, Resource.class),
                        new TypesValidation(Resource.class, Task.class)
                    ),
                    new ArcCreation(POST, Type.DEPENDENCY, true)
                ),
                new AddNode(
                    GoalsModelOp.ADD_ACTOR,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Actor.class, Goal.class),
                        new TypesValidation(Actor.class, Task.class),
                        new TypesValidation(Actor.class, Resource.class)
                    ),
                    new ArcCreation(POST, Type.RESPONSIBILITY, true)
                ),
                new UpdateGoal(
                    new TypesValidation(Goal.class, String.class, Object.class)
                ),
                new DeleteNode(
                    GoalsModelOp.DELETE_GOAL,
                    new TypesValidation(Goal.class),
                    new ArcRemovalValidation(PRE, Type.DEPENDENCY, DESTINY),
                    new WaterfalRemoval(POST, Type.ACHIEVEMENT, DESTINY),
                    new WaterfalRemoval(POST, Type.DECOMPOSITION, DESTINY)
                ),
                new DeleteNode(
                    GoalsModelOp.DELETE_TASK,
                    new TypesValidation(Task.class),
                    new ArcRemovalValidation(PRE, Type.DEPENDENCY, DESTINY),
                    new WaterfalRemoval(POST, Type.DECOMPOSITION, ORIGIN),
                    new WaterfalRemoval(POST, Type.DEPENDENCY, ORIGIN, Resource.class)
                ),
                new DeleteNode(
                    GoalsModelOp.DELETE_RESOURCE,
                    new TypesValidation(Resource.class),
                    new ArcRemovalValidation(PRE, Type.DEPENDENCY, DESTINY)
                ),
                new AddRelation(
                    Type.DEPENDENCY,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Goal.class, Goal.class),
                        new TypesValidation(Task.class, Task.class),
                        new TypesValidation(Task.class, Resource.class),
                        new TypesValidation(Resource.class, Resource.class)
                    )
                ),
                new AddRelation(
                    Type.ACHIEVEMENT,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Task.class, Goal.class)
                    )
                ),
                new AddRelation(
                    Type.DECOMPOSITION,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Goal.class, Goal.class),
                        new TypesValidation(Task.class, Task.class),
                        new TypesValidation(Task.class, Resource.class),
                        new TypesValidation(Resource.class, Resource.class)
                    )
                ),
                new AddRelation(
                    Type.RESPONSIBILITY,
                    new AlternativeRule(
                        PRE,
                        new TypesValidation(Goal.class, Actor.class),
                        new TypesValidation(Task.class, Actor.class),
                        new TypesValidation(Resource.class, Actor.class)
                    )
                )
            )
        );
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Artefact#name()
     */
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Artefact
     *  #apply(com.rigiresearch.lcastane.framework.Sentence)
     */
    @Override
    public void apply(Sentence sentence)
        throws ValidationException {
        Node[] operands = sentence.operands();
        for (int i = 0; i < operands.length; i++) {
            if (operands[i] instanceof WildcardNode) {
                operands[i] = ((WildcardNode) operands[i]).actualNode(this);
            }
        }
        sentence.operands(operands);
        this.origin.apply(sentence);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.origin.toString();
    }
    
	public Node findNode(String identifier){	    	
    	Iterator iter = this.origin.nodes().iterator();
    	do{
    		Node aNode = (Node)iter.next();
    		//System.out.println(aNode.id());
    		if(aNode.id().equals(identifier)) return aNode;    		
    	}while(iter.hasNext());
    	return null;
    }

}
