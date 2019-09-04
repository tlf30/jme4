package com.jme.scene.plugins.ogre;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jme.asset.AssetInfo;
import com.jme.asset.AssetKey;
import com.jme.scene.Spatial;

public class SceneMeshLoader extends MeshLoader{
	private Map<AssetKey,Spatial> cache=new HashMap<AssetKey,Spatial>();
	@Override
    public Object load(AssetInfo info) throws IOException {
		AssetKey key=info.getKey();
		Spatial output=cache.get(key);
		if(output==null){
			output=(Spatial)super.load(info);
			cache.put(key,output);
		}
		return output.clone(false);
	}
	public void reset(){
		cache.clear();
	}
}
