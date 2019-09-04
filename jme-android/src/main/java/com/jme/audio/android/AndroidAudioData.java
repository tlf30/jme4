package com.jme.audio.android;

import com.jme.asset.AssetKey;
import com.jme.audio.AudioData;
import com.jme.audio.AudioRenderer;
import com.jme.util.NativeObject;

public class AndroidAudioData extends AudioData {

    protected AssetKey<?> assetKey;
    protected float currentVolume = 0f;

    public AndroidAudioData(){
        super();
    }
    
    protected AndroidAudioData(int id){
        super(id);
    }
    
    public AssetKey<?> getAssetKey() {
        return assetKey;
    }

    public void setAssetKey(AssetKey<?> assetKey) {
        this.assetKey = assetKey;
    }

    @Override
    public DataType getDataType() {
        return DataType.Buffer;
    }

    @Override
    public float getDuration() {
        return 0; // TODO: ???
    }

    @Override
    public void resetObject() {
        this.id = -1;
        setUpdateNeeded();  
    }

    @Override
    public void deleteObject(Object rendererObject) {
        ((AudioRenderer)rendererObject).deleteAudioData(this);
    }

    public float getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(float currentVolume) {
        this.currentVolume = currentVolume;
    }

    @Override
    public NativeObject createDestructableClone() {
        return new AndroidAudioData(id);
    }

    @Override
    public long getUniqueId() {
        return ((long)OBJTYPE_AUDIOBUFFER << 32) | ((long)id);
    }
}
