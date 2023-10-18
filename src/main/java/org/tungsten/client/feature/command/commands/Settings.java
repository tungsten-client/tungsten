package org.tungsten.client.feature.command.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import org.tungsten.client.feature.command.GenericCommand;
import org.tungsten.client.gui.HudElementRegistry;
import org.tungsten.client.util.render.notification.Notifications;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class Settings extends GenericCommand {
    public Settings() {
        super("settings", "Manually overwrite module settings.", "manual");
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        final String ha = "New Alpha";
        builder.then(literal("hudAlpha").then(argument(
            "New Alpha", FloatArgumentType.floatArg(0F, 101F)).executes(context -> {
            HudElementRegistry.setBeta(FloatArgumentType.getFloat(context, ha) / 100F);
            Notifications.newNotification("This is a very, extremely long 12 34567890", 10_000).title("Command").build();
            Notifications.newNotification("This is a very, extremely long1234567890", 10_000).title("Command").build();
            Notifications.newNotification("724358686868686868686868686868686868686868686868686868", 10_000).title("Command").build();
            Notifications.newNotification("This is a fairly, long message.", 10_000).title("Command").build();
            Notifications.newNotification("This is a Medium MSG.", 10_000).title("Command").build();
            Notifications.newNotification("This is Average.", 10_000).title("Command").build();
            return SINGLE_SUCCESS;
        })));
    }
}
// "I have to type out so much that this is almost comically large. God I hate you for doing this. Might as well publish a book"