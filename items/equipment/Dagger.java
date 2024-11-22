package rpg.items.equipment;

public class Dagger extends Weapon {
    private static final double DAMAGE = 20;
    private static final int PRICE = 1250;
    public static final String NAME = "Dagger";
    public static final double USE_MANA = 0;
    public static final String IMAGE_SRC = "src/main/java/rpg/asset/item/dagger.png";
    public static final double DAMAGE_SUP = 40;
    public static final String CLASS_NAME_DAMAGE_SUP = "Assassin";

    public Dagger() {
        super(IMAGE_SRC, DAMAGE, PRICE, NAME, USE_MANA, DAMAGE_SUP, CLASS_NAME_DAMAGE_SUP);
    }
}
