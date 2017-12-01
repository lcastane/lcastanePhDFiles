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
import com.rigiresearch.lcastane.galapagosmodel.Flag;
import com.rigiresearch.lcastane.galapagosmodel.nodes.AbstractNode;

/**
 * TODO
 * @date 2017-07-05
 * @version $Id$
 * @since 0.0.1
 */
public class WaterfallRemovalValidation extends AbstractRule {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1176584676719227273L;

    /**
     * The specific arc type to be removed.
     */
    private final DirectedArc.Type arcType;

    /**
     * The node type to look for in the arcs.
     */
    private final DirectedArc.NodeType nodeType;

    /**
     * Default constructor.
     */
    public WaterfallRemovalValidation(final Rule.Type type,
        final DirectedArc.Type arcType,
        final DirectedArc.NodeType nodeType) {
        super(
            type,
            "Waterfal Removal Validation",
            "Unknown error"
        );
        this.arcType = arcType;
        this.nodeType = nodeType;
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.goalsmodel.validation.AbstractRule
     *  #apply(com.rigiresearch.lcastane.Graph,
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public boolean apply(Graph<DirectedArc> artefact, Node... operands) {
        artefact.connectors()
            .stream()
            .filter(arc -> arc.type().equals(this.arcType))
            .filter(arc -> this.nodeType.select.apply(arc).equals(operands[0]))
            .forEach(arc -> {
                AbstractNode node = (AbstractNode) this.nodeType.opposite().select.apply(arc);
                node.flag(Flag.REMOVE, true);
                arc.flag(Flag.REMOVE, true);
            });
        // TODO Validate waterfall removal
        return true;
    }

}
