package org.tungsten.client.gui;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.registry.ModuleRegistry;

public class KeybindsMenu extends HtmlScreen {

    public KeybindsMenu() {
        super("file:///tungsten/appdata/gui/keybind.html");
    }

    @Override
    protected void init(){
        super.init();
    }

    @Override
    public void reload(){
        super.reload();
        if(!ctx_setup){
            this.setupContext();
            ctx_setup = true;
        }
    }


    static boolean ctx_setup = false;

    public static KeybindsMenu create(){
        ctx_setup = false;
        return new KeybindsMenu();
    }


    public void setupContext(){
        Databind db = new Databind(DatabindConfiguration.builder().build());
        JavascriptContextLock ctxl = this.getUlm().getWebController().getView().lockJavascriptContext();
        JavascriptContext ctx = ctxl.getContext();
        ctx.getGlobalContext().getGlobalObject().setProperty("tungstenBridge", db.getConversionUtils().toJavascript(ctx, new TungstenBridge()), 0);
        ctxl.unlock();
        ctxl.close();
        Tungsten.LOGGER.info("set up context");
    }


}
