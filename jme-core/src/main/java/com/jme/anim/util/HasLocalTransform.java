package com.jme.anim.util;

import com.jme.export.Savable;
import com.jme.math.Transform;

public interface HasLocalTransform extends Savable {
    public void setLocalTransform(Transform transform);

    public Transform getLocalTransform();
}
