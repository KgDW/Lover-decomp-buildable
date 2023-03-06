package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.NoRenderEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemPickaxe;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.NoHitBox;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public class MixinEntityRenderer
{
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entityIn, final AxisAlignedBB boundingBox, final Predicate predicate) {
        if (NoHitBox.getINSTANCE().isOn() && ((Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && NoHitBox.getINSTANCE().pickaxe.getValue()) || (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && NoHitBox.getINSTANCE().crystal.getValue()) || (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE && NoHitBox.getINSTANCE().gapple.getValue()) || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.FLINT_AND_STEEL || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.TNT_MINECART)) {
            return new ArrayList<Entity>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }
    
    @Inject(at = { @At("HEAD") }, cancellable = true, method = { "hurtCameraEffect" })
    public void hurtCameraEffect(final float ticks, final CallbackInfo ci) {
        final NoRenderEvent event = new NoRenderEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
