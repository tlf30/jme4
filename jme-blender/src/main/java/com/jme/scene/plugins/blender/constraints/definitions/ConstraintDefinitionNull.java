package com.jme.scene.plugins.blender.constraints.definitions;

import com.jme.math.Transform;
import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.constraints.ConstraintHelper.Space;
import com.jme.scene.plugins.blender.file.Structure;

/**
 * This class represents 'Null' constraint type in blender.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
/* package */class ConstraintDefinitionNull extends ConstraintDefinition {

    public ConstraintDefinitionNull(Structure constraintData, Long ownerOMA, BlenderContext blenderContext) {
        super(constraintData, ownerOMA, blenderContext);
        trackToBeChanged = false;
    }

    @Override
    public void bake(Space ownerSpace, Space targetSpace, Transform targetTransform, float influence) {
        // null constraint does nothing so no need to implement this one
    }

    @Override
    public String getConstraintTypeName() {
        return "Null";
    }

    @Override
    public boolean isTargetRequired() {
        return false;
    }
}
