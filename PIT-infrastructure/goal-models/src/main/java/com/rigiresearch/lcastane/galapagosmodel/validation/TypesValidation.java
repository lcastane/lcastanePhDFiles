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
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Validates the of arguments passed to an operation.
 * This rule applies to any {@link Node}.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public final class TypesValidation extends AbstractRule {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2805424768530205419L;

    /**
     * Expected operand types.
     */
    private final Class<?>[] operandTypes;

    /**
     * Default constructor.
     * @param expectedOperands The expected operand types
     */
    public TypesValidation(Class<?>... operandTypes) {
        super(
            Rule.Type.PRE,
            "Operands Validation",
            ""
        );
        this.operandTypes = operandTypes;
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.ValidationRule#apply()
     */
    @Override
    public boolean apply(final Graph<DirectedArc> artefact, Node... operands) {
        this.message = String.format(
            "Given arguments (%s) differ from expected (%s)",
            Arrays.toString(operands),
            Arrays.toString(this.operandTypes)
        );
        return Arrays.equals(
            this.operandTypes,
            Stream.of(operands)
                .map(operand -> operand.getClass())
                .toArray(Class[]::new)
        );
    }

}
