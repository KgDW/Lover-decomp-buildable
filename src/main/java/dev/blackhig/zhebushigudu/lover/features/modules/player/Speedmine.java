package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.event.events.BlockEvent;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.util.render.RenderUtil;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Speedmine extends Module
{
    private static Speedmine INSTANCE;
    public final Timer timer;
    private final Setting<Float> range;
    public Setting<Boolean> tweaks;
    public Setting<Mode> mode;
    public Setting<Float> damage;
    public Setting<Boolean> reset;
    public Setting<Boolean> noBreakAnim;
    public Setting<Boolean> noDelay;
    public Setting<Boolean> noSwing;
    public Setting<Boolean> allow;
    public Setting<Boolean> webSwitch;
    public Setting<Boolean> render;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    public final Setting<Float> lineWidth;
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    public float breakTime;
    private boolean isMining;
    private BlockPos lastPos;
    private EnumFacing lastFacing;
    
    public Speedmine() {
        super("Speedmine", "packet mine.", Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.range = this.register(new Setting<>("Range", 10.0f, 0.0f, 50.0f));
        this.tweaks = this.register(new Setting<>("Tweaks", true));
        this.mode = this.register(new Setting<>("Mode", Mode.PACKET, v -> this.tweaks.getValue()));
        this.damage = this.register(new Setting<>("Damage", 0.7f, 0.0f, 1.0f, v -> this.mode.getValue() == Mode.DAMAGE && this.tweaks.getValue()));
        this.reset = this.register(new Setting<>("Reset", true));
        this.noBreakAnim = this.register(new Setting<>("NoBreakAnim", false));
        this.noDelay = this.register(new Setting<>("NoDelay", false));
        this.noSwing = this.register(new Setting<>("NoSwing", false));
        this.allow = this.register(new Setting<>("AllowMultiTask", false));
        this.webSwitch = this.register(new Setting<>("WebSwitch", false));
        this.render = this.register(new Setting<>("Render", true));
        this.red = this.register(new Setting<>("Red", 125, 0, 255, v -> this.render.getValue()));
        this.green = this.register(new Setting<>("Green", 0, 0, 255, v -> this.render.getValue()));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255, v -> this.render.getValue()));
        this.box = this.register(new Setting<>("Box", Boolean.TRUE, v -> this.render.getValue()));
        this.boxAlpha = this.register(new Setting<>("BoxAlpha", 85, 0, 255, v -> this.box.getValue() && this.render.getValue()));
        this.outline = this.register(new Setting<>("Outline", Boolean.TRUE, v -> this.render.getValue()));
        this.lineWidth = this.register(new Setting<>("LineWidth", 1.0f, 0.1f, 5.0f, v -> this.outline.getValue() && this.render.getValue()));
        this.breakTime = -1.0f;
        this.setInstance();
    }
    
    public static Speedmine getInstance() {
        if (Speedmine.INSTANCE == null) {
            Speedmine.INSTANCE = new Speedmine();
        }
        return Speedmine.INSTANCE;
    }
    
    private void setInstance() {
        Speedmine.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (this.currentPos != null) {
            if (Speedmine.mc.player != null && Speedmine.mc.player.getDistanceSq(this.currentPos) > MathUtil.square(this.range.getValue())) {
                this.currentPos = null;
                this.currentBlockState = null;
                return;
            }
            if (fullNullCheck()) {
                return;
            }
            if (!Speedmine.mc.world.getBlockState(this.currentPos).equals(this.currentBlockState) || Speedmine.mc.world.getBlockState(this.currentPos).getBlock() == Blocks.AIR) {
                this.currentPos = null;
                this.currentBlockState = null;
            }
            else if (this.webSwitch.getValue() && this.currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (this.noDelay.getValue()) {
            Speedmine.mc.playerController.blockHitDelay = 0;
        }
        if (this.isMining && this.lastPos != null && this.lastFacing != null && this.noBreakAnim.getValue()) {
            Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.lastPos, this.lastFacing));
        }
        if (this.reset.getValue() && Speedmine.mc.gameSettings.keyBindUseItem.isKeyDown() && !this.allow.getValue()) {
            Speedmine.mc.playerController.isHittingBlock = false;
        }
    }
    
    @Override
    public void onRender3D(final Render3DEvent render3DEvent) {
        if (this.render.getValue() && this.currentPos != null) {
            final Color color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.boxAlpha.getValue());
            RenderUtil.boxESP(this.currentPos, color, this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true);
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            if (this.noSwing.getValue() && event.getPacket() instanceof CPacketAnimation) {
                event.setCanceled(true);
            }
            final CPacketPlayerDigging packet;
            if (this.noBreakAnim.getValue() && event.getPacket() instanceof CPacketPlayerDigging && (packet = event.getPacket()) != null) {
                packet.getPosition();
                try {
                    for (final Entity entity : Speedmine.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(packet.getPosition()))) {
                        if (!(entity instanceof EntityEnderCrystal)) {
                            continue;
                        }
                        this.showAnimation();
                        return;
                    }
                }
                catch (final Exception ex) {}
                if (packet.getAction().equals(CPacketPlayerDigging.Action.START_DESTROY_BLOCK)) {
                    this.showAnimation(true, packet.getPosition(), packet.getFacing());
                }
                if (packet.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    this.showAnimation();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockEvent(final BlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.world.getBlockState(event.pos).getBlock() instanceof BlockEndPortalFrame) {
            Speedmine.mc.world.getBlockState(event.pos).getBlock().setHardness(50.0f);
        }
        if (event.getStage() == 3 && this.reset.getValue() && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4 && this.tweaks.getValue() && BlockUtil.canBreak(event.pos)) {
            if (this.reset.getValue()) {
                Speedmine.mc.playerController.isHittingBlock = false;
            }
            switch (this.mode.getValue()) {
                case PACKET: {
                    if (this.currentPos == null) {
                        this.currentPos = event.pos;
                        this.currentBlockState = Speedmine.mc.world.getBlockState(this.currentPos);
                        final ItemStack object = new ItemStack(Items.DIAMOND_PICKAXE);
                        this.breakTime = object.getDestroySpeed(this.currentBlockState) / 3.71f;
                        this.timer.reset();
                    }
                    Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                    event.setCanceled(true);
                    break;
                }
                case DAMAGE: {
                    if (Speedmine.mc.playerController.curBlockDamageMP < this.damage.getValue()) {
                        break;
                    }
                    Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                    break;
                }
                case INSTANT: {
                    Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.pos, event.facing));
                    Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.pos, event.facing));
                    Speedmine.mc.playerController.onPlayerDestroyBlock(event.pos);
                    Speedmine.mc.world.setBlockToAir(event.pos);
                    break;
                }
            }
        }
    }
    
    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (Speedmine.mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                return i;
            }
        }
        return -1;
    }
    
    private void showAnimation(final boolean isMining, final BlockPos lastPos, final EnumFacing lastFacing) {
        this.isMining = isMining;
        this.lastPos = lastPos;
        this.lastFacing = lastFacing;
    }
    
    public void showAnimation() {
        this.showAnimation(false, null, null);
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Speedmine.INSTANCE = new Speedmine();
    }
    
    public enum Mode
    {
        PACKET, 
        DAMAGE, 
        INSTANT
    }
}
