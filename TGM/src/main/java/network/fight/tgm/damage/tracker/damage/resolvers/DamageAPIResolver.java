package deterno.fight.tgm.damage.tracker.damage.resolvers;

import deterno.fight.tgm.damage.tracker.DamageInfo;
import deterno.fight.tgm.damage.tracker.DamageResolver;
import deterno.fight.tgm.damage.tracker.DamageResolvers;
import deterno.fight.tgm.damage.tracker.Lifetime;
import deterno.fight.tgm.damage.tracker.plugin.DamageAPIHelper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Resolves the damage stored in the {@link DamageAPI}.
 *
 * When a plugin uses the API to inflict damage on an entity, it specifies its
 * own {@link Damage} object to use. However, plugins listening on the Bukkit
 * event will use the regular channels to fetch the object for the event.
 * Therefore, this resolver is necessary to feed the proper object to those
 * event listeners.
 *
 * By Deterno
 */
public class DamageAPIResolver implements DamageResolver {
    /** @see DamageResolvers */
    public @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime, @Nonnull EntityDamageEvent damageEvent) {
        return DamageAPIHelper.get().getEventDamageInfo(damageEvent);
    }

    static {
        DamageResolvers.getManager().register(new DamageAPIResolver());
    }
}
