package dev.blackhig.zhebushigudu.lover.features.modules.render;

import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import java.awt.Color;
import dev.blackhig.zhebushigudu.lover.util.ColorUtil;
import dev.blackhig.zhebushigudu.lover.features.modules.client.ClickGui;
import net.minecraft.util.math.RayTraceResult;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class BlockHighlight extends Module
{
    private final Setting<Float> lineWidth;
    private final Setting<Integer> cAlpha;
    
    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Category.RENDER, false, false, false);
        this.lineWidth = this.register(new Setting<>("LineWidth", 1.0f, 0.1f, 5.0f));
        this.cAlpha = this.register(new Setting<>("Alpha", 255, 0, 255));
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        final RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, ClickGui.getInstance().rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), false);
        }
    }
}
