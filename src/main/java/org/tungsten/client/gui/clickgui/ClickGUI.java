package org.tungsten.client.gui.clickgui;

import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import me.x150.ul.HtmlScreen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.tungsten.client.Tungsten;

public class ClickGUI extends HtmlScreen {


	static boolean ctx_setup = false;

	public ClickGUI() {
		super("file:///tungsten/appdata/gui/clickgui.html");
	}

	@Contract(" -> new")
	public static @NotNull ClickGUI create() {
		ctx_setup = false;
		return new ClickGUI();
	}

	@Override
	protected void init() {
		super.init();
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
				.setProperty("tungstenBridge", db.getConversionUtils().toJavascript(ctx, Tungsten.tungstenBridge), 0);
		ctxl.unlock();
		ctxl.close();
		Tungsten.LOGGER.info("set up context");
	}
}
