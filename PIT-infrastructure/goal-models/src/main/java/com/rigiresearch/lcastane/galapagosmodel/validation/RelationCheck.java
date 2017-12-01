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
package com.rigiresearch.lcastane.galapagosmodel.validation;

import com.rigiresearch.lcastane.DirectedArc;
import com.rigiresearch.lcastane.Graph;
import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.framework.Rule;
import java.util.Optional;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
public final class RelationCheck<T extends Node> extends AbstractRule {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -8119831145642530054L;

    /**
     * T's class
     */
    private final Class<T> expectedType;

    /**
     * The expected type of relation.
     */
    private final DirectedArc.Type expectedRelation;

    public RelationCheck(final Rule.Type type,
        final Class<T> expectedType, final DirectedArc.Type expectedRelation) {
        super(
            type,
            "Relation Check",
            String.format(
                "Expected relation '%s' does not exist",
                expectedRelation.name()
            )
        );
        this.expectedType = expectedType;
        this.expectedRelation = expectedRelation;
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Rule
     *  #apply(com.rigiresearch.lcastane.framework.Artefact,
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public boolean apply(Graph<DirectedArc> artefact, Node... operands) {
        Optional<T> relatedNode = artefact.nodes()
            .stream()
            .filter(node -> node.getClass().equals(this.expectedType))
            .map(node -> expectedType.cast(node))
            .filter(node -> node.equals(operands[1]))
            .findFirst();
        Optional<DirectedArc> connector = artefact.connectors()
            .stream()
            .filter(arc -> arc.type().equals(this.expectedRelation))
            .filter(arc -> {
                return arc.origin().equals(operands[0])
                        && arc.destiny().equals(operands[1]);
            })
            .findFirst();
        return relatedNode.isPresent() && connector.isPresent();
    }

}
