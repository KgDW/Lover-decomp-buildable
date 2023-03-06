package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import dev.blackhig.zhebushigudu.lover.event.events.BlockBreakEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderGlobal.class })
public class MixinRenderGlobal
{
    @Inject(method = { "sendBlockBreakProgress" }, at = { @At("HEAD") })
    public void onSendingBlockBreakProgressPre(final int breakerId, final BlockPos pos, final int progress, final CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new BlockBreakEvent(breakerId, pos, progress));
    }
}
