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

import com.rigiresearch.lcastane.framework.Node;
import com.rigiresearch.lcastane.galapagosmodel.Flag;
import com.rigiresearch.lcastane.galapagosmodel.Flaggable;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * TODO
 * @date 2017-07-05
 * @version $Id$
 * @since 0.0.1
 */
@Accessors(fluent = true)
@Getter
public abstract class AbstractNode implements Node, Flaggable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 5598856346209929097L;

    /**
     * Annotations on this node.
     */
    protected final Map<Flag, Object> flags;

    /**
     * Default constructor.
     */
    public AbstractNode() {
        this.flags = new HashMap<>();
    }

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
     * @see com.rigiresearch.lcastane.framework.Node#id()
     */
    @Override
    public abstract String id();

}
