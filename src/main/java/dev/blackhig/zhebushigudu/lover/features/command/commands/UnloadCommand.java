package dev.blackhig.zhebushigudu.lover.features.command.commands;

import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class UnloadCommand extends Command
{
    public UnloadCommand() {
        super("unload", new String[0]);
    }
    
    @Override
    public void execute(final String[] commands) {
        lover.unload(true);
    }
}
