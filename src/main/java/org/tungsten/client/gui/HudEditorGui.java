package org.tungsten.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.tungsten.client.feature.module.modules.render.Hud;

public class HudEditorGui extends Screen {
    private final Screen previousScreen;
    public HudEditorGui(Screen prevScreen) {
        super(Text.literal("Hud Editor"));
        this.previousScreen = prevScreen;
    }

    public static boolean selected;
    private boolean resizeClicked;
    private int leg; //y
    private int arm; //x
    private int grandfatherMouse; //y
    private int grandmotherMouse; //x
    private int grandfatherHeight; //y
    private int grandmotherWidth; //x
    private boolean shouldRenderLogo = true;

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(shouldRenderLogo) HudElementRegistry.renderLogo();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(selected) {
            Hud.logoX = (int) mouseX - arm;
            Hud.logoY = (int) mouseY - leg;
        }

        if(resizeClicked) {
            Hud.logoWidth = grandmotherWidth + (int) mouseX - grandmotherMouse;
            Hud.logoHeight = grandfatherHeight + (int) mouseY - grandfatherMouse;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_2 && resizeClicked) resizeClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_1) {
            if(!((mouseX >= Hud.logoX && mouseX <= Hud.logoX + Hud.logoWidth) && (mouseY >= Hud.logoY && mouseY <= Hud.logoY + Hud.logoHeight))) {
                selected = false;
                return false;
            } else {
                selected = true;
                arm = ((int) mouseX) - Hud.logoX;
                leg = ((int) mouseY) - Hud.logoY;
            }
        }

        if(button == GLFW.GLFW_MOUSE_BUTTON_2) {
           if(!((mouseX >= Hud.resizeX && mouseX <= Hud.resizeX + Hud.resizeWidth) && (mouseY >= Hud.resizeY && mouseY <= Hud.resizeY + Hud.resizeHeight))) {
               resizeClicked = false;
               return false;
           } else {
               resizeClicked = true;
               grandfatherMouse = (int) mouseY;
               grandmotherMouse = (int) mouseX;
               grandfatherHeight = Hud.logoHeight;
               grandmotherWidth = Hud.logoWidth;

           }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if(key == GLFW.GLFW_KEY_ESCAPE) {
            client.setScreen(this.previousScreen);
            return false;
        }
        if(key == GLFW.GLFW_KEY_DELETE && selected) shouldRenderLogo = false;
        return super.keyPressed(key, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

}

//            shouldShrink = deltaX < 0 || deltaY < 0;
//            shouldExpand = deltaX > 0 || deltaY > 0;