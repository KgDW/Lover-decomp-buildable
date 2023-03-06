package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.Minecraft;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.PlayerTweaks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import dev.blackhig.zhebushigudu.lover.event.MixinInterface;
import net.minecraft.util.MovementInput;

@Mixin(value = { MovementInputFromOptions.class }, priority = 10001)
public class MixinMovementInputFromOptions extends MovementInput implements MixinInterface
{
    @Redirect(method = { "updatePlayerMoveState" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(final KeyBinding keyBinding) {
        final int keyCode = keyBinding.getKeyCode();
        if (keyCode <= 0) {
            return keyBinding.isKeyDown();
        }
        if (keyCode >= 256) {
            return keyBinding.isKeyDown();
        }
        if (!PlayerTweaks.getInstance().isOn()) {
            return keyBinding.isKeyDown();
        }
        if (!PlayerTweaks.getInstance().guiMove.getValue()) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen == null) {
            return keyBinding.isKeyDown();
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            return keyBinding.isKeyDown();
        }
        return Keyboard.isKeyDown(keyCode);
    }
}
