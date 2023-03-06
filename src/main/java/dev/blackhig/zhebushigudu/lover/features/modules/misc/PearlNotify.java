package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;

public class PearlNotify extends Module
{
    private boolean flag;
    
    public PearlNotify() {
        super("PearlNotify", "Notify pearl throws.", Category.MISC, true, false, false);
    }
    
    @Override
    public void onEnable() {
        this.flag = true;
    }
    
    @Override
    public void onUpdate() {
        if (PearlNotify.mc.world == null || PearlNotify.mc.player == null) {
            return;
        }
        Entity enderPearl = null;
        for (final Entity e : PearlNotify.mc.world.loadedEntityList) {
            if (e instanceof EntityEnderPearl) {
                enderPearl = e;
                break;
            }
        }
        if (enderPearl == null) {
            this.flag = true;
            return;
        }
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : PearlNotify.mc.world.playerEntities) {
            if (closestPlayer != null) {
                if (closestPlayer.getDistance(enderPearl) <= entity.getDistance(enderPearl)) {
                    continue;
                }
            }
            closestPlayer = entity;
        }
        if (closestPlayer == PearlNotify.mc.player) {
            this.flag = false;
        }
        if (closestPlayer != null && this.flag) {
            String faceing = enderPearl.getHorizontalFacing().toString();
            if (faceing.equals("west")) {
                faceing = "east";
            }
            else if (faceing.equals("east")) {
                faceing = "west";
            }
            Command.sendMessage(lover.friendManager.isFriend(closestPlayer.getName()) ? (ChatFormatting.AQUA + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!") : (ChatFormatting.RED + closestPlayer.getName() + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!"));
            this.flag = false;
        }
    }
}
