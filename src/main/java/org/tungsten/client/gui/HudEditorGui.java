package org.tungsten.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.misc.HUD;

public class HudEditorGui extends Screen {
    private final Screen previousScreen;
    public HudEditorGui(Screen prevScreen) {
        super(Text.literal("Hud Editor"));
        this.previousScreen = prevScreen;
    }

    public static int mousePosX;
    public static int mousePosY;
    public static int leg;
    public static int arm;
    public static boolean selected;
    public static boolean hoveringOverLogo;
    public static boolean resizeClicked;
    public static int grandfatherMouse; //y
    public static int grandmotherMouse; //x
    public static int grandfatherHeight; //y
    public static int grandmotherWidth; //x
    boolean shouldRenderLogo = true;

    @Override
    public void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        mousePosX = mouseX;
        mousePosY = mouseY;
        if (shouldRenderLogo) HudElementRegistry.renderLogo();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(selected) {
            HUD.logoX = (int) mouseX - arm;
            HUD.logoY = (int) mouseY - leg;
        }

        if(resizeClicked) {
            HUD.logoWidth = grandmotherWidth + (int) mouseX - grandmotherMouse;
            HUD.logoHeight = grandfatherHeight + (int) mouseY - grandfatherMouse;
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
            if (!((mouseX >= HUD.logoX && mouseX <= HUD.logoX + HUD.logoWidth) && (mouseY >= HUD.logoY && mouseY <= HUD.logoY + HUD.logoHeight))) {
                selected = false;
                return false;
            } else {
                selected = true;
                arm = ((int) mouseX) - HUD.logoX;
                leg = ((int) mouseY) - HUD.logoY;
            }
        }

        if(button == GLFW.GLFW_MOUSE_BUTTON_2) {
           if(!((mouseX >= HUD.resizeX && mouseX <= HUD.resizeX + HUD.resizeWidth) && (mouseY >= HUD.resizeY && mouseY <= HUD.resizeY + HUD.resizeHeight))) {
               resizeClicked = false;
               return false;
           } else {
               resizeClicked = true;
               grandfatherMouse = (int) mouseY;
               grandmotherMouse = (int) mouseX;
               grandfatherHeight = HUD.logoHeight;
               grandmotherWidth = HUD.logoWidth;

           }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(!((mouseX >= HUD.logoX && mouseX <= HUD.logoX + HUD.logoWidth) && (mouseY >= HUD.logoY && mouseY <= HUD.logoY + HUD.logoHeight))) {
            hoveringOverLogo = false;
            return false;
        } else hoveringOverLogo = true;
        System.out.println(hoveringOverLogo);
        return super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int key, int scanCode, int modifiers) {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
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