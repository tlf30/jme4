package jmetest.texture;


import com.jme.app.SimpleApplication;
import com.jme.material.Material;
import com.jme.math.Vector3f;
import com.jme.renderer.Caps;
import com.jme.scene.Geometry;
import com.jme.scene.Mesh;
import com.jme.scene.VertexBuffer.Type;
import com.jme.texture.Image;
import com.jme.texture.Texture;
import com.jme.texture.TextureArray;
import com.jme.util.BufferUtils;
import java.util.ArrayList;
import java.util.List;

public class TestTextureArray extends SimpleApplication
{

   @Override
   public void simpleInitApp()
   {
       Material mat = new Material(assetManager, "jme3test/texture/UnshadedArray.j3md");
       
       for (Caps caps : renderManager.getRenderer().getCaps()) {
           System.out.println(caps.name());
       }
       if(!renderManager.getRenderer().getCaps().contains(Caps.TextureArray)){
           throw new UnsupportedOperationException("Your hardware does not support TextureArray");
       }
       
       
       Texture tex1 = assetManager.loadTexture( "Textures/Terrain/Pond/Pond.jpg");
       Texture tex2 = assetManager.loadTexture("Textures/Terrain/Rock2/rock.jpg");
       List<Image> images = new ArrayList<Image>();
       images.add(tex1.getImage());
       images.add(tex2.getImage());
       TextureArray tex3 = new TextureArray(images);
       tex3.setMinFilter(Texture.MinFilter.Trilinear);
       mat.setTexture("ColorMap", tex3);

       Mesh m = new Mesh();
       Vector3f[] vertices = new Vector3f[8];
       vertices[0] = new Vector3f(0, 0, 0);
       vertices[1] = new Vector3f(3, 0, 0);
       vertices[2] = new Vector3f(0, 3, 0);
       vertices[3] = new Vector3f(3, 3, 0);

       vertices[4] = new Vector3f(3, 0, 0);
       vertices[5] = new Vector3f(6, 0, 0);
       vertices[6] = new Vector3f(3, 3, 0);
       vertices[7] = new Vector3f(6, 3, 0);

       Vector3f[] texCoord = new Vector3f[8];
       texCoord[0] = new Vector3f(0, 0, 0);
       texCoord[1] = new Vector3f(1, 0, 0);
       texCoord[2] = new Vector3f(0, 1, 0);
       texCoord[3] = new Vector3f(1, 1, 0);

       texCoord[4] = new Vector3f(0, 0, 1);
       texCoord[5] = new Vector3f(1, 0, 1);
       texCoord[6] = new Vector3f(0, 1, 1);
       texCoord[7] = new Vector3f(1, 1, 1);

       int[] indexes = { 2, 0, 1, 1, 3, 2 , 6, 4, 5, 5, 7, 6};

       m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
       m.setBuffer(Type.TexCoord, 3, BufferUtils.createFloatBuffer(texCoord));
       m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
       m.updateBound();

       Geometry geom = new Geometry("Mesh", m);
       geom.setMaterial(mat);
       rootNode.attachChild(geom);
   }

   /**
    * @param args
    */
   public static void main(String[] args)
   {
       TestTextureArray app = new TestTextureArray();
       app.start();
   }

}