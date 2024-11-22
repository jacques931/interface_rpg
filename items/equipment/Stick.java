package rpg.items.equipment;

import rpg.items.Projectile;

public class Stick extends RemoteWeapon {
    private static final double DAMAGE = 15;
    private static final int PRICE = 1500;
    public static final String NAME = "Stick";
    public static final double USE_MANA = 50;
    public static final String IMAGE_SRC = "src/main/java/rpg/asset/item/stick.png";
    public static final double DAMAGE_SUP = 120;
    public static final String CLASS_NAME_DAMAGE_SUP = "Mage";
    public static final double speed = 12;
    public static final long lifeSpan = 2;

    public Stick() {
        super(IMAGE_SRC, DAMAGE, PRICE, NAME, USE_MANA, DAMAGE_SUP, CLASS_NAME_DAMAGE_SUP);
    }

    @Override
    public Projectile getProjectile() {
        return new Projectile(speed, lifeSpan, "src/main/java/rpg/asset/fireball.png",50,30);
    }
}
