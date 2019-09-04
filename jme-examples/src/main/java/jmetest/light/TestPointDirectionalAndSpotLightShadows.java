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
import com.jme.light.DirectionalLight;
import com.jme.light.PointLight;
import com.jme.light.SpotLight;
import com.jme.math.ColorRGBA;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.post.FilterPostProcessor;
import com.jme.renderer.queue.RenderQueue;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Sphere;
import com.jme.shadow.DirectionalLightShadowFilter;
import com.jme.shadow.DirectionalLightShadowRenderer;
import com.jme.shadow.EdgeFilteringMode;
import com.jme.shadow.PointLightShadowFilter;
import com.jme.shadow.PointLightShadowRenderer;
import com.jme.shadow.SpotLightShadowFilter;
import com.jme.shadow.SpotLightShadowRenderer;

public class TestPointDirectionalAndSpotLightShadows extends SimpleApplication {
    public static final int SHADOWMAP_SIZE = 512;

    public static void main(String[] args) {
        TestPointDirectionalAndSpotLightShadows app = new TestPointDirectionalAndSpotLightShadows();
        app.start();
    }
    Node lightNode;
    PointLightShadowRenderer plsr;
    PointLightShadowFilter plsf;
    DirectionalLightShadowRenderer dlsr;
    DirectionalLightShadowFilter dlsf;
    SpotLightShadowRenderer slsr;
    SpotLightShadowFilter slsf;
    SpotLight spotLight;
    
    boolean useFilter = false;
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(0.040581334f, 1.7745866f, 6.155161f));
        cam.setRotation(new Quaternion(4.3868728E-5f, 0.9999293f, -0.011230096f, 0.0039059948f));


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

        ((PointLight) scene.getLocalLightList().get(0)).setColor(ColorRGBA.Red);
        
        plsr = new PointLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        plsr.setLight((PointLight) scene.getLocalLightList().get(0));
        plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);



        plsf = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        plsf.setLight((PointLight) scene.getLocalLightList().get(0));     
        plsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        plsf.setEnabled(useFilter);

        //DIRECTIONAL LIGHT
        DirectionalLight directionalLight = new DirectionalLight();
        rootNode.addLight(directionalLight);
        directionalLight.setColor(ColorRGBA.Blue);
        directionalLight.setDirection(new Vector3f(-1f, -.2f, 0f));
        dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE*2, 4);
        dlsr.setLight(directionalLight);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        
        dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE*2, 4);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        dlsf.setLight(directionalLight);        
        dlsf.setEnabled(useFilter);
        
        //SPOT LIGHT
        spotLight = new SpotLight();
        spotLight.setDirection(new Vector3f(1f,-1f,0f));
        spotLight.setPosition(new Vector3f(-1f,3f,0f));
        spotLight.setSpotOuterAngle(0.5f);
        spotLight.setColor(ColorRGBA.Green);
        Sphere sphere = new Sphere(8, 8, .1f);
        Geometry sphereGeometry = new Geometry("Sphere", sphere);
        sphereGeometry.setLocalTranslation(-1f, 3f, 0f);
        sphereGeometry.setMaterial(assetManager.loadMaterial("Common/Materials/WhiteColor.j3m"));
        rootNode.attachChild(sphereGeometry);
        rootNode.addLight(spotLight);
        
        slsr = new SpotLightShadowRenderer(assetManager, SHADOWMAP_SIZE);
        slsr.setLight(spotLight);
        slsr.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        
        slsf = new SpotLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        slsf.setLight(spotLight);
        slsf.setEdgeFilteringMode(EdgeFilteringMode.PCF4);
        slsf.setEnabled(useFilter);
        
        
        
        if (!useFilter)viewPort.addProcessor(slsr);
        if (!useFilter)viewPort.addProcessor(plsr);
        if (!useFilter)viewPort.addProcessor(dlsr);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(plsf);
        fpp.addFilter(dlsf);
        fpp.addFilter(slsf);
        viewPort.addProcessor(fpp);
              
        ShadowTestUIManager uiMan = new ShadowTestUIManager(assetManager, plsr, plsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManPls = new ShadowTestUIManager(assetManager, plsr, plsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManDls = new ShadowTestUIManager(assetManager, dlsr, dlsf, guiNode, inputManager, viewPort);
        ShadowTestUIManager uiManSls = new ShadowTestUIManager(assetManager, slsr, slsf, guiNode, inputManager, viewPort);
  
    }

    float timeElapsed = 0.0f;
    @Override
    public void simpleUpdate(float tpf) {
        timeElapsed += tpf;
       lightNode.setLocalTranslation(FastMath.cos(timeElapsed), lightNode.getLocalTranslation().y, FastMath.sin(timeElapsed));
       spotLight.setDirection(new Vector3f(FastMath.cos(-timeElapsed*.7f), -1.0f, FastMath.sin(-timeElapsed*.7f)));
    }
}