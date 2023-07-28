package org.tungsten.client.api;

import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScreenManager {
    public static HashMap<Class<?>, List<ScreenVisitor>> screens = new HashMap<>();


    public static void registerVisitor(Class<?> visitable, ScreenVisitor sc){
        if (screens.containsKey(visitable)) {
            screens.get(visitable).add(sc);
        }else{
            List<ScreenVisitor> vs = new ArrayList<ScreenVisitor>();
            vs.add(sc);
            screens.put(visitable, vs);
        }
    }

    public static void unregisterVisitor(ScreenVisitor sc, Class<?> subscribed){
        if (screens.containsKey(subscribed)) {
            screens.get(subscribed).remove(sc);
        }
    }


    public static List<ScreenVisitor> getVisitors(Class<?> visitable){
        if(screens.containsKey(visitable)){
            return screens.get(visitable);
        }else{
            return new ArrayList<ScreenVisitor>();
        }
    }

}
