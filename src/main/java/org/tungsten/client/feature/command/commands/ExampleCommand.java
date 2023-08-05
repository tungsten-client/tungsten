package org.tungsten.client.feature.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.GenericCommand;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ExampleCommand extends GenericCommand {
    public ExampleCommand() {
        super("example", "example command", "ex");
    }


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            Tungsten.client.inGameHud.getChatHud().addMessage(Text.literal("example command"));
            return SINGLE_SUCCESS;
        });
    }
}
