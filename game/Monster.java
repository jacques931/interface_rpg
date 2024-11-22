package rpg.game;

import rpg.Setting;
import rpg.personnage.Player;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Monster {
    private Position position;
    private Position originPosition;
    private Image monsterImage;
    private Map map;

    private int monsterWidth;
    private int monsterHeight;

    private Position targetPosition;
    private static final int BUFFER_SPACE = 10;

    private int pursueRange = 400;
    private int attackRange = 50;
    private int freezeTime = 2000;
    private long lastAttackTime = 0;
    //Stats
    private double health = 120;
    private double maxHealth;
    private int giveGold = 300;
    private double giveXp = 145;
    private double damage = 15;
    private double speed = 4.0f;

    public Monster(Position position, int width, int height, Map map) {
        this.position = position;
        this.originPosition = new Position(position.getX(),position.getY());
        this.monsterWidth = width;
        this.monsterHeight = height;
        this.maxHealth = this.health;
        this.map = map;
        loadImage();
    }

    // Met à jour la position du monstre en fonction de la position du joueur
    public void update(Player player) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastAttackTime < freezeTime){
            return;
        }

        // Calcule la position centrale du joueur
        Position playerPosition = player.getPosition();
        Position playerCenterPosition = new Position(
                playerPosition.getX() + Setting.playerWidth / 2.0f - monsterWidth / 2.0f,
                playerPosition.getY() + Setting.playerHeight / 2.0f - monsterHeight / 2.0f
        );

        // Verifie si le joueur est dans la zone d'attaque
        if (isPlayerInRange(playerCenterPosition, attackRange)) {
            // Si le joueur est dans la zone d'attaque, le monstre attaque et se fige
            if (currentTime - lastAttackTime >= freezeTime) {
                lastAttackTime = currentTime;
                player.hit(this.damage);
            }
        }
        // Verifie si le joueur est dans la zone de recherche
        else if (isPlayerInRange(playerCenterPosition, pursueRange)) {
            // Si le joueur est dans la zone de poursuite, le monstre le suit
            targetPosition = playerCenterPosition;
            moveTowards();
        }
        // Retourne et reste sur la position d'origin par defaut
        else {
            // Si le joueur est hors de la zone, le monstre retourne à sa position d'origine
            targetPosition = originPosition;
            moveTowards();
        }
    }

    // Déplace le monstre vers une position cible
    private void moveTowards() {
        int dx = (int) (targetPosition.getX() - position.getX());
        int dy = (int) (targetPosition.getY() - position.getY());
        // Essayer de se déplacer horizontalement si la case est marchable
        if (dx != 0 && map.isWalkable(new Position(position.getX() + Math.signum(dx) * BUFFER_SPACE, position.getY()), dx > 0 ? 1 : 3, monsterWidth, monsterHeight)) {
            position.setX((float) (position.getX() + Math.signum(dx) * speed));
        }

        // Essayer de se déplacer verticalement si la case est marchable
        if (dy != 0 && map.isWalkable(new Position(position.getX(), position.getY() + Math.signum(dy) * BUFFER_SPACE), dy > 0 ? 2 : 0, monsterWidth, monsterHeight)) {
            position.setY((float) (position.getY() + Math.signum(dy) * speed));
        }
    }

    public boolean intersects(Position playerPosition, int playerWidth, int playerHeight) {
        Rectangle playerRect = new Rectangle((int) playerPosition.getX(), (int) playerPosition.getY(), playerWidth, playerHeight);
        return this.getBounds().intersects(playerRect);
    }

    // Affiche le monstre dans l'interface graphique
    public void draw(Graphics g, int offsetX, int offsetY) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        int x = (int) (position.getX() + offsetX);
        int y = (int) (position.getY() + offsetY);

        if(targetPosition!=null){
            // Calcul de l'angle de rotation en fonction de la position cible
            double dx = targetPosition.getX() - position.getX();
            double dy = targetPosition.getY() - position.getY();
            double angle = Math.atan2(dy, dx); // Angle en radians pour une rotation continue

            // Appliquer la rotation et dessiner l'image du monstre
            g2d.rotate(angle, x + monsterWidth / 2.0, y + monsterHeight / 2.0);
            g2d.drawImage(monsterImage, x, y, monsterWidth, monsterHeight, null);

            g2d.setTransform(originalTransform); // Réinitialiser la transformation
        }

        // Dessiner la barre de vie
        int barWidth = monsterWidth + 10;
        int barHeight = 8;
        int healthBarX = x;
        int healthBarY = y - barHeight - 5;
        int filledWidth = (int) ((health / maxHealth) * barWidth);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(healthBarX, healthBarY, barWidth, barHeight);
        g.setColor(Color.RED);
        g.fillRect(healthBarX, healthBarY, filledWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(healthBarX, healthBarY, barWidth, barHeight);
    }

    // Vérifie si le joueur est dans la zone de poursuite
    public boolean isPlayerInRange(Position playerPosition, int range) {
        double distance = position.distanceTo(playerPosition);
        return distance <= range;
    }

    public boolean hit(double damage){
        this.health -= damage;
        return this.health>0;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) position.getX(), (int) position.getY(), monsterWidth, monsterHeight);
    }

    public int getGiveGold(){
        return this.giveGold;
    }

    public double getGiveXp(){
        return this.giveXp;
    }

    // Charge l'image du monstre
    private void loadImage() {
        monsterImage = new ImageIcon("src/main/java/rpg/asset/monster.png").getImage();
    }

    // Récupère la position du monstre
    public Position getPosition() {
        return position;
    }

}
