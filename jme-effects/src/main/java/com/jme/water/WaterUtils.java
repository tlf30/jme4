/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme.water;

import com.jme.math.Plane;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.util.TempVars;

/**
 *
 * @author Nehon
 */
public class WaterUtils {
    
    public static void updateReflectionCam(Camera reflectionCam, Plane plane, Camera sceneCam){
        
        TempVars vars = TempVars.get();
         //Temp vects for reflection cam orientation calculation
        Vector3f sceneTarget =  vars.vect1;
        Vector3f  reflectDirection =  vars.vect2;
        Vector3f  reflectUp =  vars.vect3;
        Vector3f  reflectLeft = vars.vect4;
        Vector3f  camLoc = vars.vect5;
        camLoc = plane.reflect(sceneCam.getLocation(), camLoc);
        reflectionCam.setLocation(camLoc);
        reflectionCam.setFrustum(sceneCam.getFrustumNear(),
                sceneCam.getFrustumFar(),
                sceneCam.getFrustumLeft(),
                sceneCam.getFrustumRight(),
                sceneCam.getFrustumTop(),
                sceneCam.getFrustumBottom());
        reflectionCam.setParallelProjection(sceneCam.isParallelProjection());

        sceneTarget.set(sceneCam.getLocation()).addLocal(sceneCam.getDirection(vars.vect6));
        reflectDirection = plane.reflect(sceneTarget, reflectDirection);
        reflectDirection.subtractLocal(camLoc);

        sceneTarget.set(sceneCam.getLocation()).subtractLocal(sceneCam.getUp(vars.vect6));
        reflectUp = plane.reflect(sceneTarget, reflectUp);
        reflectUp.subtractLocal(camLoc);

        sceneTarget.set(sceneCam.getLocation()).addLocal(sceneCam.getLeft(vars.vect6));
        reflectLeft = plane.reflect(sceneTarget, reflectLeft);
        reflectLeft.subtractLocal(camLoc);

        reflectionCam.setAxes(reflectLeft, reflectUp, reflectDirection);

        vars.release();
    }
}
