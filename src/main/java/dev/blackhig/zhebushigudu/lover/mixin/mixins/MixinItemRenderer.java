package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import net.minecraft.util.math.MathHelper;
import dev.blackhig.zhebushigudu.lover.util.render.RenderUtil;
import dev.blackhig.zhebushigudu.lover.features.modules.render.ViewModel;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.RenderItemEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import dev.blackhig.zhebushigudu.lover.util.ColorUtil;
import dev.blackhig.zhebushigudu.lover.features.modules.client.ClickGui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import dev.blackhig.zhebushigudu.lover.features.modules.render.HandChams;
import dev.blackhig.zhebushigudu.lover.features.modules.render.SmallShield;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.EnumHandSide;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemRenderer.class })
public abstract class MixinItemRenderer
{
    @Shadow
    @Final
    public Minecraft mc;
    private boolean injection;
    
    public MixinItemRenderer() {
        this.injection = true;
    }
    
    @Shadow
    public abstract void renderItemInFirstPerson(final AbstractClientPlayer p0, final float p1, final float p2, final EnumHand p3, final float p4, final ItemStack p5, final float p6);
    
    @Shadow
    protected abstract void renderArmFirstPerson(final float p0, final float p1, final EnumHandSide p2);
    
    @Inject(method = { "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V" }, at = { @At("HEAD") }, cancellable = true)
    public void renderItemInFirstPersonHook(final AbstractClientPlayer player, final float p_187457_2_, final float p_187457_3_, final EnumHand hand, final float p_187457_5_, final ItemStack stack, final float p_187457_7_, final CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            final SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.isOn()) {
                    xOffset = offset.mainX.getValue();
                    yOffset = offset.mainY.getValue();
                }
            }
            else if (offset.isOn()) {
                xOffset = offset.offX.getValue();
                yOffset = offset.offY.getValue();
            }
            if (HandChams.getINSTANCE().isOn() && hand == EnumHand.MAIN_HAND && stack.isEmpty()) {
                if (HandChams.getINSTANCE().mode.getValue().equals(HandChams.RenderMode.WIREFRAME)) {
                    this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                }
                GlStateManager.pushMatrix();
                if (HandChams.getINSTANCE().mode.getValue().equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPushAttrib(1048575);
                }
                else {
                    GlStateManager.pushAttrib();
                }
                if (HandChams.getINSTANCE().mode.getValue().equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glPolygonMode(1032, 6913);
                }
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                if (HandChams.getINSTANCE().mode.getValue().equals(HandChams.RenderMode.WIREFRAME)) {
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                }
                GL11.glColor4f(((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRed() / 255.0f) : (HandChams.getINSTANCE().red.getValue() / 255.0f), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getGreen() / 255.0f) : (HandChams.getINSTANCE().green.getValue() / 255.0f), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? (ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getBlue() / 255.0f) : (HandChams.getINSTANCE().blue.getValue() / 255.0f), HandChams.getINSTANCE().alpha.getValue() / 255.0f);
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
            }
            if (SmallShield.getINSTANCE().isOn() && (!stack.isEmpty || HandChams.getINSTANCE().isOff())) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            }
            else if (!stack.isEmpty || HandChams.getINSTANCE().isOff()) {
                this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_, stack, p_187457_7_);
            }
            this.injection = true;
        }
    }
    
    @Inject(method = { "transformSideFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void transformSideFirstPerson(final EnumHandSide hand, final float p_187459_2_, final CallbackInfo cancel) {
        final RenderItemEvent event = new RenderItemEvent(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (ViewModel.getInstance().isEnabled()) {
            final boolean bob = ViewModel.getInstance().isDisabled() || ViewModel.getInstance().doBob.getValue();
            final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
            GlStateManager.translate(i * 0.56f, -0.52f + (bob ? p_187459_2_ : 0.0f) * -0.6f, -0.72f);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(event.getMainX(), event.getMainY(), event.getMainZ());
                RenderUtil.rotationHelper((float)event.getMainRotX(), (float)event.getMainRotY(), (float)event.getMainRotZ());
            }
            else {
                GlStateManager.translate(event.getOffX(), event.getOffY(), event.getOffZ());
                RenderUtil.rotationHelper((float)event.getOffRotX(), (float)event.getOffRotY(), (float)event.getOffRotZ());
            }
            cancel.cancel();
        }
    }
    
    @Inject(method = { "transformEatFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    private void transformEatFirstPerson(final float p_187454_1_, final EnumHandSide hand, final ItemStack stack, final CallbackInfo cancel) {
        if (ViewModel.getInstance().isEnabled()) {
            if (!ViewModel.getInstance().noEatAnimation.getValue()) {
                final float f = Minecraft.getMinecraft().player.getItemInUseCount() - p_187454_1_ + 1.0f;
                final float f2 = f / stack.getMaxItemUseDuration();
                if (f2 < 0.8f) {
                    final float f3 = MathHelper.abs(MathHelper.cos(f / 4.0f * 3.1415927f) * 0.1f);
                    GlStateManager.translate(0.0f, f3, 0.0f);
                }
                final float f4 = 1.0f - (float)Math.pow(f2, 27.0);
                final int i = (hand == EnumHandSide.RIGHT) ? 1 : -1;
                GlStateManager.translate(f4 * 0.6f * i * ViewModel.getInstance().eatX.getValue(), f4 * 0.5f * -ViewModel.getInstance().eatY.getValue(), 0.0);
                GlStateManager.rotate(i * f4 * 90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(f4 * 10.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(i * f4 * 30.0f, 0.0f, 0.0f, 1.0f);
            }
            cancel.cancel();
        }
    }
}
