package dev.blackhig.zhebushigudu.lover.manager;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.features.Feature;

public class ReloadManager extends Feature
{
    public String prefix;
    
    public void init(final String prefix) {
        this.prefix = prefix;
        MinecraftForge.EVENT_BUS.register((Object)this);
        if (!Feature.fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "OyVey has been unloaded. Type " + prefix + "reload to reload.");
        }
    }
    
    public void unload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            lover.load();
            event.setCanceled(true);
        }
    }
}
