package jmetest.material;

import com.jme.app.SimpleApplication;
import com.jme.material.Material;
import com.jme.material.Technique;
import com.jme.material.TechniqueDef;
import com.jme.math.ColorRGBA;
import com.jme.scene.Geometry;
import com.jme.scene.shape.Box;
import com.jme.shader.Shader;
import com.jme.texture.Texture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestShaderNodes extends SimpleApplication {

    public static void main(String[] args) {
        TestShaderNodes app = new TestShaderNodes();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(20);
        Logger.getLogger("com.jme3").setLevel(Level.WARNING);
        Box boxshape1 = new Box(1f, 1f, 1f);
        Geometry cube_tex = new Geometry("A Textured Box", boxshape1);
        Texture tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/UnshadedNodes.j3md");
        mat.selectTechnique(TechniqueDef.DEFAULT_TECHNIQUE_NAME, renderManager);
        Technique t = mat.getActiveTechnique();

        for (Shader.ShaderSource shaderSource : t.getDef().getShader(assetManager, renderer.getCaps(), t.getDynamicDefines()).getSources()) {
            System.out.println(shaderSource.getSource());
        }
        
        mat.setColor("Color", ColorRGBA.Yellow);
        mat.setTexture("ColorMap", tex);
        cube_tex.setMaterial(mat);
        rootNode.attachChild(cube_tex);
    }
}
