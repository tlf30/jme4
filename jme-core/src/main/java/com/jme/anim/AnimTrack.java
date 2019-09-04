package com.jme.anim;

import com.jme.export.Savable;
import com.jme.util.clone.JmeCloneable;

public interface AnimTrack<T> extends Savable, JmeCloneable {

    public void getDataAtTime(double time, T store);
    public double getLength();


}
