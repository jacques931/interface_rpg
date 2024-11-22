package rpg.personnage;
import rpg.utils.Position;

public class Assassin extends Player {
    public static final double START_HEALTH = 140;
    public static final double START_MANA = 20;
    public static final double PLAYER_DAMAGE = 20;
    public static final double COOLDOWN = 1.5;
    public static final int ATTACK_RANGE = 50;
    public static final float SPEED = 8.0f;

    public Assassin(Position position) {
        super(position, START_HEALTH, START_MANA, PLAYER_DAMAGE, COOLDOWN, ATTACK_RANGE, SPEED);
    }

    @Override
    public String getImageSrc() {
        return "src/main/java/rpg/asset/assassin/assassin";
    }

}
