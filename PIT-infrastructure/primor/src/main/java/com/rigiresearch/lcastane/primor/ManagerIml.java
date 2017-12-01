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
package com.rigiresearch.lcastane.primor;

import com.rigiresearch.lcastane.framework.Artefact;
import com.rigiresearch.lcastane.framework.MART;
import com.rigiresearch.lcastane.framework.Notation;
import com.rigiresearch.lcastane.framework.Sentence;
import com.rigiresearch.lcastane.framework.validation.ValidationException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public final class ManagerIml implements Manager, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -2912172732160529563L;

    /**
     * The model repository.
     */
    private final Map<String, MART<?, ?>> models;

    /**
     * Default constructor.
     */
    public ManagerIml() {
        this.models = new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.primor.Manager
     *  #registerModel(java.lang.String,
     *  com.rigiresearch.lcastane.framework.MART)
     */
    @Override
    public void registerModel(final String identifier,
        final MART<?, ?> model) {
        this.models.put(identifier, model);
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.primor.Manager
     *  #executeSentence(java.lang.String,
     *  com.rigiresearch.lcastane.framework.Sentence)
     */
    @Override
    public void executeSentence(final String identifier,
        final Sentence sentence)
            throws ValidationException, ModelNotFoundException {
        if (this.models.containsKey(identifier)) {
            this.models.get(identifier)
                .artefact()
                .apply(sentence);
        } else {
            throw new ModelNotFoundException(
                String.format(
                    "Model %s not found",
                    identifier
                )
            );
        }
    }
 
    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.primor.Manager
     *  #modelArtefact(java.lang.String)
     */
    @Override
    public Artefact modelArtefact(final String identifier)
        throws ModelNotFoundException {
        if (this.models.containsKey(identifier)) {
            return this.models.get(identifier).artefact();
        }
        throw new ModelNotFoundException(
            String.format(
                "Model %s not found",
                identifier
            )
        );
    }
    
    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.primor.Manager
     *  #modelNotation(java.lang.String)
     */
    @Override
    public Notation modelNotation(final String identifier)
        throws ModelNotFoundException {
        if (this.models.containsKey(identifier)) {
            return this.models.get(identifier).notation();
        }
        throw new ModelNotFoundException(
            String.format(
                "Model %s not found",
                identifier
            )
        );
    }

    /**
     * Synchronises the given model.
     * @param model The model to synchronise
     */
    private void synchronise(final MART<?, ?> model) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Propagates changes on the given model.
     * @param model The model on which the changes are propagated
     */
    private void propagate(final MART<?, ?> model) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /* (non-Javadoc)
     * @see com.rigiresearch.lcastane.primor.Manager
     *  #modelRegistered(java.lang.String)
     */
    @Override
    public boolean modelRegistered(String identifier) {
        return this.models.containsKey(identifier);
    }

}
