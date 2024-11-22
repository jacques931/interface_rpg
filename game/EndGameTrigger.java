package rpg.game;

import rpg.Setting;
import rpg.personnage.Player;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;

public class EndGameTrigger {
    private Position position;
    private Image triggerImage;
    private int triggerWidth;
    private int triggerHeight;

    public EndGameTrigger(Position position, int width, int height) {
        this.position = position;
        this.triggerWidth = width;
        this.triggerHeight = height;
        loadImage();
    }

    private void loadImage() {
        triggerImage = new ImageIcon("src/main/java/rpg/asset/door.png").getImage();
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(triggerImage, (int) (position.getX() + offsetX), (int) (position.getY() + offsetY), triggerWidth, triggerHeight, null);
    }

    public boolean isPlayerNearby(Player player) {
        Position playerPosition = player.getPosition();
        int playerWidth = Setting.playerWidth;
        int playerHeight = Setting.playerHeight;

        // Créer des rectangles pour le magasin et le joueur
        Rectangle storeRect = new Rectangle((int) position.getX(), (int) position.getY(), triggerWidth, triggerHeight);
        Rectangle playerRect = new Rectangle((int) playerPosition.getX(), (int) playerPosition.getY(), playerWidth, playerHeight);

        // Vérifie si les rectangles se superposent (collisions)
        return storeRect.intersects(playerRect);
    }

    public Position getPosition() {
        return position;
    }
}
