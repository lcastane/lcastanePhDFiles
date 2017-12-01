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

import java.net.URL;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TODO
 * @date 2017-07-04
 * @version $Id$
 * @since 0.0.1
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public final class Resource extends AbstractNode {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -6041848968555297805L;

    /**
     * This resources's name.
     */
    private final String name;

    /**
     * This resources's type.
     */
    private final String type;

    /**
     * This resources's url.
     */
    private final URL url;

    /**
     * This resources's service.
     */
    private final String service;

    /**
     * Whether this resource requires authorisation request.
     */
    private final boolean authRequest;

    /**
     * This resources's credentials.
     */
    private Map<String, String> credentials;

    /**
     * Whether this resource is autoenabled.
     */
    private boolean autoenabled;

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
