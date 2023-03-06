package dev.blackhig.zhebushigudu.lover.util.HoleFillPlus;

import com.google.gson.JsonElement;
import com.mojang.util.UUIDTypeAdapter;
import com.google.gson.JsonParser;
import java.util.Collection;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.init.Blocks;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;

public class PlayerUtil implements Globals
{
    public static UUID getUUIDFromName(final String name) {
        try {
            final lookUpUUID process = new lookUpUUID(name);
            final Thread thread = new Thread(process);
            thread.start();
            thread.join();
            return process.getUUID();
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plusY) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : (cy - h); y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(PlayerUtil.mc.player.posX), Math.floor(PlayerUtil.mc.player.posY), Math.floor(PlayerUtil.mc.player.posZ));
    }
    
    public static double[] forward(final double speed) {
        float forward = PlayerUtil.mc.player.movementInput.moveForward;
        float side = PlayerUtil.mc.player.movementInput.moveStrafe;
        float yaw = PlayerUtil.mc.player.prevRotationYaw + (PlayerUtil.mc.player.rotationYaw - PlayerUtil.mc.player.prevRotationYaw) * PlayerUtil.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = PlayerUtil.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest) {
                    return i;
                }
                if (block instanceof BlockObsidian) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public static String convertStreamToString(final InputStream is) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }
    
    public static boolean isInHole() {
        final BlockPos player_block = getPlayerPos();
        return PlayerUtil.mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.west()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.north()).getBlock() != Blocks.AIR && PlayerUtil.mc.world.getBlockState(player_block.south()).getBlock() != Blocks.AIR;
    }
    
    public static String requestIDs(final String data) {
        try {
            final String query = "https://api.mojang.com/profiles/minecraft";
            final URL url = new URL("https://api.mojang.com/profiles/minecraft");
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            final OutputStream os = conn.getOutputStream();
            os.write(data.getBytes(StandardCharsets.UTF_8));
            os.close();
            final InputStream in = new BufferedInputStream(conn.getInputStream());
            final String res = convertStreamToString(in);
            in.close();
            conn.disconnect();
            return res;
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static boolean isMoving(final EntityLivingBase entity) {
        return entity.moveForward != 0.0f || entity.moveStrafing != 0.0f;
    }
    
    public static void setSpeed(final EntityLivingBase entity, final double speed) {
        final double[] dir = forward(speed);
        entity.motionX = dir[0];
        entity.motionZ = dir[1];
    }
    
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (PlayerUtil.mc.player != null && PlayerUtil.mc.player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = PlayerUtil.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static Item getBestItem(final Block block) {
        final String tool = block.getHarvestTool(block.getDefaultState());
        if (tool == null) {
            return Items.DIAMOND_PICKAXE;
        }
        final String s = tool;
        switch (s) {
            case "axe": {
                return Items.DIAMOND_AXE;
            }
            case "shovel": {
                return Items.DIAMOND_SHOVEL;
            }
            default: {
                return Items.DIAMOND_PICKAXE;
            }
        }
    }
    
    public static ItemStack getItemStackFromItem(final Item item) {
        if (PlayerUtil.mc.player == null) {
            return null;
        }
        for (int slot = 0; slot <= 9; ++slot) {
            if (PlayerUtil.mc.player.inventory.getStackInSlot(slot).getItem() == item) {
                return PlayerUtil.mc.player.inventory.getStackInSlot(slot);
            }
        }
        return null;
    }
    
    public static class lookUpUUID implements Runnable
    {
        private final String name;
        private volatile UUID uuid;
        
        public lookUpUUID(final String name) {
            this.name = name;
        }
        
        @Override
        public void run() {
            NetworkPlayerInfo profile;
            try {
                final ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<NetworkPlayerInfo>(Objects.requireNonNull(Globals.mc.getConnection()).getPlayerInfoMap());
                profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(this.name)).findFirst().orElse(null);
                assert profile != null;
                this.uuid = profile.getGameProfile().getId();
            }
            catch (final Exception e2) {
                profile = null;
            }
            if (profile == null) {
                final String s = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
                if (s != null) {
                    if (!s.isEmpty()) {
                        final JsonElement element = new JsonParser().parse(s);
                        if (element.getAsJsonArray().size() != 0) {
                            try {
                                final String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                                this.uuid = UUIDTypeAdapter.fromString(id);
                            }
                            catch (final Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        
        public UUID getUUID() {
            return this.uuid;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
