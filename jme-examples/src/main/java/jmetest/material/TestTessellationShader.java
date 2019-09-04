package jmetest.material;

import com.jme.app.SimpleApplication;
import com.jme.input.KeyInput;
import com.jme.input.controls.AnalogListener;
import com.jme.input.controls.KeyTrigger;
import com.jme.material.Material;
import com.jme.scene.Geometry;
import com.jme.scene.Mesh;
import com.jme.scene.VertexBuffer;
import com.jme.scene.shape.Quad;
import com.jme.util.BufferUtils;

import java.util.concurrent.Callable;

/**
 * Created by michael on 28.02.15.
 */
public class TestTessellationShader extends SimpleApplication {
    Material tessellationMaterial;
    int tessFactor=5;
    @Override
    public void simpleInitApp() {
        tessellationMaterial = new Material(getAssetManager(), "Materials/Tess/SimpleTess.j3md");
        tessellationMaterial.setInt("TessellationFactor", tessFactor);
        tessellationMaterial.getAdditionalRenderState().setWireframe(true);
        Quad quad = new Quad(10, 10);
        quad.clearBuffer(VertexBuffer.Type.Index);
        quad.setBuffer(VertexBuffer.Type.Index, 4, BufferUtils.createIntBuffer(0, 1, 2, 3));
        quad.setMode(Mesh.Mode.Patch);
        quad.setPatchVertexCount(4);
        Geometry geometry = new Geometry("tessTest", quad);
        geometry.setMaterial(tessellationMaterial);
        rootNode.attachChild(geometry);

        getInputManager().addMapping("TessUp", new KeyTrigger(KeyInput.KEY_O));
        getInputManager().addMapping("TessDo", new KeyTrigger(KeyInput.KEY_L));
        getInputManager().addListener(new AnalogListener() {
            @Override
            public void onAnalog(String name, float value, float tpf) {
                if(name.equals("TessUp")){
                    tessFactor++;
                    enqueue(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            tessellationMaterial.setInt("TessellationFactor",tessFactor);
                            return true;
                        }
                    });
                }
                if(name.equals("TessDo")){
                    tessFactor--;
                    enqueue(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            tessellationMaterial.setInt("TessellationFactor",tessFactor);
                            return true;
                        }
                    });
                }
            }
        },"TessUp","TessDo");
    }

    public static void main(String[] args) {
        new TestTessellationShader().start();
    }
}
