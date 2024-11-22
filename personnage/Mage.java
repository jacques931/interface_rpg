package rpg.personnage;
import rpg.utils.Position;

public class Mage extends Player {
    public static final double START_HEALTH = 120;
    public static final double START_MANA = 200;
    public static final double PLAYER_DAMAGE = 20;
    public static final double COOLDOWN = 3;
    public static final int ATTACK_RANGE = 60;
    public static final float SPEED = 6.0f;

    public Mage(String name,Position position) {
        super(name ,position, START_HEALTH, START_MANA, PLAYER_DAMAGE, COOLDOWN, ATTACK_RANGE, SPEED);
    }

    @Override
    public String getImageSrc() {
        return "src/main/java/rpg/asset/mage/mage";
    }

}
