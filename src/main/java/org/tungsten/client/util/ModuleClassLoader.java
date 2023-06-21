package org.tungsten.client.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleClassLoader extends URLClassLoader{

    public ModuleClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public Class<?> registerClass(byte[] classBytes) {
        return super.defineClass(null, classBytes, 0, classBytes.length);
    }
}