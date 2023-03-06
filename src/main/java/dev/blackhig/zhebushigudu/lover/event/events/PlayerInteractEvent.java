package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class PlayerInteractEvent extends EventStage
{
    private final MovementInput MovementInput;
    
    public PlayerInteractEvent(final EntityPlayer Player, final MovementInput movementInput) {
        this.MovementInput = movementInput;
    }
    
    public MovementInput getMovementInput() {
        return this.MovementInput;
    }
}
