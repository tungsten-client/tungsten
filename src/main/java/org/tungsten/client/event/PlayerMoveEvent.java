package org.tungsten.client.event;

import org.tungsten.client.util.ClientPlayerEntityInterface;

public class PlayerMoveEvent extends Event {

    ClientPlayerEntityInterface player;
    public void setPlayer(ClientPlayerEntityInterface player) {
        this.player = player;
    }
    ClientPlayerEntityInterface getPlayer() {
        return this.player;
    }

}