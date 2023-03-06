package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.renderer.DestroyBlockProgress;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ DestroyBlockProgress.class })
public interface AccessorminingPlayerEntId
{
    @Accessor("miningPlayerEntId")
    int miningPlayerEbntId();
}
