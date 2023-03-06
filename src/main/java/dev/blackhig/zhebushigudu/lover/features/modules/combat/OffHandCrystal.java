package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import net.minecraft.inventory.ClickType;
import dev.blackhig.zhebushigudu.lover.util.CrystalUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.util.e.EntityUtil;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import dev.blackhig.zhebushigudu.lover.lover;
import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import dev.blackhig.zhebushigudu.lover.features.modules.player.PacketXP;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class OffHandCrystal extends Module
{
    public Setting<Mode> mode;
    public Setting<Integer> delay;
    public Setting<Boolean> totem;
    public Setting<Boolean> autoSwitchback;
    public Setting<Double> sbHealth;
    public Setting<Boolean> autoSwitch;
    public Setting<Boolean> anti32k;
    public Setting<Double> rs;
    public Setting<SwingMode2> switchMode;
    public Setting<Boolean> elytra;
    public Setting<Boolean> holeCheck;
    public Setting<Double> holeSwitch;
    public Setting<Boolean> crystalCalculate;
    public Setting<Boolean> xp;
    public Setting<Boolean> crystalpop;
    public Setting<Double> maxSelfDmg;
    public static Boolean dev;
    private int totems;
    private int count;
    private static final Timer timer;
    
    public OffHandCrystal() {
        super("OffHandCrystal", "Allows you to switch up your Offhand.", Category.COMBAT, true, false, false);
        this.mode = this.register(new Setting<>("Item", Mode.Crystal));
        this.delay = this.register(new Setting<>("Delay", 0, 0, 1000));
        this.totem = this.register(new Setting<>("SwitchTotem", true));
        this.autoSwitchback = this.register(new Setting<>("SwitchBack", true));
        this.sbHealth = this.register(new Setting<>("Health", 11.0, 0.0, 36.0));
        this.autoSwitch = this.register(new Setting<>("SwitchGap", true));
        this.anti32k = this.register(new Setting<>("Anti32k", true));
        this.rs = this.register(new Setting("AttackRange", 7.0, 1.0, 20.0, v -> this.anti32k.getValue()));
        this.switchMode = this.register(new Setting<>("GapWhen", SwingMode2.Sword));
        this.elytra = this.register(new Setting<>("CheckElytra", true));
        this.holeCheck = this.register(new Setting<>("CheckHole", false));
        this.holeSwitch = this.register(new Setting("HoleHealth", 8.0, 0.0, 36.0, v -> this.holeCheck.getValue()));
        this.crystalCalculate = this.register(new Setting<>("CalculateDmg", true));
        this.xp = this.register(new Setting<>("Noxp", true));
        this.crystalpop = this.register(new Setting<>("Nocrystalpop", false));
        this.maxSelfDmg = this.register(new Setting("MaxSelfDmg", 26.0, 0.0, 36.0, v -> this.crystalCalculate.getValue()));
    }
    
    @Override
    public void onUpdate() {
        if (OffHandCrystal.mc.player == null) {
            return;
        }
        if (OffHandCrystal.dev) {
            return;
        }
        int crystals = OffHandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (OffHandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            crystals += OffHandCrystal.mc.player.getHeldItemOffhand().getCount();
        }
        int gapple = OffHandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (OffHandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            gapple += OffHandCrystal.mc.player.getHeldItemOffhand().getCount();
        }
        this.totems = OffHandCrystal.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (OffHandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        }
        Item item = null;
        if (this.xp.getValue() && PacketXP.inft) {
            if (PacketXP.binds.getKey() > -1) {
                if (Keyboard.isKeyDown(PacketXP.binds.getKey()) && OffHandCrystal.mc.currentScreen == null) {
                    return;
                }
            }
            else if (PacketXP.binds.getKey() < -1 && Mouse.isButtonDown(PacketXP.convertToMouse(PacketXP.binds.getKey())) && OffHandCrystal.mc.currentScreen == null) {
                return;
            }
        }
        if (this.crystalpop.getValue() && lover.moduleManager.getModuleByName("AutoCrystal+").isDisabled() && lover.moduleManager.getModuleByName("AutoCrystal").isDisabled() && lover.moduleManager.getModuleByName("StormCrystal").isEnabled()) {
            if (this.getItemSlot(Items.TOTEM_OF_UNDYING) != -1 && OffHandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                return;
            }
            if (this.getItemSlot(Items.TOTEM_OF_UNDYING) != -1 && OffHandCrystal.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                this.switch_Totem();
                return;
            }
        }
        if (!OffHandCrystal.mc.player.getHeldItemOffhand().isEmpty()) {
            item = OffHandCrystal.mc.player.getHeldItemOffhand().getItem();
        }
        if (item != null) {
            if (item.equals(Items.END_CRYSTAL)) {
                this.count = crystals;
            }
            else if (item.equals(Items.TOTEM_OF_UNDYING)) {
                this.count = this.totems;
            }
            else {
                this.count = gapple;
            }
        }
        else {
            this.count = 0;
        }
        final Item handItem = OffHandCrystal.mc.player.getHeldItemMainhand().getItem();
        final Item offhandItem = (this.mode.getValue() == Mode.Crystal) ? Items.END_CRYSTAL : Items.GOLDEN_APPLE;
        final Item sOffhandItem = (this.mode.getValue() == Mode.Crystal) ? Items.GOLDEN_APPLE : Items.END_CRYSTAL;
        if (this.anti32k.getValue()) {
            if (this.shouldTotem32k() && this.getItemSlot(Items.TOTEM_OF_UNDYING) != -1 && OffHandCrystal.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
                this.switch_Totem();
                return;
            }
            if (OffHandCrystal.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING && this.shouldTotem32k()) {
                return;
            }
        }
        boolean shouldSwitch;
        if (this.switchMode.getValue() == SwingMode2.Sword) {
            shouldSwitch = (OffHandCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Mouse.isButtonDown(1) && this.autoSwitch.getValue());
        }
        else {
            shouldSwitch = (Mouse.isButtonDown(1) && this.autoSwitch.getValue() && !(handItem instanceof ItemFood) && !(handItem instanceof ItemExpBottle) && !(handItem instanceof ItemBlock));
        }
        if (this.shouldTotem() && this.getItemSlot(Items.TOTEM_OF_UNDYING) != -1) {
            this.switch_Totem();
        }
        else if (shouldSwitch && this.getItemSlot(sOffhandItem) != -1) {
            if (!OffHandCrystal.mc.player.getHeldItemOffhand().getItem().equals(sOffhandItem)) {
                final int slot = (this.getItemSlot(sOffhandItem) < 9) ? (this.getItemSlot(sOffhandItem) + 36) : this.getItemSlot(sOffhandItem);
                this.switchTo(slot);
            }
        }
        else if (this.getItemSlot(offhandItem) != -1) {
            final int slot = (this.getItemSlot(offhandItem) < 9) ? (this.getItemSlot(offhandItem) + 36) : this.getItemSlot(offhandItem);
            if (!OffHandCrystal.mc.player.getHeldItemOffhand().getItem().equals(offhandItem)) {
                this.switchTo(slot);
            }
        }
    }
    
    private boolean shouldTotem() {
        return this.totem.getValue() && (this.checkHealth() || (OffHandCrystal.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && this.elytra.getValue()) || OffHandCrystal.mc.player.fallDistance >= 5.0f || (EntityUtil.isPlayerInHole() && this.holeCheck.getValue() && OffHandCrystal.mc.player.getHealth() + OffHandCrystal.mc.player.getAbsorptionAmount() <= this.holeSwitch.getValue()) || (this.crystalCalculate.getValue() && this.calcHealth()));
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : OffHandCrystal.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range) && (!EntityUtil.isTrapped(player, false, false, false, false, false))) {
                if (lover.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = OffHandCrystal.mc.player.getDistanceSq(player);
                }
                else {
                    if (OffHandCrystal.mc.player.getDistanceSq(player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = OffHandCrystal.mc.player.getDistanceSq(player);
                }
            }
        }
        return target;
    }
    
    private boolean shouldTotem32k() {
        if (this.anti32k.getValue()) {
            final EntityPlayer IS = this.getTarget(this.rs.getValue());
            if (IS != null && IS.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_SWORD) {
                final String nbt = IS.getHeldItem(EnumHand.MAIN_HAND).serializeNBT().copy().toString();
                return nbt.contains("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName");
            }
            return false;
        }
        return false;
    }
    
    private boolean calcHealth() {
        double maxDmg = 0.5;
        for (final Entity entity : OffHandCrystal.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            if (OffHandCrystal.mc.player.getDistance(entity) > 12.0f) {
                continue;
            }
            final double d = CrystalUtil.calculateDamage(entity.posX, entity.posY, entity.posZ, OffHandCrystal.mc.player);
            if (d <= maxDmg) {
                continue;
            }
            maxDmg = d;
        }
        return maxDmg - 0.5 > OffHandCrystal.mc.player.getHealth() + OffHandCrystal.mc.player.getAbsorptionAmount() || maxDmg > this.maxSelfDmg.getValue();
    }
    
    public boolean checkHealth() {
        final boolean lowHealth = OffHandCrystal.mc.player.getHealth() + OffHandCrystal.mc.player.getAbsorptionAmount() <= this.sbHealth.getValue();
        final boolean notInHoleAndLowHealth = lowHealth && !EntityUtil.isPlayerInHole();
        return this.holeCheck.getValue() ? notInHoleAndLowHealth : lowHealth;
    }
    
    private void switch_Totem() {
        if (this.totems != 0 && !OffHandCrystal.mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            final int slot = (this.getItemSlot(Items.TOTEM_OF_UNDYING) < 9) ? (this.getItemSlot(Items.TOTEM_OF_UNDYING) + 36) : this.getItemSlot(Items.TOTEM_OF_UNDYING);
            this.switchTo(slot);
        }
    }
    
    private void switchTo(final int slot) {
        try {
            if (OffHandCrystal.timer.passedMs(this.delay.getValue())) {
                OffHandCrystal.mc.playerController.windowClick(OffHandCrystal.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, OffHandCrystal.mc.player);
                OffHandCrystal.mc.playerController.windowClick(OffHandCrystal.mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, OffHandCrystal.mc.player);
                OffHandCrystal.mc.playerController.windowClick(OffHandCrystal.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, OffHandCrystal.mc.player);
                OffHandCrystal.timer.reset();
            }
        }
        catch (final Exception ignored) {}
    }
    
    private int getItemSlot(final Item input) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (OffHandCrystal.mc.player.inventory.getStackInSlot(i).getItem() == input) {
                itemSlot = i;
                break;
            }
        }
        return itemSlot;
    }
    
    @Override
    public void onDisable() {
        if (this.autoSwitchback.getValue()) {
            this.switch_Totem();
        }
    }
    
    static {
        OffHandCrystal.dev = false;
        timer = new Timer();
    }
    
    public enum Mode
    {
        Crystal, 
        Gap
    }
    
    public enum SwingMode2
    {
        Sword, 
        RClick
    }
}
