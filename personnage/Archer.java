package rpg.personnage;

import rpg.utils.Position;

public class Archer extends Player {
    public static final double START_HEALTH = 120;
    public static final double START_MANA = 50;
    public static final double PLAYER_DAMAGE = 20;
    public static final double COOLDOWN = 1.5;
    public static final int ATTACK_RANGE = 60;
    public static final float SPEED = 7.0f;

    public Archer(String name,Position position) {
        super(name ,position, START_HEALTH, START_MANA, PLAYER_DAMAGE, COOLDOWN, ATTACK_RANGE, SPEED);
    }

    @Override
    public String getImageSrc() {
        return "src/main/java/rpg/asset/archer/archer";
    }
}
