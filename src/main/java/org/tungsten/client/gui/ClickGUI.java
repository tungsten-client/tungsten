package org.tungsten.client.gui;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;

public class ClickGUI extends HtmlScreen {


    public ClickGUI() {
        super("file:///tungsten/appdata/gui/index.html");
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

    public static ClickGUI create(){
        ctx_setup = false;
        return new ClickGUI();
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
