package com.jme.scene.plugins.blender.constraints.definitions;

import com.jme.animation.Bone;
import com.jme.animation.Skeleton;
import com.jme.math.Matrix4f;
import com.jme.math.Transform;
import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.BlenderContext.LoadedDataType;
import com.jme.scene.plugins.blender.animations.BoneContext;
import com.jme.scene.plugins.blender.constraints.ConstraintHelper;
import com.jme.scene.plugins.blender.constraints.ConstraintHelper.Space;
import com.jme.scene.plugins.blender.file.Pointer;
import com.jme.scene.plugins.blender.file.Structure;
import com.jme.scene.plugins.blender.objects.ObjectHelper;
import com.jme.util.TempVars;

/**
 * This class represents 'Trans like' constraint type in blender.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
public class ConstraintDefinitionTransLike extends ConstraintDefinition {
    private Long   targetOMA;
    private String subtargetName;

    public ConstraintDefinitionTransLike(Structure constraintData, Long ownerOMA, BlenderContext blenderContext) {
        super(constraintData, ownerOMA, blenderContext);
        Pointer pTarget = (Pointer) constraintData.getFieldValue("tar");
        targetOMA = pTarget.getOldMemoryAddress();
        Object subtarget = constraintData.getFieldValue("subtarget");
        if (subtarget != null) {
            subtargetName = subtarget.toString();
        }
    }

    @Override
    public void bake(Space ownerSpace, Space targetSpace, Transform targetTransform, float influence) {
        if (influence == 0 || targetTransform == null) {
            return;// no need to do anything
        }
        Object target = this.getTarget();// Bone or Node
        Object owner = this.getOwner();// Bone or Node
        if (!target.getClass().equals(owner.getClass())) {
            ConstraintHelper constraintHelper = blenderContext.getHelper(ConstraintHelper.class);

            TempVars tempVars = TempVars.get();
            Matrix4f m = constraintHelper.toMatrix(targetTransform, tempVars.tempMat4);
            tempVars.tempMat42.set(BoneContext.BONE_ARMATURE_TRANSFORMATION_MATRIX);
            if (target instanceof Bone) {
                tempVars.tempMat42.invertLocal();
            }
            m = m.multLocal(tempVars.tempMat42);
            tempVars.release();

            targetTransform = new Transform(m.toTranslationVector(), m.toRotationQuat(), m.toScaleVector());
        }
        this.applyOwnerTransform(targetTransform, ownerSpace);
    }

    /**
     * @return the target feature; it is either Node or Bone (vertex group subtarger is not yet supported)
     */
    private Object getTarget() {
        Object target = blenderContext.getLoadedFeature(targetOMA, LoadedDataType.FEATURE);
        if (subtargetName != null && blenderContext.getMarkerValue(ObjectHelper.ARMATURE_NODE_MARKER, target) != null) {
            Skeleton skeleton = blenderContext.getSkeleton(targetOMA);
            target = skeleton.getBone(subtargetName);
        }
        return target;
    }

    @Override
    public String getConstraintTypeName() {
        return "Copy transforms";
    }

    @Override
    public boolean isTargetRequired() {
        return true;
    }
}
