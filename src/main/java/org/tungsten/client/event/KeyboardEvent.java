package org.tungsten.client.event;


public class KeyboardEvent extends Event{
    int keycode;
    int modifiers;
    int action;


    public KeyboardEvent(int keycode, int modifiers, int action){
        this.keycode = keycode;
        this.modifiers = modifiers;
        this.action = action;
    }

    public int getKeycode(){
        return this.keycode;
    }

    public int getModifiers(){
        return this.modifiers;
    }

    public int getAction(){
        return this.action;
    }
}
