package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class MCF extends Module
{
    private boolean clicked;
    
    public MCF() {
        super("MCF", "Middleclick Friends.", Category.MISC, true, false, false);
        this.clicked = false;
    }
    
    @Override
    public void onUpdate() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked && MCF.mc.currentScreen == null) {
                this.onClick();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void onClick() {
        final RayTraceResult result = MCF.mc.objectMouseOver;
        final Entity entity;
        if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
            if (lover.friendManager.isFriend(entity.getName())) {
                lover.friendManager.removeFriend(entity.getName());
                Command.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
            }
            else {
                lover.friendManager.addFriend(entity.getName());
                Command.sendMessage(ChatFormatting.AQUA + entity.getName() + ChatFormatting.AQUA + " has been friended.");
            }
        }
        this.clicked = true;
    }
}
