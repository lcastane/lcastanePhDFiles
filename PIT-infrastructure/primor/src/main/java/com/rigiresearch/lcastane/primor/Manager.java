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
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * TODO
 * @date 2017-07-11
 * @version $Id$
 * @since 0.0.1
 */
public interface Manager extends Remote {

    /**
     * Registers a new model in this manager.
     * @param identifier The model's identifier
     * @param model The model to register
     */
    void registerModel(final String identifier, final MART<?, ?> model)
        throws RemoteException;

    /**
     * Executes the given sentence on the model.
     * @param identifier The model's identifier
     * @param sentence The sentence to execute
     * @throws ValidationException If the sentence is not compliant with the
     *  corresponding operation
     * @throws ModelNotFoundException If the model is not found
     * @throws RemoteException If there is a communication error
     */
    void executeSentence(final String identifier, final Sentence sentence)
        throws RemoteException, ValidationException, ModelNotFoundException;

    /**
     * Returns the model's artefact.
     * @param identifier The model's identifier
     * @return An {@link Artefact}
     * @throws ModelNotFoundException If the model is not found
     * @throws RemoteException If there is a communication error
     */
    Artefact modelArtefact(final String identifier)
        throws RemoteException, ModelNotFoundException;

    /**
     * Returns an instance of the model's notation.
     * @param identifier The model's identifier
     * @return A {@link Notation}
     * @throws ModelNotFoundException If the model is not found
     * @throws RemoteException If there is a communication error
     */
    Notation modelNotation(final String identifier)
        throws RemoteException, ModelNotFoundException;

    /**
     * Whether the given model is registered.
     * @param identifier The model's identifier
     * @throws RemoteException If there is a communication error
     */
    boolean modelRegistered(final String identifier) throws RemoteException;

}
