package org.tungsten.client.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;

public interface ScreenDuck {

    public <T extends Element & Drawable & Selectable> T addDrawableChildEx(T drawableElement);

}
