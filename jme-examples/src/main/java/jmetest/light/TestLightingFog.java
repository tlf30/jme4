package jmetest.light;

import com.jme.app.SimpleApplication;
import com.jme.input.KeyInput;
import com.jme.input.controls.ActionListener;
import com.jme.input.controls.KeyTrigger;
import com.jme.light.DirectionalLight;
import com.jme.material.Material;
import com.jme.material.Materials;
import com.jme.math.ColorRGBA;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.shape.Sphere;

public class TestLightingFog extends SimpleApplication implements ActionListener {

    private Material material;
    private Vector2f linear = new Vector2f(25, 120);
    private float exp = 0.015f;
    private float expsq = 0.02f;

    public static void main(String[] args) {
        TestLightingFog testLightingFog = new TestLightingFog();
        testLightingFog.start();
    }

    @Override
    public void simpleInitApp() {

        ColorRGBA skyColor = new ColorRGBA(0.5f, 0.6f, 0.7f, 0.0f);

        flyCam.setMoveSpeed(20);
        viewPort.setBackgroundColor(skyColor.mult(0.9f));

        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal());
        rootNode.addLight(directionalLight);

        material = new Material(assetManager, Materials.LIGHTING);
        material.setBoolean("UseFog", true);
        material.setColor("FogColor", skyColor);
        material.setVector2("LinearFog", linear);

        int distance = -3;

        for (int i = 0; i < 100; i++) {
            Geometry geometry = new Geometry("Sphere", new Sphere(32, 32, 2));
            geometry.setMaterial(material);

            geometry.setLocalTranslation((FastMath.nextRandomFloat() - 0.5f) * 45, 0, i * distance);
            rootNode.attachChild(geometry);
        }

        inputManager.addMapping("Linear",  new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Exponential",  new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("ExponentialSquared",  new KeyTrigger(KeyInput.KEY_3));
        inputManager.addListener(this, "Linear", "Exponential", "ExponentialSquared");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("Linear") && !isPressed) {
            material.setVector2("LinearFog", linear);
            material.clearParam("ExpFog");
            material.clearParam("ExpSqFog");
        }
        else if (name.equals("Exponential") && !isPressed) {
            material.clearParam("LinearFog");
            material.setFloat("ExpFog", exp);
            material.clearParam("ExpSqFog");
        }
        else if (name.equals("ExponentialSquared") && !isPressed) {
            material.clearParam("LinearFog");
            material.clearParam("ExpFog");
            material.setFloat("ExpSqFog", expsq);
        }
    }
}
