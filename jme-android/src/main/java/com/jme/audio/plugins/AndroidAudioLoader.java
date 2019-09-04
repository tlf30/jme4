package com.jme.audio.plugins;

import com.jme.asset.AssetInfo;
import com.jme.asset.AssetLoader;
import com.jme.audio.android.AndroidAudioData;
import java.io.IOException;

/**
 * <code>AndroidAudioLoader</code> will create an 
 * {@link AndroidAudioData} object with the specified asset key.
 */
public class AndroidAudioLoader implements AssetLoader {

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        AndroidAudioData result = new AndroidAudioData();
        result.setAssetKey(assetInfo.getKey());
        return result;
    }
}
