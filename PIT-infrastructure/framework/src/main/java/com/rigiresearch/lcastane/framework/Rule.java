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
package com.rigiresearch.lcastane.framework;

import java.io.Serializable;

/**
 * Represents a validation rule for a specific {@link Artefact}.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public interface Rule<T extends Artefact> extends Serializable {

    /**
     * Determines when this rule should be applied.
     * @date 2017-06-23
     * @version $Id$
     * @since 0.0.1
     */
    public enum Type {
        PRE,
        POST
    }

    /**
     * Determines when this rule should be applied.
     * @return This rule's type
     */
    Type type();

    /**
     * Describes this rule's purpose.
     * @return a short name
     */
    String name();

    /**
     * Describes the cause of failure. This is expected to be invoked in case
     * this rule fails.
     * @return A message describing the cause of failure
     */
    String errorMessage();

    /**
     * Applies this validation rule to the given {@link Artefact}.
     * @param artefact The {@link Artefact} using this rule
     * @return whether this rule passes or fails
     */
    boolean apply(T artefact, Node... operands);

}
