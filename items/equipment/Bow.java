package rpg.items.equipment;

import rpg.items.Projectile;

public class Bow extends RemoteWeapon {
    private static final double DAMAGE = 10;
    private static final int PRICE = 1450;
    public static final String NAME = "Bow";
    public static final double USE_MANA = 10;
    public static final String IMAGE_SRC = "src/main/java/rpg/asset/item/bow.png";
    public static final double DAMAGE_SUP = 50;
    public static final String CLASS_NAME_DAMAGE_SUP = "Archer";

    public static final double speed = 11;
    public static final long lifeSpan = 2;

    public Bow() {
        super(IMAGE_SRC, DAMAGE, PRICE, NAME, USE_MANA, DAMAGE_SUP, CLASS_NAME_DAMAGE_SUP);
    }

    @Override
    public Projectile getProjectile() {
        return new Projectile(speed, lifeSpan, "src/main/java/rpg/asset/arrow.png",50,20);
    }
}
