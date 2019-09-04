/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme.terrain.geomipmap;

import com.jme.math.Vector3f;

/**
 * Notifies the user of grid change events, such as moving to new grid cells.
 * @author Anthyon
 */
public interface TerrainGridListener {

    /**
     * Called whenever the camera has moved full grid cells. This triggers new tiles to load.
     * @param newCenter 
     */
    public void gridMoved(Vector3f newCenter);

    /**
     * Called when a TerrainQuad is attached to the scene and is visible (attached to the root TerrainGrid)
     * @param cell the cell that is moved into
     * @param quad the quad that was just attached
     */
    public void tileAttached( Vector3f cell, TerrainQuad quad );

    /**
     * Called when a TerrainQuad is detached from its TerrainGrid parent: it is no longer on the scene graph.
     * @param cell the cell that is moved into
     * @param quad the quad that was just detached
     */
    public void tileDetached( Vector3f cell, TerrainQuad quad );
}
