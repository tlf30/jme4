package com.jme.input.vr;

import com.jme.math.Vector2f;

/**
 * This interface describe the VR playground bounds.
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 *
 */
public interface VRBounds {

    /**
     * Get the size of the VR playground.
     * @return the size of the VR playground.
     */
    public Vector2f getPlaySize();
}
