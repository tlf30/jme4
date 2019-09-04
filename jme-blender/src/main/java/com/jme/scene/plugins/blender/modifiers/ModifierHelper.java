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
package com.jme.scene.plugins.blender.modifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.scene.plugins.blender.AbstractBlenderHelper;
import com.jme.scene.plugins.blender.BlenderContext;
import com.jme.scene.plugins.blender.file.BlenderFileException;
import com.jme.scene.plugins.blender.file.Structure;

/**
 * A class that is used in modifiers calculations.
 * 
 * @author Marcin Roguski
 */
public class ModifierHelper extends AbstractBlenderHelper {

    private static final Logger LOGGER = Logger.getLogger(ModifierHelper.class.getName());

    /**
     * This constructor parses the given blender version and stores the result.
     * Some functionalities may differ in different blender versions.
     * 
     * @param blenderVersion
     *            the version read from the blend file
     * @param blenderContext
     *            the blender context
     */
    public ModifierHelper(String blenderVersion, BlenderContext blenderContext) {
        super(blenderVersion, blenderContext);
    }

    /**
     * This method reads the given object's modifiers.
     * 
     * @param objectStructure
     *            the object structure
     * @param blenderContext
     *            the blender context
     * @throws BlenderFileException
     *             this exception is thrown when the blender file is somehow
     *             corrupted
     */
    public Collection<Modifier> readModifiers(Structure objectStructure, BlenderContext blenderContext) throws BlenderFileException {
        Set<String> alreadyReadModifiers = new HashSet<String>();
        Collection<Modifier> result = new ArrayList<Modifier>();
        Structure modifiersListBase = (Structure) objectStructure.getFieldValue("modifiers");
        List<Structure> modifiers = modifiersListBase.evaluateListBase();
        for (Structure modifierStructure : modifiers) {
            String modifierType = modifierStructure.getType();
            if (!Modifier.canBeAppliedMultipleTimes(modifierType) && alreadyReadModifiers.contains(modifierType)) {
                LOGGER.log(Level.WARNING, "Modifier {0} can only be applied once to object: {1}", new Object[] { modifierType, objectStructure.getName() });
            } else {
                Modifier modifier = null;
                if (Modifier.ARRAY_MODIFIER_DATA.equals(modifierStructure.getType())) {
                    modifier = new ArrayModifier(modifierStructure, blenderContext);
                } else if (Modifier.MIRROR_MODIFIER_DATA.equals(modifierStructure.getType())) {
                    modifier = new MirrorModifier(modifierStructure, blenderContext);
                } else if (Modifier.ARMATURE_MODIFIER_DATA.equals(modifierStructure.getType())) {
                    modifier = new ArmatureModifier(objectStructure, modifierStructure, blenderContext);
                } else if (Modifier.PARTICLE_MODIFIER_DATA.equals(modifierStructure.getType())) {
                    modifier = new ParticlesModifier(modifierStructure, blenderContext);
                } else if(Modifier.SUBSURF_MODIFIER_DATA.equals(modifierStructure.getType())) {
                    modifier = new SubdivisionSurfaceModifier(modifierStructure, blenderContext);
                }

                if (modifier != null) {
                    if (modifier.isModifying()) {
                        result.add(modifier);
                        alreadyReadModifiers.add(modifierType);
                    } else {
                        LOGGER.log(Level.WARNING, "The modifier {0} will cause no changes in the model. It will be ignored!", modifierStructure.getName());
                    }
                } else {
                    LOGGER.log(Level.WARNING, "Unsupported modifier type: {0}", modifierStructure.getType());
                }
            }
        }
        return result;
    }
}
