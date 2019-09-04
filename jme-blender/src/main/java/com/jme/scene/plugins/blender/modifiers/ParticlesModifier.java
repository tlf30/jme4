package com.jme.scene.plugins.blender.modifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.effect.ParticleEmitter;
import com.jme.effect.shapes.EmitterMeshVertexShape;
import com.jme.effect.shapes.EmitterShape;
import com.jme.material.Material;
import com.jme.scene.Geometry;
import com.jme.scene.Mesh;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.file.BlenderFileException;
import com.jme.scene.plugins.blender.file.Pointer;
import com.jme.scene.plugins.blender.file.Structure;
import com.jme.scene.plugins.blender.materials.MaterialHelper;
import com.jme.scene.plugins.blender.meshes.TemporalMesh;
import com.jme.scene.plugins.blender.particles.ParticlesHelper;

/**
 * This modifier allows to add particles to the object.
 * 
 * @author Marcin Roguski (Kaelthas)
 */
/* package */class ParticlesModifier extends Modifier {
    private static final Logger LOGGER = Logger.getLogger(MirrorModifier.class.getName());

    /** Loaded particles emitter. */
    private ParticleEmitter     particleEmitter;

    /**
     * This constructor reads the particles system structure and stores it in
     * order to apply it later to the node.
     * 
     * @param modifierStructure
     *            the structure of the modifier
     * @param blenderContext
     *            the blender context
     * @throws BlenderFileException
     *             an exception is throw wneh there are problems with the
     *             blender file
     */
    public ParticlesModifier(Structure modifierStructure, BlenderContext blenderContext) throws BlenderFileException {
        if (this.validate(modifierStructure, blenderContext)) {
            Pointer pParticleSystem = (Pointer) modifierStructure.getFieldValue("psys");
            if (pParticleSystem.isNotNull()) {
                ParticlesHelper particlesHelper = blenderContext.getHelper(ParticlesHelper.class);
                Structure particleSystem = pParticleSystem.fetchData().get(0);
                particleEmitter = particlesHelper.toParticleEmitter(particleSystem);
            }
        }
    }
    
    @Override
    public void postMeshCreationApply(Node node, BlenderContext blenderContext) {
        LOGGER.log(Level.FINE, "Applying particles modifier to: {0}", node);
        
        MaterialHelper materialHelper = blenderContext.getHelper(MaterialHelper.class);
        ParticleEmitter emitter = particleEmitter.clone();

        // veryfying the alpha function for particles' texture
        Integer alphaFunction = MaterialHelper.ALPHA_MASK_HYPERBOLE;
        char nameSuffix = emitter.getName().charAt(emitter.getName().length() - 1);
        if (nameSuffix == 'B' || nameSuffix == 'N') {
            alphaFunction = MaterialHelper.ALPHA_MASK_NONE;
        }
        // removing the type suffix from the name
        emitter.setName(emitter.getName().substring(0, emitter.getName().length() - 1));

        // applying emitter shape
        EmitterShape emitterShape = emitter.getShape();
        List<Mesh> meshes = new ArrayList<Mesh>();
        for (Spatial spatial : node.getChildren()) {
            if (spatial instanceof Geometry) {
                Mesh mesh = ((Geometry) spatial).getMesh();
                if (mesh != null) {
                    meshes.add(mesh);
                    Material material = materialHelper.getParticlesMaterial(((Geometry) spatial).getMaterial(), alphaFunction, blenderContext);
                    emitter.setMaterial(material);// TODO: divide into several pieces
                }
            }
        }
        if (meshes.size() > 0 && emitterShape instanceof EmitterMeshVertexShape) {
            ((EmitterMeshVertexShape) emitterShape).setMeshes(meshes);
        }

        node.attachChild(emitter);
    }

    @Override
    public void apply(Node node, BlenderContext blenderContext) {
        if (invalid) {
            LOGGER.log(Level.WARNING, "Particles modifier is invalid! Cannot be applied to: {0}", node.getName());
        } else {
            TemporalMesh temporalMesh = this.getTemporalMesh(node);
            if(temporalMesh != null) {
                temporalMesh.applyAfterMeshCreate(this);
            } else {
                LOGGER.log(Level.WARNING, "Cannot find temporal mesh for node: {0}. The modifier will NOT be applied!", node);
            }
        }
    }
}
