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

package jmetest.collision;

import com.jme.app.SimpleApplication;
import com.jme.bounding.BoundingVolume;
import com.jme.collision.CollisionResults;
import com.jme.input.KeyInput;
import com.jme.input.controls.AnalogListener;
import com.jme.input.controls.KeyTrigger;
import com.jme.light.DirectionalLight;
import com.jme.material.Material;
import com.jme.math.ColorRGBA;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.Mesh;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

public class TestTriangleCollision extends SimpleApplication {

    Geometry geom1;

    Spatial golem;

    public static void main(String[] args) {
        TestTriangleCollision app = new TestTriangleCollision();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Create two boxes
        Mesh mesh1 = new Box(0.5f, 0.5f, 0.5f);
        geom1 = new Geometry("Box", mesh1);
        geom1.move(2, 2, -.5f);
        Material m1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m1.setColor("Color", ColorRGBA.Blue);
        geom1.setMaterial(m1);
        rootNode.attachChild(geom1);

        // load a character from jme3test-test-data
        golem = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        golem.scale(0.5f);
        golem.setLocalTranslation(-1.0f, -1.5f, -0.6f);

        // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        golem.addLight(sun);
        rootNode.attachChild(golem);

        // Create input
        inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("MoveDown", new KeyTrigger(KeyInput.KEY_K));

        inputManager.addListener(analogListener, new String[]{
                    "MoveRight", "MoveLeft", "MoveUp", "MoveDown"
                });
    }
    private AnalogListener analogListener = new AnalogListener() {

        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("MoveRight")) {
                geom1.move(2 * tpf, 0, 0);
            }

            if (name.equals("MoveLeft")) {
                geom1.move(-2 * tpf, 0, 0);
            }

            if (name.equals("MoveUp")) {
                geom1.move(0, 2 * tpf, 0);
            }

            if (name.equals("MoveDown")) {
                geom1.move(0, -2 * tpf, 0);
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        CollisionResults results = new CollisionResults();
        BoundingVolume bv = geom1.getWorldBound();
        golem.collideWith(bv, results);

        if (results.size() > 0) {
            geom1.getMaterial().setColor("Color", ColorRGBA.Red);
        }else{
            geom1.getMaterial().setColor("Color", ColorRGBA.Blue);
        }
    }
}
