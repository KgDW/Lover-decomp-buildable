package dev.blackhig.zhebushigudu.lover.mixin;

import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class loverLoader implements IFMLLoadingPlugin
{
    private static boolean isObfuscatedEnvironment;
    
    public static void OyVeyLoader() {
        lover.LOGGER.info("\n\nLoading mixins by Alpha432");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.lover.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        lover.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
        loverLoader.isObfuscatedEnvironment = (boolean) data.get("runtimeDeobfuscationEnabled");
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    static {
        loverLoader.isObfuscatedEnvironment = false;
    }
}
