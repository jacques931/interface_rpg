package rpg.game;

import rpg.Setting;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Map {
    private int[][] mapTiles;
    private int tileSize;
    private int width;
    private int height;
    private HashMap<Integer, Image> tileImages;

    public Map(int size, int tileSize) {
        this.tileSize = tileSize;
        this.width = size * tileSize;
        this.height = size * tileSize;
        generateMapTiles(size);
        loadTileImages();
    }

    // Méthode pour vérifier si une tuile est interdite
    private boolean isForbiddenTile(int tile) {
        return tile == 0;
    }

    public boolean isWalkable(Position position, int mouvement, int playerWidth, int playerHeight) {
        // Calcul de tous les coins de la position du joueur
        int xTileLeft = (int) position.getX() / tileSize;
        int xTileRight = (int) (position.getX() + playerWidth) / tileSize;
        int yTileTop = (int) (position.getY()) / tileSize;
        int yTileBottom = (int) (position.getY() + playerHeight) / tileSize;

        // Vérifie si le mouvement est possible dans la direction choisie
        return switch (mouvement) {
            case 0 -> // Vers le haut
                    !isForbiddenTile(mapTiles[yTileTop][xTileLeft]) && !isForbiddenTile(mapTiles[yTileTop][xTileRight]);
            case 1 -> // Vers la droite
                    !isForbiddenTile(mapTiles[yTileTop][xTileRight]) && !isForbiddenTile(mapTiles[yTileBottom][xTileRight]);
            case 2 -> // Vers le bas
                    !isForbiddenTile(mapTiles[yTileBottom][xTileLeft]) && !isForbiddenTile(mapTiles[yTileBottom][xTileRight]);
            case 3 -> // Vers la gauche
                    !isForbiddenTile(mapTiles[yTileTop][xTileLeft]) && !isForbiddenTile(mapTiles[yTileBottom][xTileLeft]);
            default -> false; // Mouvement non valide
        };
    }

    // Génère dynamiquement la matrice de tuiles en fonction de la taille spécifiée
    private void generateMapTiles(int size) {
        mapTiles = new int[size][size];

        // Remplie la carte
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mapTiles[i][j] = 0;
            }
        }

        // Genere la map d'apres une taille de 80 (n'est pas responsive)
        int centre = size / 2;
        int grandSalle = size / 5;
        int moyenneSalle = size / 6;
        int petitSalle = size / 10;

        // Placer la grande salle centrale
        int halfGrandSalle = grandSalle / 2;
        createRoom(centre - halfGrandSalle, centre - halfGrandSalle, grandSalle, grandSalle); //Centre

        createRoom(centre/9, centre/10, moyenneSalle, moyenneSalle); // Gauche Haut
        createRoom(centre/5, centre - halfGrandSalle, moyenneSalle, moyenneSalle); // Centre Haut
        createRoom(centre/10, (int) (size*0.9), petitSalle/2, petitSalle); // Droitee Haut
        createRoom(size-petitSalle-3, 4, petitSalle/2, petitSalle); // Gauche Bas
        createRoom(centre - halfGrandSalle, centre + 22, moyenneSalle, moyenneSalle);
        createRoom(size-moyenneSalle-5, centre - halfGrandSalle, moyenneSalle, moyenneSalle);
        createRoom(size-moyenneSalle-5, size-moyenneSalle-3, moyenneSalle, moyenneSalle);

        createCorridor(centre/3,centre + 5,13,true);
        createCorridor(centre/4,moyenneSalle+4,15,true);
        createCorridor(3,centre,5,false);
        createCorridor(moyenneSalle + 7,centre - 2,12,false);
        createCorridor(centre,5,27,true);
        createCorridor(centre -10,19,35,false);
        createCorridor(centre -23,10,13,false);
        createCorridor(centre -10,centre-30,9,true);
        createCorridor(10,size-7,22,false);
        createCorridor(centre -2,centre+halfGrandSalle,14,true);
        createCorridor(centre+2,5,27,false);
        createCorridor(centre + halfGrandSalle,centre,14,false);
        createCorridor(size - moyenneSalle,centre + halfGrandSalle-3,19,true);
        createCorridor(size - moyenneSalle -15,centre + halfGrandSalle+5,16,true);
        createCorridor(centre + halfGrandSalle -3,size-12,17,false);
        createCorridor(centre + halfGrandSalle + 4,centre+12,15,false);
    }

    // Méthode pour créer une salle rectangulaire de taille spécifique à une position fixe
    private void createRoom(int startY, int startX, int width, int height) {
        for (int i = startY; i < startY + height; i++) {
            for (int j = startX; j < startX + width; j++) {
                if (i >= 0 && i < mapTiles.length && j >= 0 && j < mapTiles[0].length) {
                    mapTiles[i][j] = 1; // Place la salle avec le type de tuile donné (vide)
                }
            }
        }
    }

    // Méthode pour créer un couloir de deux cases de largeur entre deux salles
    private void createCorridor(int startY, int startX, int length, boolean horizontal) {
        if (horizontal) {
            for (int j = startX; j < startX + length && j < mapTiles[0].length - 1; j++) {
                mapTiles[startY][j] = 1;
                if (startY + 1 < mapTiles.length) {
                    mapTiles[startY + 1][j] = 1;
                }
            }
        } else {
            for (int i = startY; i < startY + length && i < mapTiles.length - 1; i++) {
                mapTiles[i][startX] = 1;
                if (startX + 1 < mapTiles[0].length) {
                    mapTiles[i][startX + 1] = 1;
                }
            }
        }
    }

    private void loadTileImages() {
        tileImages = new HashMap<>();

        // Charger les images de chaque type de
        tileImages.put(0, new ImageIcon("src/main/java/rpg/asset/black.jpg").getImage());   // Vide
        tileImages.put(1, new ImageIcon("src/main/java/rpg/asset/road.png").getImage());        // Route
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        for (int row = 0; row < mapTiles.length; row++) {
            for (int col = 0; col < mapTiles[row].length; col++) {
                int tileType = mapTiles[row][col];
                Image tileImage = tileImages.get(tileType);

                if (tileImage != null) {
                    int x = col * tileSize + offsetX;
                    int y = row * tileSize + offsetY;

                    g.drawImage(tileImage, x, y, tileSize, tileSize, null);
                }
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth(){
        return width;
    }
}
