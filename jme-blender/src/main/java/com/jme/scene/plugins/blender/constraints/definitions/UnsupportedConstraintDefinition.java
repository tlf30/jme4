package com.jme.scene.plugins.blender.constraints.definitions;

import com.jme.math.Transform;
import com.jme.scene.plugins.blender.constraints.ConstraintHelper.Space;

/**
 * This class represents a constraint that is defined by blender but not
 * supported by either importer ot jme. It only wirtes down a warning when
 * baking is called.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
/* package */class UnsupportedConstraintDefinition extends ConstraintDefinition {
    private String typeName;

    public UnsupportedConstraintDefinition(String typeName) {
        super(null, null, null);
        this.typeName = typeName;
        trackToBeChanged = false;
    }

    @Override
    public void bake(Space ownerSpace, Space targetSpace, Transform targetTransform, float influence) {
    }

    @Override
    public boolean isImplemented() {
        return false;
    }

    @Override
    public String getConstraintTypeName() {
        return typeName;
    }

    @Override
    public boolean isTargetRequired() {
        return false;
    }
}
