package jmetest.scene.instancing;

import com.jme.app.SimpleApplication;
import com.jme.light.PointLight;
import com.jme.material.Material;
import com.jme.math.ColorRGBA;
import com.jme.math.Vector3f;
import com.jme.scene.Geometry;
import com.jme.scene.instancing.InstancedNode;
import com.jme.scene.shape.Box;

public class TestInstanceNodeWithLight extends SimpleApplication {
    // Try to test with different offset
    private static float offset = 12;

    public static void main(String[] args) {
        TestInstanceNodeWithLight app = new TestInstanceNodeWithLight();
        app.start();
    }

    Geometry box;
    PointLight pointLight;

    @Override
    public void simpleInitApp() {
        InstancedNode instancedNode = new InstancedNode("testInstancedNode");
        rootNode.attachChild(instancedNode);

        box = new Geometry("Box", new Box(0.5f, 0.5f, 0.5f));
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseInstancing", true);
        material.setColor("Diffuse", ColorRGBA.Red);
        material.setBoolean("UseMaterialColors", true);
        box.setMaterial(material);

        instancedNode.attachChild(box);
        instancedNode.instance();

        pointLight = new PointLight();
        pointLight.setColor(ColorRGBA.White);
        pointLight.setRadius(10f);
        rootNode.addLight(pointLight);

        box.setLocalTranslation(new Vector3f(offset, 0, 0));
        pointLight.setPosition(new Vector3f(offset - 3f, 0, 0));

        cam.setLocation(new Vector3f(offset - 5f, 0, 0));
        cam.lookAtDirection(Vector3f.UNIT_X, Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        offset += tpf;

        System.err.println(offset);
        box.setLocalTranslation(new Vector3f(offset, 0, 0));
        pointLight.setPosition(new Vector3f(offset - 3f, 0, 0));

        cam.setLocation(new Vector3f(offset - 5f, 0, 0));
        cam.lookAtDirection(Vector3f.UNIT_X, Vector3f.UNIT_Y);
    }
}