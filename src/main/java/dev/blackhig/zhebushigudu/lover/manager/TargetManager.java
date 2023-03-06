package dev.blackhig.zhebushigudu.lover.manager;

import net.minecraft.entity.EntityLivingBase;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.Globals;

public class TargetManager implements Globals
{
    private EntityLivingBase currentTarget;
    
    public TargetManager() {
        this.currentTarget = null;
    }
    
    public void updateTarget(final EntityLivingBase targetIn) {
        this.currentTarget = targetIn;
    }
    
    public EntityLivingBase getTarget() {
        return this.currentTarget;
    }
}
