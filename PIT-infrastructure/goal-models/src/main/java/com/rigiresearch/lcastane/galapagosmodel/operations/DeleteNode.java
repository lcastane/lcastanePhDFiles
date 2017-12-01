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

import com.rigiresearch.lcastane.DirectedArc;
import com.rigiresearch.lcastane.Graph;
import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.framework.Rule;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import java.util.List;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
public final class DeleteNode extends AbstractOperation {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -2074185127072484042L;

    /**
     * Default constructor.
     * @param operationType This operation's type
     * @param rules This operation's pre and post activities
     */
    public DeleteNode(final GoalsModelOp operationType,
        final List<Rule<Graph<DirectedArc>>> rules) {
        super(operationType, rules);
    }

    /**
     * Secondary constructor.
     * @param operationType This operation's type
     * @param rules This operation's pre and post activities
     */
    @SafeVarargs
    public DeleteNode(final GoalsModelOp operationType,
        final Rule<Graph<DirectedArc>>... rules) {
        super(operationType, rules);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.goals.operations.AbstractOperation
     *  #apply(com.rigiresearch.lcastane.framework.goals.Graph,
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public void applyOp(Graph<DirectedArc> artefact, Node... operands)
        throws ValidationException {
        artefact.nodes().remove(operands[0]);
    }

}
