package dev.blackhig.zhebushigudu.lover.features.command.commands;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.manager.FriendManager;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.command.Command;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    @Override
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (lover.friendManager.getFriends().isEmpty()) {
                Command.sendMessage("Friend list empty D:.");
            }
            else {
                String f = "Friends: ";
                for (final FriendManager.Friend friend : lover.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    }
                    catch (final Exception ex) {}
                }
                Command.sendMessage(f);
            }
            return;
        }
        if (commands.length != 2) {
            if (commands.length >= 2) {
                final String s = commands[0];
                switch (s) {
                    case "add": {
                        lover.friendManager.addFriend(commands[1]);
                        Command.sendMessage(ChatFormatting.GREEN + commands[1] + " has been friended");
                        return;
                    }
                    case "del": {
                        lover.friendManager.removeFriend(commands[1]);
                        Command.sendMessage(ChatFormatting.RED + commands[1] + " has been unfriended");
                        return;
                    }
                    default: {
                        Command.sendMessage("Unknown Command, try friend add/del (name)");
                        break;
                    }
                }
            }
            return;
        }
        final String s2 = commands[0];
        switch (s2) {
            case "reset": {
                lover.friendManager.onLoad();
                Command.sendMessage("Friends got reset.");
                return;
            }
            default: {
                Command.sendMessage(commands[0] + (lover.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            }
        }
    }
}
