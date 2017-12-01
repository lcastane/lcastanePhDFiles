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
import com.rigiresearch.lcastane.framework.Operation;
import com.rigiresearch.lcastane.framework.Rule;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import com.rigiresearch.lcastane.galapagosmodel.GoalsModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;

/**
 * Represents an abstract operation in the context of a {@link GoalsModel}.
 * @date 2017-06-14
 * @version $Id$
 * @since 0.0.1
 */
@EqualsAndHashCode
public abstract class AbstractOperation implements Operation<Graph<DirectedArc>> {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -9149720813150556327L;

    /**
     * This operation's validation rules.
     */
    private final List<Rule<Graph<DirectedArc>>> rules;

    /**
     * This operation's type.
     */
    private final GoalsModelOp operationType;

    /**
     * Default constructor.
     * @param operationType This operation's type
     * @param rules This operation's pre and post activities
     */
    public AbstractOperation(final GoalsModelOp operationType,
        final List<Rule<Graph<DirectedArc>>> rules) {
        this.operationType = operationType;
        this.rules = rules;
    }

    /**
     * Secondary constructor.
     * @param operationType This operation's type
     * @param rules This operation's pre and post activities
     */
    @SafeVarargs
    public AbstractOperation(final GoalsModelOp operationType,
        final Rule<Graph<DirectedArc>>... rules) {
        this(operationType, Arrays.asList(rules));
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Operation#type()
     */
    @Override
    public OperationType type() {
        return this.operationType;
    }

    /**
     * Runs {@code PRE} and {@code POST rules} before applying the operation.
     * @param graph The artefact
     * @param operands This operation's arguments
     * @throws ValidationException If there is at least one failing rule
     */
    @Override
    public void apply(Graph<DirectedArc> graph, Node... operands)
            throws ValidationException {
        runRules(Rule.Type.PRE, graph, operands);
        this.applyOp(graph, operands);
        runRules(Rule.Type.POST, graph, operands);
    }

    /**
     * Executes the operation.
     * @param graph The artefact
     * @param operands This operation's arguments
     * @throws ValidationException If there is at least one failing rule
     */
    protected abstract void applyOp(Graph<DirectedArc> graph, Node... operands)
        throws ValidationException;

    /**
     * Executes the specified set of rules.
     * @param type The type of rules to execute
     * @param graph The artefact
     * @param operands This operation's arguments
     * @throws ValidationException If there is at least one failing rule
     */
    private void runRules(final Rule.Type type, final Graph<DirectedArc> graph,
        final Node... operands) throws ValidationException {
        Optional<Rule<Graph<DirectedArc>>> failingRule = this.rules
                .stream()
                .filter(rule -> rule.type().equals(type))
                .filter(rule -> !rule.apply(graph, operands))
                .findFirst();
            if (failingRule.isPresent())
                throw new ValidationException(graph, this, failingRule.get());
    }

}
