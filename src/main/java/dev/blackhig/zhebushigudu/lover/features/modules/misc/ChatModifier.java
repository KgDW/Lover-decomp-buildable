package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.network.play.client.CPacketChatMessage;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ChatModifier extends Module
{
    private static ChatModifier INSTANCE;
    public Setting<Boolean> clean;
    public Setting<Boolean> infinite;
    public boolean check;
    
    public ChatModifier() {
        super("BetterChat", "Modifies your chat", Category.MISC, true, false, false);
        this.clean = this.register(new Setting<>("NoChatBackground", false, "Cleans your chat"));
        this.infinite = this.register(new Setting<>("InfiniteChat", false, "Makes your chat infinite."));
        this.setInstance();
    }
    
    public static ChatModifier getInstance() {
        if (ChatModifier.INSTANCE == null) {
            ChatModifier.INSTANCE = new ChatModifier();
        }
        return ChatModifier.INSTANCE;
    }
    
    private void setInstance() {
        ChatModifier.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            this.check = !s.startsWith(lover.commandManager.getPrefix());
        }
    }
    
    static {
        ChatModifier.INSTANCE = new ChatModifier();
    }
}
