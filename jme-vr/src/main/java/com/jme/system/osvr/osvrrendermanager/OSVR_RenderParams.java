package com.jme.system.osvr.osvrrendermanager;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
/**
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class OSVR_RenderParams extends Structure {
	/**
	 * Room space to insert<br>
	 * C type : OSVR_PoseState*
	 */
	public Pointer worldFromRoomAppend;
	/**
	 * Overrides head space<br>
	 * C type : OSVR_PoseState*
	 */
	public Pointer roomFromHeadReplace;
	public double nearClipDistanceMeters;
	public double farClipDistanceMeters;
	public OSVR_RenderParams() {
		super();
	}
	protected List<String> getFieldOrder() {
		return Arrays.asList("worldFromRoomAppend", "roomFromHeadReplace", "nearClipDistanceMeters", "farClipDistanceMeters");
	}
	/**
	 * @param worldFromRoomAppend Room space to insert<br>
	 * C type : OSVR_PoseState*<br>
	 * @param roomFromHeadReplace Overrides head space<br>
	 * C type : OSVR_PoseState*
	 */
	public OSVR_RenderParams(Pointer worldFromRoomAppend, Pointer roomFromHeadReplace, double nearClipDistanceMeters, double farClipDistanceMeters) {
		super();
		this.worldFromRoomAppend = worldFromRoomAppend;
		this.roomFromHeadReplace = roomFromHeadReplace;
		this.nearClipDistanceMeters = nearClipDistanceMeters;
		this.farClipDistanceMeters = farClipDistanceMeters;
	}
	public OSVR_RenderParams(Pointer peer) {
		super(peer);
	}
	public static class ByReference extends OSVR_RenderParams implements Structure.ByReference {
		
	};
	public static class ByValue extends OSVR_RenderParams implements Structure.ByValue {
		
	};
}
