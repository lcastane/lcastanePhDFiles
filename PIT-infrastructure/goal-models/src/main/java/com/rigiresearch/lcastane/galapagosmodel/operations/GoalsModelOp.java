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
package com.rigiresearch.lcastane.galapagosmodel.operations;

import com.rigiresearch.lcastane.framework.Operation.OperationType;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Actor;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Goal;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Resource;
import com.rigiresearch.lcastane.galapagosmodel.nodes.Task;

/**
 * Represents the type of operations supported by the goals model.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public enum GoalsModelOp implements OperationType {
    /**
     * Adds a new {@link Actor}.
     */
    ADD_ACTOR,

    /**
     * Adds a new {@link Goal}.
     */
    ADD_GOAL,

    /**
     * Adds a new {@link Resource}
     */
    ADD_RESOURCE,

    /**
     * Adds a new {@link Task}
     */
    ADD_TASK,

    /**
     * Adds a new achievement.
     */
    ADD_ACHIEVEMENT,

    /**
     * Adds a new decomposition.
     */
    ADD_DECOMPOSITION,
    
    /**
     * Adds a new dependency.
     */
    ADD_DEPENDENCY,

    /**
     * Adds a new responsibility.
     */
    ADD_RESPONSIBILITY,

    /**
     * Deletes a {@link Goal}.
     */
    DELETE_GOAL,

    /**
     * Deletes a {@link Task}.
     */
    DELETE_TASK,

    /**
     * Deletes a {@link Resource}.
     */
    DELETE_RESOURCE,

    /**
     * Updates a {@link Goal}.
     */
    UPDATE_GOAL;
}
