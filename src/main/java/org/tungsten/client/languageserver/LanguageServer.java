package org.tungsten.client.languageserver;

import com.sun.source.tree.ClassTree;
import org.lwjgl.system.Library;
import org.tungsten.client.util.LibraryDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LanguageServer {

    private static LanguageServer inst = null;

    public static LanguageServer instance(){
        if(inst == null) inst = new LanguageServer();
        return inst;
    }

    List<ClassTreeBuilder.Class> classPool = new ArrayList<>();


    public LanguageServer()  {
        Path[] jarPaths = LibraryDownloader.libraries.values().toArray(Path[]::new);
        for(Path p : jarPaths) {
            try {
                ClassTreeBuilder.Class[] jarClassPool = ClassTreeBuilder.buildTreeFrom(p);
                Collections.addAll(classPool, jarClassPool);
            } catch (Exception e) {
                throw new RuntimeException("Language Server Threw Exception whilest trying to parse class tree from jar files\nOffending Jar File:" + p.toAbsolutePath() + "\nError:" + e.getStackTrace().toString());
            }
        }
    }


    public ClassTreeBuilder.Class findFirstClass(String name) {
        ClassTreeBuilder.Class mostLikelyMatch = null;
        int idx = 0;
        for(ClassTreeBuilder.Class clazz : classPool) {
            String clz_name = clazz.getSimpleName();
            if(clz_name.equalsIgnoreCase(name)) { return clazz; }
            if(clz_name.startsWith(name)) {
                int pidx = (name.length() -  clz_name.length()) * -1;
                if(pidx > idx) {
                    mostLikelyMatch = clazz;
                    idx = pidx;
                }
            }
        }
        return mostLikelyMatch;
    }

    public static String santizeMethod(String input) {
        String regex = "\\([^)]*\\)";
        return input.replaceAll(regex, "");
    }

    public ClassTreeBuilder.Member findFirstMember(ClassTreeBuilder.Class parentClass, String name) {
        name = santizeMethod(name);
        ClassTreeBuilder.Member mostLikelyMatch = null;
        int idx = 0;
        for(ClassTreeBuilder.Member member : parentClass.members()){
            if(member.name().equals(name)){ return member; }
            if(member.name().startsWith(name)) {
                int pidx = member.name().length() - name.length();
                if(pidx > idx){
                    mostLikelyMatch = member;
                    idx = pidx;
                }
            }
        }
        return mostLikelyMatch;
    }

    public ClassTreeBuilder.Class forName(String input) {
        input = input.substring(0, input.length() - 1);
        for(ClassTreeBuilder.Class cl : classPool) {
            if(cl.name().equalsIgnoreCase(input)) {
                return cl;
            }
        }
        throw new RuntimeException("Error while Parsing java language string, class returned non-real method");
    }


    public ClassTreeBuilder.Class descriptorToClass(String input) {
        input = santizeMethod(input);
        if(input.startsWith("L")) {
            input = input.substring(1);
            return forName(input);
        } else return null;
    }


    public String getFirstCompletion(String partial) {
        String buffer = "";
        ClassTreeBuilder.Class parentClass = null;
        for (int i = 0; i < partial.length(); i++) {
            char c = partial.charAt(i);
            if (c == '.' && parentClass == null) {
                parentClass = findFirstClass(buffer);
                buffer = "";
                continue;
                //consume the buffer and parse the next node
            } else if (c == '.') {
                ClassTreeBuilder.Member mostLikelyMember = findFirstMember(parentClass, buffer);
                parentClass = descriptorToClass(mostLikelyMember.desc());
                buffer = "";
                continue;
            }
            buffer += c;
        }
        if (parentClass != null) {
            System.out.println(parentClass.name());
            ClassTreeBuilder.Member mostLikelyMember = findFirstMember(parentClass, buffer);
            partial = partial.substring(0, partial.length() - buffer.length());
            if (mostLikelyMember.type().equals(ClassTreeBuilder.Member.Type.METHOD)) {
                return partial + mostLikelyMember.name() + "()";
            }
            if (mostLikelyMember.type().equals(ClassTreeBuilder.Member.Type.FIELD)) {
                return partial + mostLikelyMember.name();
            }
        }
        return "";
    }
}