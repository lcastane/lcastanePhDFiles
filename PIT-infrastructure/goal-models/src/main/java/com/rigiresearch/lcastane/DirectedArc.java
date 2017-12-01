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

import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.galapagosmodel.Flag;
import com.rigiresearch.lcastane.galapagosmodel.Flaggable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Represents a graph arc.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
@Accessors(fluent = true)
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public final class DirectedArc implements Connector, Flaggable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2395559203659262373L;

    /**
     * Links according to Table 6.4.
     * @date 2017-07-04
     * @version $Id$
     * @since 0.0.1
     */
    public enum Type {
        DEPENDENCY,
        DECOMPOSITION,
        ACHIEVEMENT,
        RESPONSIBILITY
    }

    /**
     * Node to search for.
     * @date 2017-07-04
     * @version $Id$
     * @since 0.0.1
     */
    @AllArgsConstructor
    @Accessors(fluent = true)
    public enum NodeType {
        ORIGIN(arc -> arc.origin()),
        DESTINY(arc -> arc.destiny());
        
        /**
         * A function to select the node.
         */
        @Getter
        public final Function<DirectedArc, Node> select;

        /**
         * Returns the opposite node type.
         */
        public NodeType opposite() {
            if (this.equals(ORIGIN))
                return DESTINY;
            return ORIGIN;
        }
    }

    /**
     * The left side of this arc.
     */
    private final Node origin;

    /**
     * The right side of this arc.
     */
    private final Node destiny;

    /**
     * This arc's type.
     */
    private final Type type;

    /**
     * Annotations on this arc.
     */
    private final Map<Flag, Object> flags = new HashMap<>();

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.goalsmodel.Flaggable
     *  #flag(com.rigiresearch.lcastane.goalsmodel.Flag, java.lang.Object)
     */
    @Override
    public void flag(final Flag flag, final Object value) {
        this.flags.put(flag, value);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.Connector#origin()
     */
    @Override
    public List<Node> edge1() {
        return Arrays.asList(this.origin);
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.Connector#destiny()
     */
    @Override
    public List<Node> edge2() {
        return Arrays.asList(this.destiny);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
            "%s --%s--> %s",
            this.origin.toString(),
            this.type.toString(),
            this.destiny.toString()
        );
    }

}
