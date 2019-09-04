package com.jme.scene.plugins.gltf;

import com.jme.material.MatParam;

/**
 * Created by Nehon on 20/08/2017.
 */
public class PBRSpecGlossMaterialAdapter extends PBRMaterialAdapter {

    public PBRSpecGlossMaterialAdapter() {
        super();
        addParamMapping("diffuseFactor", "BaseColor");
        addParamMapping("diffuseTexture", "BaseColorMap");
        addParamMapping("specularFactor", "Specular");
        addParamMapping("glossinessFactor", "Glossiness");
        addParamMapping("specularGlossinessTexture", "SpecularGlossinessMap");
    }

    @Override
    protected MatParam adaptMatParam(MatParam param) {
        getMaterial().setBoolean("UseSpecGloss", true);
        return super.adaptMatParam(param);
    }
}
