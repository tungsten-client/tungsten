package org.tungsten.client.feature.command.temp;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.GameModeArgumentType;
import net.minecraft.text.Text;
import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.command.ArgumentEntry;
import org.tungsten.client.feature.command.GenericCommand;

import java.util.List;

public class ExampleCommand extends GenericCommand {
    public ExampleCommand() {
        super("example", List.of(
                ArgumentEntry.create("Example Argument", GameModeArgumentType.gameMode()),
                ArgumentEntry.create("Secondary Argument", IntegerArgumentType.integer(-100,100))
        ));
    }

    @Override
    public void execute(String[] args) {
        Tungsten.client.player.sendMessage(Text.of("Command ran. Arguments:"));
        if(args.length == 2){
            Tungsten.client.player.sendMessage(Text.of(args[0]));
            Tungsten.client.player.sendMessage(Text.of(args[1]));
        } else{
            Tungsten.client.player.sendMessage(Text.of("Something went wrong..."));
        }
    }
}
