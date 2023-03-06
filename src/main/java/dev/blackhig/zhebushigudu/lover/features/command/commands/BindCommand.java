package dev.blackhig.zhebushigudu.lover.features.command.commands;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", new String[] { "<module>", "<bind>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("Please specify a module.");
            return;
        }
        final String rkey = commands[1];
        final String moduleName = commands[0];
        final Module module = lover.moduleManager.getModuleByName(moduleName);
        if (module == null) {
            Command.sendMessage("Unknown module '" + null + "'!");
            return;
        }
        if (rkey == null) {
            Command.sendMessage(module.getName() + " is bound to " + ChatFormatting.GRAY + module.getBind().toString());
            return;
        }
        int key = Keyboard.getKeyIndex(rkey.toUpperCase());
        if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        if (key == 0) {
            Command.sendMessage("Unknown key '" + rkey + "'!");
            return;
        }
        module.bind.setValue(new Bind(key));
        Command.sendMessage("Bind for " + ChatFormatting.GREEN + module.getName() + ChatFormatting.WHITE + " set to " + ChatFormatting.GRAY + rkey.toUpperCase());
    }
}
