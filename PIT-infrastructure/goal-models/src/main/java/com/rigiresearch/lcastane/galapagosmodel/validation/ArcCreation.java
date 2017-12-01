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
import com.rigiresearch.lcastane.galapagosmodel.operations.GoalsModelOp;

/**
 * TODO
 * @date 2017-07-05
 * @version $Id$
 * @since 0.0.1
 */
public final class ArcCreation extends AbstractRule {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5172051403840041363L;

    /**
     * The type of arc.
     */
    private final DirectedArc.Type arcType;

    /**
     * Whether the operation arguments should be inverted before creating the
     * arc.
     */
    private final boolean inverted;

    /**
     * Default constructor.
     */
    public ArcCreation(final Rule.Type type,
        final DirectedArc.Type arcType, final boolean inverted) {
        super(
            type,
            "Relation Creation",
            "Unknown error"
        );
        this.arcType = arcType;
        this.inverted = inverted;
    }

    /**
     * Secondary constructor.
     */
    public ArcCreation(final Rule.Type type,
            final DirectedArc.Type arcType) {
        this(type, arcType, false);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.goalsmodel.validation.AbstractRule
     *  #apply(com.rigiresearch.lcastane.Graph,
     *  com.rigiresearch.lcastane.framework.Node[])
     */
    @Override
    public boolean apply(Graph<DirectedArc> artefact, Node... operands) {
        if (operands.length == 1)
            return true; // arc is optional
        Node[] _operands = this.inverted
                ? new Node[] { operands[1], operands[0] } : operands;
        try {
            artefact.apply(
                new Sentence(
                    GoalsModelOp.valueOf(
                        String.format(
                            "ADD_%s",
                            this.arcType.name()
                        )
                    ),
                    _operands
                )
            );
        } catch (ValidationException e) {
            this.message = e.getMessage();
            return false;
        }
        return true;
    }

}
