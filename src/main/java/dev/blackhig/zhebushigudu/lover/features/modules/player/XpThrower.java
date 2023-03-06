package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.RotationUtil;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.ArrayList;
import java.util.Comparator;

public class XpThrower extends Module
{
    Setting<Boolean> feetThrow;
    Setting<Integer> throwSpeed;
    Setting<Bind> throwKey;
    Setting<Boolean> stopXp;
    Setting<Boolean> noLowDurable;
    Setting<Integer> stopDurable;
    Setting<Boolean> autoXp;
    Setting<Integer> autoXpRange;
    Setting<Boolean> takeoffArmor;
    Setting<Integer> takeoffDurable;
    private final Setting<Integer> takeoffDelay;
    Timer timer;
    private int delay_count;
    
    public XpThrower() {
        super("XpThrower", "AutoXp by KijinSeija", Category.PLAYER, true, false, false);
        this.feetThrow = this.register(new Setting<>("FeetThrow", true));
        this.throwSpeed = this.register(new Setting<>("ThorwDelay", 20, 1, 1000));
        this.throwKey = this.register(new Setting<>("ThrowKey", new Bind(0)));
        this.stopXp = this.register(new Setting<>("StopXP", true));
        this.noLowDurable = this.register(new Setting<>("NoLowDurable", true, v -> this.stopXp.getValue()));
        this.stopDurable = this.register(new Setting<>("StopDurable", 100, 0, 100, v -> this.stopXp.getValue()));
        this.autoXp = this.register(new Setting<>("AutoThrowXp", false));
        this.autoXpRange = this.register(new Setting<>("AutoXpRange", 8, 0, 20, v -> this.autoXp.getValue()));
        this.takeoffArmor = this.register(new Setting<>("TakeoffArmor", true, v -> this.stopXp.getValue()));
        this.takeoffDurable = this.register(new Setting<>("TakeoffDurable", 100, 0, 100, v -> this.takeoffArmor.getValue()));
        this.takeoffDelay = this.register(new Setting<>("Delay", 0, 0, 5, v -> this.takeoffArmor.getValue()));
        this.timer = new Timer();
    }
    
    @Override
    public void onUpdate() {
        if (!this.throwKey.getValue().isDown() || !this.timer.passedDms(this.throwSpeed.getValue())) {
            if (this.autoXp.getValue() && !this.isRangeNotPlayer(this.autoXpRange.getValue())) {
                return;
            }
            if (!this.autoXp.getValue()) {
                return;
            }
        }
        if (this.stopXp.getValue() && this.stopDurable.getValue() <= this.getArmorDurable(this.noLowDurable.getValue())) {
            return;
        }
        final int XpSlot = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
        if (XpSlot == -1) {
            return;
        }
        if (this.takeoffArmor.getValue()) {
            this.takeArmorOff();
        }
        final int oldSlot = XpThrower.mc.player.inventory.currentItem;
        if (this.feetThrow.getValue()) {
            final float yaw = XpThrower.mc.player.cameraYaw;
            RotationUtil.faceYawAndPitch(yaw, 90.0f);
        }
        InventoryUtil.switchToHotbarSlot(XpSlot, false);
        XpThrower.mc.playerController.processRightClick(XpThrower.mc.player, XpThrower.mc.world, EnumHand.MAIN_HAND);
        InventoryUtil.switchToHotbarSlot(oldSlot, false);
    }
    
    public Double getArmorDurable(final boolean getLowestValue) {
        final ArrayList<Double> DurableList = new ArrayList<>();
        for (int i = 5; i <= 8; ++i) {
            final ItemStack armor = XpThrower.mc.player.inventoryContainer.getInventory().get(i);
            final double max_dam = armor.getMaxDamage();
            final double dam_left = armor.getMaxDamage() - armor.getItemDamage();
            final double percent = dam_left / max_dam * 100.0;
            DurableList.add(percent);
        }
        DurableList.sort(Comparator.naturalOrder());
        if (getLowestValue) {
            return DurableList.get(0);
        }
        return DurableList.get(DurableList.size() - 1);
    }
    
    private Boolean isRangeNotPlayer(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : XpThrower.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range) && !lover.friendManager.isFriend(player.getName())) {
                if (XpThrower.mc.player.posY - player.posY >= 5.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq(player);
                }
                else {
                    if (EntityUtil.mc.player.getDistanceSq(player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq(player);
                }
            }
        }
        return target == null;
    }
    
    private ItemStack getArmor(final int first) {
        return XpThrower.mc.player.inventoryContainer.getInventory().get(first);
    }
    
    private void takeArmorOff() {
        for (int slot = 5; slot <= 8; ++slot) {
            final ItemStack item = this.getArmor(slot);
            final double max_dam = item.getMaxDamage();
            final double dam_left = item.getMaxDamage() - item.getItemDamage();
            final double percent = dam_left / max_dam * 100.0;
            if (percent >= this.takeoffDurable.getValue() && !item.equals(Items.AIR)) {
                if (InventoryUtil.findItemInventorySlot(Items.AIR, false) == -1) {
                    return;
                }
                if (this.delay_count < this.takeoffDelay.getValue()) {
                    ++this.delay_count;
                    return;
                }
                this.delay_count = 0;
                XpThrower.mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, XpThrower.mc.player);
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.delay_count = 0;
    }
}
