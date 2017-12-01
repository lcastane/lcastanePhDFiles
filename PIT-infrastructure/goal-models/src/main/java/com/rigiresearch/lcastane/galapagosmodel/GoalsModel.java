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

import com.rigiresearch.lcastane.framework.MART;
import com.rigiresearch.lcastane.framework.ObservableNotation;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents a goals model using the {@link IStarPIT} notation, and a
 * {@link UCSAGoalGraph} representation.
 * TODO implement this class.
 * @date 2017-06-13
 * @version $Id$
 * @since 0.0.1
 */
public final class GoalsModel implements MART<IStarPIT, UCSAGoalGraph>, Observer {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2364040486996447866L;

    /**
     * This model's associated notation.
     */
    private final ObservableNotation<IStarPIT> notation;

    /**
     * This model's associated artefact.
     */
    private final UCSAGoalGraph artefact;

    /**
     * Default constructor.
     * @param notation This model's associated notation
     * @param artefact This model's associated artefact
     */
    public GoalsModel(final IStarPIT notation, final UCSAGoalGraph artefact) {
        this.notation = new ObservableNotation<>(notation);
        this.artefact = artefact;
        this.notation.addObserver(this);
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Translator#artefact()
     */
    @Override
    public UCSAGoalGraph artefact() {
        return this.artefact;
    }

    /*
     * (non-Javadoc)
     * @see com.rigiresearch.lcastane.framework.Translator#notation()
     */
    @Override
    public IStarPIT notation() {
        return this.notation.origin();
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO synchronise views
    }

}
