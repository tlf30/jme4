/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme.post;

import com.jme.post.Filter.Pass;
import com.jme.renderer.Caps;
import com.jme.renderer.RenderManager;
import com.jme.renderer.Renderer;
import com.jme.renderer.ViewPort;
import com.jme.texture.FrameBuffer;

/**
 * Pre normal caching class.
 * @author reden - phr00t - https://github.com/phr00t
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 */
public class PreNormalCaching {
    
    private static FrameBuffer cachedPreNormals;
    private static int lastNormalPassesCount, curCount;
    
    /**
     * Get pre-normals from the given rendering.
     * @param renderManager the render manager.
     * @param normalPass the normal pass.
     * @param viewPort the viewport.
     */
    public static void getPreNormals(RenderManager renderManager, Pass normalPass, ViewPort viewPort) {
        curCount++;
        // do we already have a valid cache to set the framebuffer to?
        Renderer r = renderManager.getRenderer();
        if( cachedPreNormals != null ) {
            r.copyFrameBuffer(cachedPreNormals, normalPass.getRenderFrameBuffer(), false);
        } else {
            // lets make the prenormals
            r.setFrameBuffer(normalPass.getRenderFrameBuffer());
            renderManager.getRenderer().clearBuffers(true, true, true);
            if( renderManager.getRenderer().getCaps().contains(Caps.GLSL150) ) {
                renderManager.setForcedTechnique("PreNormalPass15");
            } else {
                renderManager.setForcedTechnique("PreNormalPass");                
            }
            renderManager.renderViewPortQueues(viewPort, false);
            renderManager.setForcedTechnique(null);
            // if we should cache this, do it now
            if( lastNormalPassesCount > 1 ) {
                cachedPreNormals = normalPass.getRenderFrameBuffer();
            }
        }
        renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
    }
    
   /**
    * Reset the cache
    * @param stereo <code>true</code> if the rendering is stereo based and <code>false</code> otherwise.
    */
    public static void resetCache(boolean stereo) {
        if( stereo == false ) {
            // only use this feature if we are NOT in VR
            // we can't use the same normal information for another eye,
            // because it will be different!
            lastNormalPassesCount = curCount;
        }
        cachedPreNormals = null;
        curCount = 0;
    }
    
}
