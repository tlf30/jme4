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
package jmetest.light;

import com.jme.app.SimpleApplication;
import com.jme.input.controls.ActionListener;
import com.jme.light.AmbientLight;
import com.jme.light.PointLight;
import com.jme.math.ColorRGBA;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.post.FilterPostProcessor;
import com.jme.renderer.queue.RenderQueue;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.shadow.EdgeFilteringMode;
import com.jme.shadow.PointLightShadowFilter;
import com.jme.shadow.PointLightShadowRenderer;

public class TestPointLightShadows extends SimpleApplication implements ActionListener{
    public static final int SHADOWMAP_SIZE = 512;

    public static void main(String[] args) {
        TestPointLightShadows app = new TestPointLightShadows();
        app.start();
    }
    Node lightNode;
    PointLightShadowRenderer plsr;
    PointLightShadowFilter plsf;
    AmbientLight al;

    @Override
    public void simpleInitApp () {
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0.040581334f, 1.7745866f, 6.155161f));
        cam.setRotation(new Quaternion(4.3868728E-5f, 0.9999293f, -0.011230096f, 0.0039059948f));

        al = new AmbientLight(ColorRGBA.White.mult(0.02f));
        rootNode.addLight(al);




        Node scene = (Node) assetManager.loadModel("Models/Test/CornellBox.j3o");
        scene.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);
        rootNode.getChild("Cube").setShadowMode(RenderQueue.ShadowMode.Receive);
        lightNode = (Node) rootNode.getChild("Lamp");
        Geometry lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        //Geometry  lightMdl = new Geometry("Light", new Box(.1f,.1f,.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        lightMdl.setShadowMode(RenderQueue.ShadowMode.Off);
        lightNode.attachChild(lightMdl);
        //lightMdl.setLocalTranslation(lightNode.getLocalTranslation());


        Geometry box = new Geometry("box", new Box(0.2f, 0.2f, 0.2f));
        //Geometry  lightMdl = new Geometry("Light", new Box(.1f,.1f,.1f));
        box.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        box.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(box);
        box.setLocalTranslation(-1f, 0.5f, -2);


        plsr = new PointLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        plsr.setLight((PointLight) scene.getLocalLightList().get(0));
        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        plsr.setShadowZExtend(15);
        plsr.setShadowZFadeLength(5);
        plsr.setShadowIntensity(0.9f);
       // plsr.setFlushQueues(false);
        //plsr.displayFrustum();
        plsr.displayDebug();
        viewPort.addProcessor(plsr);


        plsf = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        plsf.setLight((PointLight) scene.getLocalLightList().get(0));    
        plsf.setShadowZExtend(15);
        plsf.setShadowZFadeLength(5);
        plsf.setShadowIntensity(0.8f);
        plsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        plsf.setEnabled(false);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(plsf);
        viewPort.addProcessor(fpp);
        inputManager.addListener(this,"ShadowUp","ShadowDown");
        ShadowTestUIManager uiMan = new ShadowTestUIManager(assetManager, plsr, plsf, guiNode, inputManager, viewPort);

    }

    @Override
    public void simpleUpdate(float tpf) {
 //      lightNode.move(FastMath.cos(tpf) * 0.4f, 0, FastMath.sin(tpf) * 0.4f);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if ((name.equals("ShadowUp") || name.equals("ShadowDown")) && isPressed) {
            al.setColor(ColorRGBA.White.mult((1 - plsr.getShadowIntensity()) * 0.2f));
        }
    }
}