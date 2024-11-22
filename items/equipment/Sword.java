package rpg.items.equipment;

public class Sword extends Weapon {
    private static final double DAMAGE = 30;
    private static final int PRICE = 1200;
    public static final String NAME = "Sword";
    public static final double USE_MANA = 0;
    public static final String IMAGE_SRC = "src/main/java/rpg/asset/item/sword.png";
    public static final double DAMAGE_SUP = 15;
    public static final String CLASS_NAME_DAMAGE_SUP = "Paladin";

    public Sword() {
        super(IMAGE_SRC, DAMAGE, PRICE, NAME, USE_MANA, DAMAGE_SUP, CLASS_NAME_DAMAGE_SUP);
    }
}
