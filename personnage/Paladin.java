package rpg.personnage;
import rpg.utils.Position;

public class Paladin extends Player {
    public static final double START_HEALTH = 180;
    public static final double START_MANA = 25;
    public static final double PLAYER_DAMAGE = 25;
    public static final double COOLDOWN = 2;
    public static final int ATTACK_RANGE = 50;
    public static final float SPEED = 6.5f;

    public Paladin(String name,Position position) {
        super(name ,position, START_HEALTH, START_MANA, PLAYER_DAMAGE, COOLDOWN, ATTACK_RANGE, SPEED);
    }

    @Override
    public String getImageSrc() {
        return "src/main/java/rpg/asset/paladin/paladin";
    }

}
