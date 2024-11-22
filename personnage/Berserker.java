package rpg.personnage;
import rpg.utils.Position;

public class Berserker extends Player {
    public static final double START_HEALTH = 220;
    public static final double START_MANA = 15;
    public static final double PLAYER_DAMAGE = 30;
    public static final double COOLDOWN = 1.5;
    public static final int ATTACK_RANGE = 50;
    public static final float SPEED = 6.5f;

    public Berserker(String name,Position position) {
        super(name ,position, START_HEALTH, START_MANA, PLAYER_DAMAGE, COOLDOWN, ATTACK_RANGE, SPEED);
    }

    @Override
    public String getImageSrc() {
        return "src/main/java/rpg/asset/berserker/berserker";
    }

}
