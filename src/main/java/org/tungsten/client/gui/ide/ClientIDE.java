package org.tungsten.client.gui.ide;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.modules.client.ClientIDEModule;
import org.tungsten.client.gui.clickgui.ClickGUI;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class ClientIDE extends HtmlScreen {
    private static final Path MODULES_FOLDER = Tungsten.RUNDIR.resolve("modules");

    public ClientIDE() {
        super("file:///tungsten/appdata/ide/index.html"); //placeholder, please implement when actual
    }

    @Override
    protected void init() {
        super.init();
    }

    static boolean ctx_setup = false;

    public static void openIde() {
        if (ClientIDEModule.useNativeIde.getValue()) {
            try {
                Runtime.getRuntime().exec(new String[]{ClientIDEModule.nativeIdeLocation.getValue(), MODULES_FOLDER.toString()});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            ClientIDE ide = create();
            Tungsten.client.setScreen(ide);
            ide.reload();
        }
    }

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
