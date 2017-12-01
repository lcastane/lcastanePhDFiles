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
package com.rigiresearch.lcastane;

import com.rigiresearch.lcastane.framework.Artefact;
import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.framework.Operation;
import com.rigiresearch.lcastane.framework.Operation.OperationType;
import com.rigiresearch.lcastane.framework.Sentence;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Represents an undirected graph.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
@Accessors(fluent = true)
@EqualsAndHashCode
@Getter
public final class Graph<T extends Connector> implements Artefact {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -2129396913139022694L;

    /**
     * The set of connectors.
     */
    private final List<T> connectors;

    /**
     * The set of nodes.
     */
    private final List<Node> nodes;

    /**
     * The list of supported operations.
     */
    private final Map<OperationType, Operation<Graph<T>>> operations;

    /**
     * Default constructor.
     * @param nodes This graph's nodes
     * @param connectors This graph's connectors
     * @param operations The list of supported operations.
     */
    public Graph(final List<Node> nodes, final List<T> connectors,
        final List<Operation<Graph<T>>> operations) {
        this.nodes = nodes;
        this.connectors = connectors;
        this.operations = operations.stream()
            .collect(Collectors.toMap(
                operation -> operation.type(),
                operation -> operation
            ));
    }

    /**
     * Instantiates an empty graph.
     * @param operations The list of supported operations.
     */
    public Graph(final List<Operation<Graph<T>>> operations) {
        this(new ArrayList<>(), new ArrayList<>(), operations);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Artefact#name()
     */
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * Applies the given function to this graph's connectors.
     * @param consumer A void function that receives as arc as parameter
     */
    public void forEach(Consumer<Connector> consumer) {
        this.connectors
            .stream()
            .forEach(consumer);
    }

    /**
     * Adds a new connector.
     * @param connector The new connector
     */
    public void addArc(final T connector) {
        this.connectors.add(connector);
    }

    /**
     * Adds a new node.
     * @param node The new node
     */
    public void addNode(final Node node) {
        this.nodes.add(node);
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Artefact
     *  #apply(com.rigiresearch.lcastane.framework.Sentence)
     */
    @Override
    public void apply(Sentence sentence)
        throws ValidationException {
        this.operations.get(sentence.operationType())
            .apply(this, sentence.operands());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
            "Nodes: %s\nArcs: %s",
            this.nodes.toString(),
            this.connectors.toString()
        );
    }
    
    

}
