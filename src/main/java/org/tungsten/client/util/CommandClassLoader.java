package org.tungsten.client.util;

import java.net.URL;
import java.net.URLClassLoader;

public class CommandClassLoader extends URLClassLoader{

    private static CommandClassLoader INSTANCE;

    public CommandClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }


    public static CommandClassLoader getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CommandClassLoader(CommandClassLoader.class.getClassLoader());
        }
        return INSTANCE;
    }

    public Class<?> registerClass(byte[] classBytes) {
        return super.defineClass(null, classBytes, 0, classBytes.length);
    }
}