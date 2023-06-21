package org.tungsten.client.feature.registry;

import org.tungsten.client.feature.command.GenericCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandRegistry {

    public static List<GenericCommand> commands = new ArrayList<>();

    public GenericCommand getByName(String name) {
        return null; //todo: implement genericcommand and genericmodule
    }

    public static void addCommand(GenericCommand command){
        commands.add(command);
    }

}
