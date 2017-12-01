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
import com.rigiresearch.lcastane.framework.Sentence;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import com.rigiresearch.lcastane.galapagosmodel.nodes.AbstractNode;
import com.rigiresearch.lcastane.galapagosmodel.operations.GoalsModelOp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
public final class WaterfalRemoval extends AbstractRule {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -109131535376686299L;

    /**
     * The specific arc type to be removed.
     */
    private final DirectedArc.Type arcType;

    /**
     * The node type to look for in the arcs.
     */
    private final DirectedArc.NodeType nodeType;

    /**
     * The type of target connected nodes.
     * This is to decide which nodes (connected to the argument) should be
     * deleted.
     */
    private final Class<? extends Node> connectedNodeType;

    /**
     * Default constructor.
     */
    public WaterfalRemoval(final Rule.Type type, final DirectedArc.Type arcType,
        final DirectedArc.NodeType nodeType, Class<? extends Node> connectedNodeType) {
        super(
            type,
            "Waterfal Removal",
            "Unknown error"
        );
        this.arcType = arcType;
        this.nodeType = nodeType;
        this.connectedNodeType = connectedNodeType;
    }

    public WaterfalRemoval(final Rule.Type type, final DirectedArc.Type arcType,
            final DirectedArc.NodeType nodeType) {
        this(type, arcType, nodeType, Node.class);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Rule
     *  #apply(com.rigiresearch.lcastane.framework.Artefact,
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public boolean apply(Graph<DirectedArc> artefact, Node... operands) {
        // TODO flag connectors first
        // .filter(arc -> arc.flags().containsKey(Flag.REMOVE))
        List<DirectedArc> arcs = artefact.connectors()
            .stream()
            .filter(arc -> arc.type().equals(this.arcType))
            .filter(arc -> this.nodeType.select.apply(arc).equals(operands[0]))
            .collect(Collectors.toList());
        artefact
            .connectors()
            .removeAll(arcs);
        // Not all nodes are removed
        arcs.forEach(arc -> {
                AbstractNode node = (AbstractNode) this.nodeType
                    .opposite()
                    .select.apply(arc);
                if (!connectedNodeType.isInstance(node))
                    return;
                GoalsModelOp operation = GoalsModelOp.valueOf(
                    String.format(
                        "DELETE_%s",
                        node.getClass()
                            .getSimpleName()
                            .toUpperCase()
                    )
                );
                try {
                    // TODO flag nodes first
                    // if (node.flags().containsKey(Flag.REMOVE))
                    artefact.apply(
                        new Sentence(
                            operation,
                            node
                        )
                    );
                } catch (ValidationException e) {
                    // Do nothing
                }
            });
        // Remove the rest of the arcs
        List<DirectedArc> rest = artefact.connectors()
            .stream()
            .filter(arc -> arc.type().equals(this.arcType))
            .filter(arc -> this.nodeType.opposite().select.apply(arc).equals(operands[0]))
            .collect(Collectors.toList());
        artefact
            .connectors()
            .removeAll(rest);
        return true;
    }

}
