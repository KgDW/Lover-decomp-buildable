package dev.blackhig.zhebushigudu.lover.event.events.a;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraft.network.Packet;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class PacketEvent extends EventStage
{
    public final Packet<?> packet;
    
    public PacketEvent(final int stage, final Packet<?> packet) {
        super(stage);
        this.packet = packet;
    }
    
    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }
    
    @Cancelable
    public static class Send extends PacketEvent
    {
        public Send(final int stage, final Packet<?> packet) {
            super(stage, packet);
        }
    }
    
    @Cancelable
    public static class Receive extends PacketEvent
    {
        public Receive(final int stage, final Packet<?> packet) {
            super(stage, packet);
        }
    }
}
