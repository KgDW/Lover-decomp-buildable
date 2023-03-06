package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ChatSuffix extends Module
{
    public ChatSuffix() {
        super("ChatSuffix", "suffix", Category.MISC, true, false, false);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = event.getPacket();
            final String message = packet.getMessage();
            if (!message.startsWith("/")) {
                String loverChat = message + " > Lover";
                if (loverChat.length() >= 256) {
                    loverChat = loverChat.substring(0, 256);
                }
                packet.message = loverChat;
            }
        }
    }
}
