package rpg.game;

import rpg.Setting;
import rpg.personnage.Player;
import rpg.utils.Position;

import java.awt.*;
import java.util.ArrayList;

public class MonsterManager {
    private ArrayList<Monster> monsters;
    private Player player;
    private Map map;

    public MonsterManager(Player player, Map map){
        this.monsters = new ArrayList<>();
        this.player = player;
        this.map = map;
        initializeMonsters();
    }

    private void initializeMonsters() {
        // Création des positions fixes de chaque monstre
        Position[] monsterPositions = {
                new Position(200, 300), new Position(400, 500), new Position(600, 200), new Position(800, 400),
                new Position(500, 700), new Position(1300, 500), new Position(1700, 450), new Position(2000, 700),
                new Position(2800, 650), new Position(1800, 850), new Position(1950, 1300), new Position(2050, 2400),
                new Position(2500, 1900), new Position(2900, 1900), new Position(3150, 1600), new Position(3700, 1600),
                new Position(3150, 2150), new Position(3700, 2150), new Position(3450, 2500), new Position(3450, 2900),
                new Position(3000, 2600), new Position(2650, 2700), new Position(3700, 3200), new Position(3600, 3600),
                new Position(3400, 3400), new Position(2700, 3350), new Position(2200, 3350), new Position(1700, 3150),
                new Position(1650, 3650), new Position(1900, 3400), new Position(2100, 3650), new Position(2050, 2850),
                new Position(1000, 2000), new Position(600, 1500), new Position(1000, 2600), new Position(1000, 3100),
                new Position(300, 2000), new Position(300, 2400), new Position(300, 2800), new Position(250, 3200),
                new Position(200, 3500), new Position(300, 3650), new Position(3620, 300), new Position(3700, 280),
                new Position(3650, 450), new Position(3750, 500), new Position(3690, 600),
        };

        // Création des monstres aux positions spécifiées
        for (Position pos : monsterPositions) {
            monsters.add(new Monster(pos, Setting.monsterSize, Setting.monsterSize, map));
        }
    }

    public void draw(Graphics g, int offsetX, int offsetY){
        for (Monster monster : this.monsters){
            monster.draw(g, offsetX, offsetY);
        }
    }

    public void update(){
        for (Monster monster : this.monsters){
            monster.update(player);
        }
    }

    public void destroyMonster(Monster monster){
        this.monsters.remove(monster);
    }

    // Fonction pour obtenir un monstre à portée et dans la direction du joueur
    public Monster getMonsterByPosition(Position playerPosition, int lastMouvement, float attackRange) {
        for (Monster monster : monsters) {
            Position monsterPosition = monster.getPosition();
            double distance = playerPosition.distanceTo(monsterPosition);

            // Vérifier si le monstre est à portée
            if (distance <= attackRange && isMonsterInDirection(playerPosition, monsterPosition, lastMouvement)) {
                return monster;
            }
        }
        return null;
    }

    // Vérifie si le monstre est dans la direction de l'attaque
    private boolean isMonsterInDirection(Position playerPosition, Position monsterPosition, int lastMouvement) {
        float dx = monsterPosition.getX() - playerPosition.getX();
        float dy = monsterPosition.getY() - playerPosition.getY();

        return switch (lastMouvement) {
            case 0 -> // Haut
                    dy < 0 && Math.abs(dy) >= Math.abs(dx);
            case 1 -> // Droite
                    dx > 0 && Math.abs(dx) >= Math.abs(dy);
            case 2 -> // Bas
                    dy > 0 && Math.abs(dy) >= Math.abs(dx);
            case 3 -> // Gauche
                    dx < 0 && Math.abs(dx) >= Math.abs(dy);
            default -> false;
        };
    }

    // Verifie si un monstre touche le joueur
    public boolean isPlayerCollidingWithMonster(Position playerPosition, int playerWidth, int playerHeight) {
        for (Monster monster : monsters) {
            if (monster.intersects(playerPosition, playerWidth, playerHeight)) {
                return true;
            }
        }
        return false;
    }
}
