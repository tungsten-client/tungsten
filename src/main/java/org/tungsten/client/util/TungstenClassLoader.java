package org.tungsten.client.util;

import java.net.URL;
import java.net.URLClassLoader;

public class TungstenClassLoader extends URLClassLoader {

	private static TungstenClassLoader INSTANCE;

	public TungstenClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}


	public static TungstenClassLoader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TungstenClassLoader(TungstenClassLoader.class.getClassLoader());
		}
		return INSTANCE;
	}

	public Class<?> registerClass(byte[] classBytes) {
		return super.defineClass(null, classBytes, 0, classBytes.length);
	}
}