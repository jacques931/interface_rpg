package rpg.utils;

import javax.swing.*;
import java.awt.*;

public class Animation {
    // Directions indexées: 0 = haut, 1 = droite, 2 = bas, 3 = gauche
    private Image[][] frames;
    private int currentFrameIndex = 0;
    private int direction = 1;
    private long lastFrameTime = 0;
    private final long frameDelay;

    // Constructeur pour initialiser les frames et le délai
    public Animation(String basePath, long frameDelay) {
        // Prend que des animation de 4 direction et 4 frame
        this.frames = new Image[4][4];
        this.frameDelay = frameDelay;

        loadFrames(basePath);
    }

    // Charge les frames pour chaque direction (0 = haut, 1 = droite, 2 = bas, 3 = gauche)
    private void loadFrames(String basePath) {
        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < 4; frame++) {
                String imagePath = basePath + "_dir" + dir + "_frame" + frame + ".png";
                frames[dir][frame] = new ImageIcon(imagePath).getImage();
            }
        }
    }

    // Met à jour l'animation pour passer à la frame suivante
    public void update() {
        if (System.currentTimeMillis() - lastFrameTime >= frameDelay) {
            currentFrameIndex = (currentFrameIndex + 1) % frames[direction].length;
            lastFrameTime = System.currentTimeMillis();
        }
    }

    // Change la direction de l'animation
    public void setDirection(int newDirection) {
        if (newDirection >= 0 && newDirection < 4) {
            direction = newDirection;
            currentFrameIndex = 0; // Reset animation when direction changes
        }
    }

    // Obtient l'image actuelle de l'animation en fonction de la direction et de l'index de frame
    public Image getCurrentFrame() {
        return frames[direction][currentFrameIndex];
    }
}
