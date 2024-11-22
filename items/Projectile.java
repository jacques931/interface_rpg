package rpg.items;

import rpg.Setting;
import rpg.utils.Position;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Projectile {
    private Position position;
    private int direction; // 0: haut, 1: droite, 2: bas, 3: gauche
    private double speed;
    private double damage;
    private long launchTime;
    private long lifeSpan;
    private Image projectileImage;
    private boolean isActive;

    private int width;
    private int height;

    public Projectile(double speed, long lifeSpan, String imagePath, int width, int height) {
        this.speed = speed;
        this.lifeSpan = lifeSpan * 1000;
        this.launchTime = System.currentTimeMillis();
        this.isActive = true;
        this.width = width;
        this.height = height;
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        this.projectileImage = new ImageIcon(imagePath).getImage();
    }

    // Configure le projectile avec sa position de départ, sa direction et ses dégâts
    public void setProjectile(Position position, int direction, double damage) {
        // Calcule la position initiale pour centrer le projectile par rapport au joueur
        switch (direction) {
            case 1 -> // Droite
                    this.position = new Position(position.getX() + Setting.playerWidth, position.getY() + (Setting.playerHeight - height) / 2);
            case 3 -> // Gauche
                    this.position = new Position(position.getX() - width, position.getY() + (Setting.playerHeight - height) / 2);
            default -> this.position = position;
        }

        this.direction = direction;
        this.damage = damage;
        this.isActive = true;
    }


    // Met à jour la position du projectile et vérifie s'il est encore actif
    public void update() {
        // Vérifie si la durée de vie est écoulée
        if (System.currentTimeMillis() - launchTime > lifeSpan) {
            isActive = false;
            return;
        }

        // Déplace le projectile en fonction de sa direction
        switch (direction) {
            case 0 -> position.setY((float) (position.getY() - speed)); // Haut
            case 1 -> position.setX((float) (position.getX() + speed)); // Droite
            case 2 -> position.setY((float) (position.getY() + speed)); // Bas
            case 3 -> position.setX((float) (position.getX() - speed)); // Gauche
        }
    }

    // Dessine le projectile avec une image, ou en rouge par défaut s'il n'y a pas d'image
    public void draw(Graphics g, int offsetX, int offsetY) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();

        // Calculer la position de dessin ajustée avec l'offset
        int x = (int) (position.getX() + offsetX);
        int y = (int) (position.getY() + offsetY);

        // Calculer l'angle de rotation en fonction de la direction
        double angle = 0;
        switch (direction) {
            case 0 -> angle = Math.toRadians(-90); // Haut
            case 1 -> angle = 0;                   // Droite (par défaut)
            case 2 -> angle = Math.toRadians(90);  // Bas
            case 3 -> angle = Math.toRadians(180); // Gauche
        }

        // Appliquer la transformation de rotation centrée
        g2d.rotate(angle, x + width / 2.0, y + height / 2.0);
        g2d.drawImage(projectileImage, x, y, width, height, null);

        // Réinitialiser la transformation
        g2d.setTransform(originalTransform);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) position.getX(), (int) position.getY(), width, height);
    }

    public boolean isActive() {
        return isActive;
    }

    public Position getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public double getDamage() {
        return damage;
    }
}
