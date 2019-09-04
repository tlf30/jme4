package com.jme.system.osvr.osvrmatrixconventions;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class OSVR_Pose3 extends Structure {
	/** C type : OSVR_Vec3 */
	public OSVR_Vec3 translation;
	/** C type : OSVR_Quaternion */
	public OSVR_Quaternion rotation;
	public OSVR_Pose3() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("translation", "rotation");
	}
	/**
	 * @param translation C type : OSVR_Vec3<br>
	 * @param rotation C type : OSVR_Quaternion
	 */
	public OSVR_Pose3(OSVR_Vec3 translation, OSVR_Quaternion rotation) {
		super();
		this.translation = translation;
		this.rotation = rotation;
	}
	public OSVR_Pose3(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends OSVR_Pose3 implements Structure.ByReference {
		
	};
	public static class ByValue extends OSVR_Pose3 implements Structure.ByValue {
		
	};
}
