package deterno.fight.tgm.damage.tracker.base;

import deterno.fight.tgm.damage.tracker.DamageInfo;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;


public abstract class AbstractDamageInfo implements DamageInfo {
    protected AbstractDamageInfo(@Nullable LivingEntity resolvedDamager) {
        this.resolvedDamager = resolvedDamager;
    }

    public @Nullable
    LivingEntity getResolvedDamager() {
        return this.resolvedDamager;
    }

    protected final @Nullable
    LivingEntity resolvedDamager;
}
