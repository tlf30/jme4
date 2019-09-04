package com.jme.scene.plugins.gltf;

import com.jme.asset.AssetKey;
import com.jme.asset.cache.AssetCache;

/**
 * Created by Nehon on 09/09/2017.
 */
class BinDataKey extends AssetKey<Object> {
    public BinDataKey(String name) {
        super(name);
    }

    @Override
    public Class<? extends AssetCache> getCacheType() {
        return null;
    }
}
