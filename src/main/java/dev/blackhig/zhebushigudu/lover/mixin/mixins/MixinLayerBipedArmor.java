package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.NoRenderEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { LayerBipedArmor.class }, priority = 1898)
public class MixinLayerBipedArmor
{
    @Inject(method = { "setModelSlotVisible" }, at = { @At("HEAD") }, cancellable = true)
    protected void setModelSlotVisible(final ModelBiped model, final EntityEquipmentSlot slotIn, final CallbackInfo ci) {
        final NoRenderEvent event = new NoRenderEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.cancel();
        switch (slotIn.ordinal()) {
            case 2: {
                model.bipedHead.showModel = false;
                model.bipedHeadwear.showModel = false;
            }
            case 3: {
                model.bipedBody.showModel = false;
                model.bipedRightArm.showModel = false;
                model.bipedLeftArm.showModel = false;
            }
            case 4: {
                model.bipedBody.showModel = false;
                model.bipedRightLeg.showModel = false;
                model.bipedLeftLeg.showModel = false;
            }
            case 5: {
                model.bipedRightLeg.showModel = false;
                model.bipedLeftLeg.showModel = false;
            }
            default: {}
        }
    }
}
