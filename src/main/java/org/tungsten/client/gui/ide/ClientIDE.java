package org.tungsten.client.gui.ide;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;
import org.tungsten.client.gui.clickgui.ClickGUI;

public class ClientIDE extends HtmlScreen {

    public ClientIDE() {
        super("file://C:\\Users\\burke\\IdeaProjects\\tungstenDevving\\src\\main\\resources\\ide\\index.html"); //placeholder, please implement when actual
    }

    @Override
    protected void init() {
        super.init();
    }

    static boolean ctx_setup = false;

    public static ClientIDE create() {
        ctx_setup = false;
        return new ClientIDE();
    }

    @Override
    public void reload() {
        super.reload();
        if (!ctx_setup) {
            this.setupContext();
            ctx_setup = true;
        }
    }


    public void setupContext() {
        Databind db = new Databind(DatabindConfiguration.builder().build());
        JavascriptContextLock ctxl = this.getUlm().getWebController().getView().lockJavascriptContext();
        JavascriptContext ctx = ctxl.getContext();
        ctx.getGlobalContext()
                .getGlobalObject()
                .setProperty("binders", db.getConversionUtils().toJavascript(ctx, new EditorBridge()), 0);
        ctxl.unlock();
        ctxl.close();
    }
}