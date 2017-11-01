package deterno.fight.tgm.damage.tracker.damage.resolvers;

import com.google.common.base.Preconditions;
import deterno.fight.tgm.damage.tracker.DamageInfo;
import deterno.fight.tgm.damage.tracker.DamageResolver;
import deterno.fight.tgm.damage.tracker.Lifetime;
import deterno.fight.tgm.damage.tracker.damage.GravityDamageInfo;
import deterno.fight.tgm.damage.tracker.trackers.base.gravity.Fall;
import deterno.fight.tgm.damage.tracker.trackers.base.gravity.SimpleGravityKillTracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GravityDamageResolver implements DamageResolver {
    public GravityDamageResolver(@Nonnull SimpleGravityKillTracker tracker) {
        Preconditions.checkNotNull(tracker, "tracker");
        this.tracker = tracker;
    }

    public @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        if(!(entity instanceof Player)) return null;
        Player victim = (Player) entity;
        Fall fall = this.tracker.getCausingFall(victim, damageEvent.getCause());
        if(fall != null) {
            return new GravityDamageInfo(fall.attacker, fall.cause, fall.from);
        } else {
            return null;
        }
    }

    private final @Nonnull
    SimpleGravityKillTracker tracker;
}
