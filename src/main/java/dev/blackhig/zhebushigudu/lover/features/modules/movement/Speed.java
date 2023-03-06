package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Speed extends Module
{
    public Speed() {
        super("Speed", "Speed.", Category.MOVEMENT, true, false, false);
    }
    
    @Override
    public String getDisplayInfo() {
        return "Strafe";
    }
}
