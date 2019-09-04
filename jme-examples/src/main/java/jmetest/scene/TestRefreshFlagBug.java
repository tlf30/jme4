package jmetest.scene;

import com.jme.app.SimpleApplication;
import com.jme.material.Material;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class TestRefreshFlagBug extends SimpleApplication {

    private float time = 0;
    private boolean attached = false;
    private Node inBetweenNode;
    
    public static void main(String[] args) {
        TestRefreshFlagBug app = new TestRefreshFlagBug();
        app.start();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        time += tpf;
        if (time > 5 && !attached) {
            attached = true;
            
            Box b = new Box(1, 1, 1);
            Geometry geom = new Geometry("Box", b);
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            geom.setMaterial(mat);
            
            inBetweenNode.attachChild(geom);
            
            // the refresh flags become corrupted here ...
            inBetweenNode.getWorldBound();
        }
    }
    
    @Override
    public void simpleInitApp() {
        inBetweenNode = new Node("In Between Node");
        rootNode.attachChild(inBetweenNode);
            
        flyCam.setDragToRotate(true);
    }
}
