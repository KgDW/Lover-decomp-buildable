package dev.blackhig.zhebushigudu.lover.features.modules.render;

import dev.blackhig.zhebushigudu.lover.event.events.BlockEvent;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.FadeUtils;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import dev.blackhig.zhebushigudu.lover.util.RenderUtils3D;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class SomeRenders extends Module
{
    FadeUtils fadeUtils;
    BlockPos pos;
    Setting<Boolean> hasFade;
    Setting<Integer> red;
    Setting<Integer> green;
    Setting<Integer> blue;
    Setting<Integer> alpha;
    Setting<FadeType> fadeType;
    Setting<FadeUtil> fadeUtil;
    Setting<FadeStyle> fadeStyle;
    Setting<Integer> fadeLength;
    Setting<RenderType> renderType;
    
    public SomeRenders() {
        super("SomeRenderers", "Renders", Category.RENDER, true, false, false);
        this.hasFade = this.register(new Setting<>("Fade", true));
        this.red = this.register(new Setting<>("Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.alpha = this.register(new Setting<>("Alpha", 100, 20, 255));
        this.fadeType = this.register(new Setting<>("FadeType", FadeType.FadeIn, v -> this.hasFade.getValue()));
        this.fadeUtil = this.register(new Setting<>("FadeUtil", FadeUtil.EasingDefault, v -> this.hasFade.getValue()));
        this.fadeStyle = this.register(new Setting<>("FadeStyle", FadeStyle.Heighten, v -> this.hasFade.getValue()));
        this.fadeLength = this.register(new Setting<>("FadeLength", 2000, 1000, 10000, v -> this.hasFade.getValue()));
        this.renderType = this.register(new Setting<>("RenderType", RenderType.Outline));
    }
    
    @SubscribeEvent
    public void clickingBlock(final BlockEvent event) {
        this.fadeUtils = new FadeUtils(this.fadeLength.getValue());
        this.pos = event.pos;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.pos == null) {
            return;
        }
        if (!this.hasFade.getValue()) {
            switch (this.renderType.getValue()) {
                case Outline: {
                    RenderUtils3D.drawBlockOutline(this.pos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.5f, true);
                }
                case Fill: {
                    RenderUtil.drawFilledBoxESPN(this.pos, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
                    break;
                }
            }
            return;
        }
        final Vec3d interpolateEntity = MathUtil.interpolateEntity(SomeRenders.mc.player, SomeRenders.mc.getRenderPartialTicks());
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(this.pos);
        axisAlignedBB = axisAlignedBB.grow(0.0020000000949949026).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z);
        double size;
        switch (this.fadeUtil.getValue()) {
            case EasingDefault: {
                size = ((this.fadeType.getValue() == FadeType.FadeOut) ? this.fadeUtils.easeOutQuad() : this.fadeUtils.easeInQuad());
                break;
            }
            case Eps: {
                size = ((this.fadeType.getValue() == FadeType.FadeOut) ? this.fadeUtils.getEpsEzFadeOut() : this.fadeUtils.getEpsEzFadeIn());
                break;
            }
            case Default: {
                size = ((this.fadeType.getValue() == FadeType.FadeOut) ? this.fadeUtils.getFadeOutDefault() : this.fadeUtils.getFadeInDefault());
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + this.fadeUtil.getValue());
            }
        }
        AxisAlignedBB axisAlignedBB2;
        switch (this.fadeStyle.getValue()) {
            case Heighten: {
                axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX, axisAlignedBB.minY + size, axisAlignedBB.maxZ);
                break;
            }
            case Growing: {
                final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
                final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
                final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
                final double full = axisAlignedBB.maxX - centerX;
                final double progressValX = full * size;
                final double progressValY = full * size;
                final double progressValZ = full * size;
                axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
                break;
            }
            default: {
                throw new IllegalStateException("Unexpected value: " + this.fadeStyle.getValue());
            }
        }
        switch (this.renderType.getValue()) {
            case Outline: {
                RenderUtils3D.drawBlockOutline(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.5f);
            }
            case Fill: {
                RenderUtil.drawFilledBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB());
            }
            case Solid: {
                RenderUtils3D.drawBlockOutline(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.5f);
                RenderUtil.drawFilledBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB());
                break;
            }
        }
    }
    
    enum FadeType
    {
        FadeIn, 
        FadeOut
    }
    
    enum RenderType
    {
        Outline, 
        Solid, 
        Fill
    }
    
    enum FadeStyle
    {
        Heighten, 
        Growing
    }
    
    enum FadeUtil
    {
        Eps, 
        Default, 
        EasingDefault
    }
}
