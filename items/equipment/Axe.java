package rpg.items.equipment;

public class Axe extends Weapon {
    private static final double DAMAGE = 15;
    private static final int PRICE = 900;
    public static final String NAME = "Axe";
    public static final double USE_MANA = 0;
    public static final String IMAGE_SRC = "src/main/java/rpg/asset/item/axe.png";
    public static final double DAMAGE_SUP = 30;
    public static final String CLASS_NAME_DAMAGE_SUP = "Berserker";

    public Axe() {
        super(IMAGE_SRC, DAMAGE, PRICE, NAME, USE_MANA, DAMAGE_SUP, CLASS_NAME_DAMAGE_SUP);
    }
}
