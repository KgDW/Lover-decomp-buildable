package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import dev.blackhig.zhebushigudu.lover.util.RotationUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class PacketXP extends Module
{
    public final Setting<Boolean> allowTakeOff;
    private final Setting<Integer> takeOffVal;
    private final Setting<Integer> delay;
    private final Setting<Boolean> rotate;
    private final Setting<Bind> bind;
    private int delay_count;
    int prvSlot;
    public static Boolean inft;
    public static Bind binds;
    
    public PacketXP() {
        super("PacketXP", "Allows you to XP instantly", Category.PLAYER, false, false, false);
        this.allowTakeOff = this.register(new Setting<>("AutoMend", false));
        this.rotate = this.register(new Setting<>("Feet", true));
        this.takeOffVal = this.register(new Setting<>("Durable%", 100, 0, 100));
        this.delay = this.register(new Setting<>("Delay", 0, 0, 5));
        this.bind = this.register(new Setting<>("PacketBind", new Bind(-1)));
    }
    
    private void rotateToPos(final BlockPos pos) {
        final float[] angle = MathUtil.calcAngle(PacketXP.mc.player.getPositionEyes(PacketXP.mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5f), (pos.getY() - 0.5f), (pos.getZ() + 0.5f)));
        lover.rotationManager.setPlayerRotations(angle[0], angle[1]);
    }
    
    @Override
    public void onEnable() {
        this.delay_count = 0;
    }
    
    @Override
    public void onUpdate() {
        PacketXP.inft = this.allowTakeOff.getValue();
        PacketXP.binds = this.bind.getValue();
        if (this.findExpInHotbar() == -1) {
            return;
        }
        if (this.bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(this.bind.getValue().getKey()) && PacketXP.mc.currentScreen == null) {
                if (this.findExpInHotbar() == -1) {
                    return;
                }
                this.usedXp();
            }
        }
        else if (this.bind.getValue().getKey() < -1 && Mouse.isButtonDown(convertToMouse(this.bind.getValue().getKey())) && PacketXP.mc.currentScreen == null) {
            if (this.rotate.getValue()) {
                RotationUtil.faceVector(new Vec3d(PacketXP.mc.player.posX, PacketXP.mc.player.posY - 1.0, PacketXP.mc.player.posZ), true);
            }
            if (this.findExpInHotbar() == -1) {
                return;
            }
            this.usedXp();
        }
    }
    
    public static int convertToMouse(final int key) {
        switch (key) {
            case -2: {
                return 0;
            }
            case -3: {
                return 1;
            }
            case -4: {
                return 2;
            }
            case -5: {
                return 3;
            }
            case -6: {
                return 4;
            }
            default: {
                return -1;
            }
        }
    }
    
    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (PacketXP.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    
    private void usedXp() {
        final float pitch = PacketXP.mc.player.rotationPitch;
        final float yawln = PacketXP.mc.player.rotationYaw;
        this.prvSlot = PacketXP.mc.player.inventory.currentItem;
        PacketXP.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.findExpInHotbar()));
        PacketXP.mc.player.rotationPitch = 90.0f;
        PacketXP.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        PacketXP.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.prvSlot));
        PacketXP.mc.player.rotationPitch = pitch;
        if (this.allowTakeOff.getValue()) {
            this.takeArmorOff();
        }
    }
    
    private ItemStack getArmor(final int first) {
        return PacketXP.mc.player.inventoryContainer.getInventory().get(first);
    }
    
    private void takeArmorOff() {
        for (int slot = 5; slot <= 8; ++slot) {
            final ItemStack item = this.getArmor(slot);
            final double max_dam = item.getMaxDamage();
            final double dam_left = item.getMaxDamage() - item.getItemDamage();
            final double percent = dam_left / max_dam * 100.0;
            if (percent >= this.takeOffVal.getValue() && !item.equals(Items.AIR)) {
                if (!this.notInInv(Items.AIR)) {
                    return;
                }
                if (this.delay_count < this.delay.getValue()) {
                    ++this.delay_count;
                    return;
                }
                this.delay_count = 0;
                PacketXP.mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, PacketXP.mc.player);
            }
        }
    }
    
    public Boolean notInInv(final Item itemOfChoice) {
        int n = 0;
        if (itemOfChoice == PacketXP.mc.player.getHeldItemOffhand().getItem()) {
            return true;
        }
        for (int i = 35; i >= 0; --i) {
            final Item item = PacketXP.mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;
            }
            if (item != itemOfChoice) {
                ++n;
            }
        }
        if (n >= 35) {
            return false;
        }
        return true;
    }
    
    static {
        PacketXP.inft = false;
    }
}
