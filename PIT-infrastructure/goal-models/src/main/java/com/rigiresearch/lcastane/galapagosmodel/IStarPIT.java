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
package com.rigiresearch.lcastane.galapagosmodel;

import com.rigiresearch.lcastane.framework.Notation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Represents the iStarML notation.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode
@Getter
public final class IStarPIT implements Notation {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -4201248322390578752L;

    /**
     * This instance contents.
     */
    private String contents;

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Notation#name()
     */
    @Override
    public String name() {
        return "iStarML";
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Notation#contents()
     */
    @Override
    public String contents() {
        return this.contents;
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Notation
     *  #update(java.lang.String)
     */
    @Override
    public void update(String contents) {
        this.contents = contents;
    }

}
