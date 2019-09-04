package com.jme.light;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.export.Savable;
import com.jme.math.Matrix4f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.util.TempVars;

public interface ProbeArea extends Savable, Cloneable{

    public void setCenter(Vector3f center);

    public float getRadius();

    public void setRadius(float radius);

    public Matrix4f getUniformMatrix();

    /**
     * @see Light#intersectsBox(BoundingBox, TempVars)
     */
    public boolean intersectsBox(BoundingBox box, TempVars vars);

    /**
     * @see Light#intersectsSphere(BoundingSphere, TempVars)
     */
    public boolean intersectsSphere(BoundingSphere sphere, TempVars vars);

    /**
     * @see Light#intersectsFrustum(Camera, TempVars)
     */
    public abstract boolean intersectsFrustum(Camera camera, TempVars vars);
}
