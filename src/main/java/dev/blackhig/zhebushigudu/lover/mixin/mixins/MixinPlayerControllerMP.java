package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import net.minecraft.item.ItemFood;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.PacketEat;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.blackhig.zhebushigudu.lover.event.events.ProcessRightClickBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.BlockEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.modules.player.TpsSync;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    public float getPlayerRelativeBlockHardnessHook(final IBlockState state, final EntityPlayer player, final World worldIn, final BlockPos pos) {
        return state.getPlayerRelativeBlockHardness(player, worldIn, pos) * ((TpsSync.getInstance().isOn() && TpsSync.getInstance().mining.getValue()) ? (1.0f / lover.serverManager.getTpsFactor()) : 1.0f);
    }
    
    @Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void clickBlockHook(final BlockPos pos, final EnumFacing face, final CallbackInfoReturnable<Boolean> info) {
        final BlockEvent event = new BlockEvent(3, pos, face);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void onPlayerDamageBlockHook(final BlockPos pos, final EnumFacing face, final CallbackInfoReturnable<Boolean> info) {
        final BlockEvent event = new BlockEvent(4, pos, face);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Shadow
    public void syncCurrentPlayItem() {
    }
    
    @Inject(method = { "processRightClickBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void processRightClickBlock(final EntityPlayerSP player, final WorldClient worldIn, final BlockPos pos, final EnumFacing direction, final Vec3d vec, final EnumHand hand, final CallbackInfoReturnable<EnumActionResult> cir) {
        final ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.instance.player.getHeldItem(hand));
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }
    
    @Inject(method = { "onStoppedUsingItem" }, at = { @At("HEAD") }, cancellable = true)
    private void onStoppedUsingItem(final EntityPlayer playerIn, final CallbackInfo ci) {
        if (!PacketEat.getInstance().isOn()) {
            return;
        }
        if (!(playerIn.getHeldItem(playerIn.getActiveHand()).getItem() instanceof ItemFood)) {
            return;
        }
        this.syncCurrentPlayItem();
        playerIn.stopActiveHand();
        ci.cancel();
    }
}
