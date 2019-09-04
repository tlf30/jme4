package jmetest.light;

import com.jme.app.SimpleApplication;
import com.jme.asset.TextureKey;
import com.jme.input.ChaseCamera;
import com.jme.light.DirectionalLight;
import com.jme.material.Material;
import com.jme.math.Vector3f;
import com.jme.post.FilterPostProcessor;
import com.jme.post.filters.BloomFilter;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.texture.Texture;
import com.jme.util.SkyFactory;

/**
 * test
 * @author nehon
 */
public class TestEnvironmentMapping extends SimpleApplication {

    public static void main(String[] args) {
        TestEnvironmentMapping app = new TestEnvironmentMapping();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        final Node buggy = (Node) assetManager.loadModel("Models/Buggy/Buggy.j3o");

        TextureKey key = new TextureKey("Textures/Sky/Bright/BrightSky.dds", true);
        key.setGenerateMips(true);
        key.setTextureTypeHint(Texture.Type.CubeMap);
        final Texture tex = assetManager.loadTexture(key);

        for (Spatial geom : buggy.getChildren()) {
            if (geom instanceof Geometry) {
                Material m = ((Geometry) geom).getMaterial();
                m.setTexture("EnvMap", tex);
                m.setVector3("FresnelParams", new Vector3f(0.05f, 0.18f, 0.11f));
            }
        }

        flyCam.setEnabled(false);

        ChaseCamera chaseCam = new ChaseCamera(cam, inputManager);
        chaseCam.setLookAtOffset(new Vector3f(0,0.5f,-1.0f));
        buggy.addControl(chaseCam);
        rootNode.attachChild(buggy);
        rootNode.attachChild(SkyFactory.createSky(assetManager, tex,
                SkyFactory.EnvMapType.CubeMap));

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        BloomFilter bf = new BloomFilter(BloomFilter.GlowMode.Objects);
        bf.setBloomIntensity(2.3f);
        bf.setExposurePower(0.6f);
        
        fpp.addFilter(bf);
        
        DirectionalLight l = new DirectionalLight();
        l.setDirection(new Vector3f(0, -1, -1));
        rootNode.addLight(l);
        
        viewPort.addProcessor(fpp);
    }
}
