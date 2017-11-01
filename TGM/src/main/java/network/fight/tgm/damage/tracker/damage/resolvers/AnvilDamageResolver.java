package deterno.fight.tgm.damage.tracker.damage.resolvers;

import deterno.fight.tgm.damage.tracker.DamageInfo;
import deterno.fight.tgm.damage.tracker.DamageResolver;
import deterno.fight.tgm.damage.tracker.Lifetime;
import deterno.fight.tgm.damage.tracker.damage.AnvilDamageInfo;
import deterno.fight.tgm.damage.tracker.trackers.AnvilTracker;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AnvilDamageResolver implements DamageResolver {
    private final AnvilTracker anvilTracker;

    public AnvilDamageResolver(AnvilTracker anvilTracker) {
        this.anvilTracker = anvilTracker;
    }

    public DamageInfo resolve(LivingEntity entity, Lifetime lifetime, EntityDamageEvent damageEvent) {
        if(damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) damageEvent;

            if(event.getDamager() instanceof FallingBlock) {
                FallingBlock anvil = (FallingBlock) event.getDamager();
                OfflinePlayer offlineOwner = this.anvilTracker.getOwner(anvil);
                Player onlineOwner = null;

                if(offlineOwner != null) onlineOwner = offlineOwner.getPlayer();

                return new AnvilDamageInfo(anvil, onlineOwner, offlineOwner);
            }
        }

        return null;
    }

}
