package org.tungsten.client.util;

import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {

	private static ModuleClassLoader INSTANCE;

	public ModuleClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}


	public static ModuleClassLoader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ModuleClassLoader(ModuleClassLoader.class.getClassLoader());
		}
		return INSTANCE;
	}

	public Class<?> registerClass(byte[] classBytes) {
		return super.defineClass(null, classBytes, 0, classBytes.length);
	}
}