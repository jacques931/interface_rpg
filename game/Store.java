package rpg.game;

import rpg.Setting;
import rpg.items.*;
import rpg.items.equipment.*;
import rpg.personnage.Player;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Store {
    private Position position;
    private Image storeImage;
    private int storeWidth;
    private int storeHeight;
    private static final int INTERACTION_RANGE = 0; // Distance pour interagir avec le magasin
    private ArrayList<Item> items;

    public Store(Position position, int width, int height) {
        this.position = position;
        this.storeWidth = width;
        this.storeHeight = height;
        initStore();
        loadImage();
    }

    public Position getPosition() {
        return position;
    }
    public ArrayList<Item> getItems(){
        return this.items;
    }

    private void initStore(){
        this.items = new ArrayList<>();
        this.items.add(new Axe());
        this.items.add(new Sword());
        this.items.add(new Bow());
        this.items.add(new Dagger());
        this.items.add(new Stick());
        this.items.add(new Potion("Life potion", "src/main/java/rpg/asset/item/life_potion.png",100,"health",65));
        this.items.add(new Potion("Mana potion","src/main/java/rpg/asset/item/mana_potion.png",100,"mana",50));
    }

    // Vérifie si le joueur est à portée pour interagir avec le magasin
    public boolean isPlayerNearby(Player player) {
        Position playerPosition = player.getPosition();
        int playerWidth = Setting.playerWidth;
        int playerHeight = Setting.playerHeight;

        // Créer des rectangles pour le magasin et le joueur
        Rectangle storeRect = new Rectangle((int) position.getX(), (int) position.getY(), storeWidth, storeHeight);
        Rectangle playerRect = new Rectangle((int) playerPosition.getX(), (int) playerPosition.getY(), playerWidth, playerHeight);

        // Vérifie si les rectangles se superposent (collisions)
        return storeRect.intersects(playerRect);
    }

    private void loadImage() {
        storeImage = new ImageIcon("src/main/java/rpg/asset/store.png").getImage();
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(storeImage, (int) (position.getX() + offsetX), (int) (position.getY() + offsetY), storeWidth, storeHeight, null);
    }

}
