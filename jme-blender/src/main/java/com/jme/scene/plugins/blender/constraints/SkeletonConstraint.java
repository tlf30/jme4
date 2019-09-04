package com.jme.scene.plugins.blender.constraints;

import java.util.logging.Logger;

import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.animations.Ipo;
import com.jme.scene.plugins.blender.file.BlenderFileException;
import com.jme.scene.plugins.blender.file.Structure;

/**
 * Constraint applied on the skeleton. This constraint is here only to make the
 * application not crash when loads constraints applied to armature. But
 * skeleton movement is not supported by jme so the constraint will never be
 * applied.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
/* package */class SkeletonConstraint extends Constraint {
    private static final Logger LOGGER = Logger.getLogger(SkeletonConstraint.class.getName());

    public SkeletonConstraint(Structure constraintStructure, Long ownerOMA, Ipo influenceIpo, BlenderContext blenderContext) throws BlenderFileException {
        super(constraintStructure, ownerOMA, influenceIpo, blenderContext);
    }

    @Override
    public boolean validate() {
        LOGGER.warning("Constraints for skeleton are not supported.");
        return false;
    }

    @Override
    public void apply(int frame) {
        LOGGER.warning("Applying constraints to skeleton is not supported.");
    }
    
    @Override
    public Long getTargetOMA() {
        LOGGER.warning("Constraints for skeleton are not supported.");
        return null;
    }
}
