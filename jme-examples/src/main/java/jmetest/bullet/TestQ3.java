/*
 * Copyright (c) 2009-2018 jMonkeyEngine
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

package jmetest.bullet;

import com.jme.app.SimpleApplication;
import com.jme.asset.plugins.HttpZipLocator;
import com.jme.asset.plugins.ZipLocator;
import com.jme.bullet.BulletAppState;
import com.jme.bullet.PhysicsSpace;
import com.jme.bullet.collision.shapes.SphereCollisionShape;
import com.jme.bullet.control.RigidBodyControl;
import com.jme.bullet.objects.PhysicsCharacter;
import com.jme.input.KeyInput;
import com.jme.input.controls.ActionListener;
import com.jme.input.controls.KeyTrigger;
import com.jme.light.AmbientLight;
import com.jme.light.DirectionalLight;
import com.jme.material.MaterialList;
import com.jme.math.ColorRGBA;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.plugins.ogre.OgreMeshKey;
import java.io.File;

public class TestQ3 extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private Node gameLevel;
    private PhysicsCharacter player;
    private Vector3f walkDirection = new Vector3f();
    private static boolean useHttp = false;
    private boolean left=false,right=false,up=false,down=false;

    public static void main(String[] args) {
        File file = new File("quake3level.zip");
        if (!file.exists()) {
            useHttp = true;
        }
        TestQ3 app = new TestQ3();
        app.start();
    }

    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        flyCam.setMoveSpeed(100);
        setupKeys();

        this.cam.setFrustumFar(2000);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White.clone().multLocal(2));
        dl.setDirection(new Vector3f(-1, -1, -1).normalize());
        rootNode.addLight(dl);

        AmbientLight am = new AmbientLight();
        am.setColor(ColorRGBA.White.mult(2));
        rootNode.addLight(am);

        // load the level from zip or http zip
        if (useHttp) {
            assetManager.registerLocator(
                    "https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/jmonkeyengine/quake3level.zip",
                    HttpZipLocator.class);
        } else {
            assetManager.registerLocator("quake3level.zip", ZipLocator.class);
        }

        // create the geometry and attach it
        MaterialList matList = (MaterialList) assetManager.loadAsset("Scene.material");
        OgreMeshKey key = new OgreMeshKey("main.meshxml", matList);
        gameLevel = (Node) assetManager.loadAsset(key);
        gameLevel.setLocalScale(0.1f);

        // add a physics control, it will generate a MeshCollisionShape based on the gameLevel
        gameLevel.addControl(new RigidBodyControl(0));

        player = new PhysicsCharacter(new SphereCollisionShape(5), .01f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);

        player.setPhysicsLocation(new Vector3f(60, 10, -60));

        rootNode.attachChild(gameLevel);

        getPhysicsSpace().addAll(gameLevel);
        getPhysicsSpace().add(player);
    }

    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }

    @Override
    public void simpleUpdate(float tpf) {
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0,0,0);
        if(left)
            walkDirection.addLocal(camLeft);
        if(right)
            walkDirection.addLocal(camLeft.negate());
        if(up)
            walkDirection.addLocal(camDir);
        if(down)
            walkDirection.addLocal(camDir.negate());
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this,"Lefts");
        inputManager.addListener(this,"Rights");
        inputManager.addListener(this,"Ups");
        inputManager.addListener(this,"Downs");
        inputManager.addListener(this,"Space");
    }

    public void onAction(String binding, boolean value, float tpf) {

        if (binding.equals("Lefts")) {
            if(value)
                left=true;
            else
                left=false;
        } else if (binding.equals("Rights")) {
            if(value)
                right=true;
            else
                right=false;
        } else if (binding.equals("Ups")) {
            if(value)
                up=true;
            else
                up=false;
        } else if (binding.equals("Downs")) {
            if(value)
                down=true;
            else
                down=false;
        } else if (binding.equals("Space")) {
            player.jump();
        }
    }
}
