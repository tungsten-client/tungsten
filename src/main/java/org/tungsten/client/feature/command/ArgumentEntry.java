package org.tungsten.client.feature.command;

import com.mojang.brigadier.arguments.ArgumentType;

public record ArgumentEntry(String name, ArgumentType type) {
    public static ArgumentEntry create(String name, ArgumentType type) {
        return new ArgumentEntry(name, type);
    }
}
