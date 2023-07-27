package org.tungsten.client.gui.ide;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;
import org.tungsten.client.gui.clickgui.ClickGUI;

public class ClientIDE extends HtmlScreen {
    public ClientIDE() {
        super("file:///tungsten/appdata/gui/ide.html"); //placeholder, please implement when actual
    }

    @Override
    protected void init() {
        super.init();
    }

    static boolean ctx_setup = false;

    public static ClickGUI create() {
        ctx_setup = false;
        return new ClickGUI();
    }



    public void setupContext() {
        Databind db = new Databind(DatabindConfiguration.builder().build());
        JavascriptContextLock ctxl = this.getUlm().getWebController().getView().lockJavascriptContext();
        JavascriptContext ctx = ctxl.getContext();
        ctx.getGlobalContext()
                .getGlobalObject()
                .setProperty("bindings", db.getConversionUtils().toJavascript(ctx, new EditorBridge()), 0);
        ctxl.unlock();
        ctxl.close();
    }

}
