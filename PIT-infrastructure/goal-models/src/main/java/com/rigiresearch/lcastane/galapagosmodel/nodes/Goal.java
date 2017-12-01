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
package com.rigiresearch.lcastane.galapagosmodel.nodes;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
@AllArgsConstructor
@Accessors(fluent = true)
@Getter
public final class Goal extends AbstractNode {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 972679585585375062L;

    /**
     * This goal's name.
     */
    private final String name;

    /**
     * This goal's human-readable description.
     */
    private final String description;

    /**
     * This goal's subgoals.
     */
    private final List<Goal> subgoals;

    /**
     * This goal's tasks.
     */
    private final List<Task> tasks;

    /**
     * This goal's measurable outcomes.
     */
    private final List<Resource> outcomes;

    /**
     * Secondary constructor.
     * @param name This goal's name
     * @param description This goal's description
     */
    public Goal(final String name, final String description) {
        this(
            name,
            description,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
            "%s",
            this.name
        );
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.goalsmodel.nodes.AbstractNode#id()
     */
    @Override
    public String id() {
        return this.name;
    }

}
