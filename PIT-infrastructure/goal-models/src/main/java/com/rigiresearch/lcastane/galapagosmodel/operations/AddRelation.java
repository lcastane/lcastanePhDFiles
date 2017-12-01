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
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 * @author Miguel Jimenez (miguel@uvic.ca)
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
public class AddRelation extends AbstractOperation {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 4431635666407407721L;

    /**
     * The kind of arc type to create.
     */
    private final DirectedArc.Type arcType;

    /**
     * Default constructor.
     * @param rules
     */
    public AddRelation(final DirectedArc.Type arcType,
        final List<Rule<Graph<DirectedArc>>> rules) {
        super(
            GoalsModelOp.valueOf(
                String.format(
                    "ADD_%s",
                    arcType.name()
                )
            ),
            rules
        );
        this.arcType = arcType;
    }

    /**
     * Secondary constructor.
     * @param rules This operation's pre and post activities
     */
    @SafeVarargs
    public AddRelation(final DirectedArc.Type arcType,
        final Rule<Graph<DirectedArc>>... rules) {
        this(arcType, Arrays.asList(rules));
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.goalsmodel.operations.AbstractOperation
     *  #apply(com.rigiresearch.lcastane.goalsmodel.Graph, 
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public void applyOp(Graph<DirectedArc> graph, Node... operands)
        throws ValidationException {
        graph.addArc(
            new DirectedArc(
                operands[0],
                operands[1],
                this.arcType
            )
        );
    }
}
