package rpg.items.equipment;

import rpg.items.Projectile;

public abstract class RemoteWeapon extends Weapon {

    public RemoteWeapon(String imageSrc, double damage, int price, String name, double useMana, double damageSup, String classNameDamageSup) {
        super(imageSrc, damage, price, name, useMana, damageSup, classNameDamageSup);
    }

    public abstract Projectile getProjectile();
}
