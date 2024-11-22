package rpg;

import rpg.game.GameManager;
import rpg.game.Map;
import rpg.game.MonsterManager;
import rpg.panel.CharacterSelectionListener;
import rpg.panel.CharacterSelectionPanel;
import rpg.panel.EndGamePanel;
import rpg.personnage.*;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class Main extends JFrame implements CharacterSelectionListener {
    private GameManager gameManager;
    private Player player;
    private Map map;
    private MonsterManager monsterManager;
    private Timer gameTimer;
    private CharacterSelectionPanel selectionPanel;
    private Set<Integer> pressedKeys = new HashSet<>();
    private boolean eKeyUsed = false;
    private boolean iKeyUsed = false;
    private long startTime;

    public Main() {
        // Initialisation du panneau de sélection de personnage
        selectionPanel = new CharacterSelectionPanel(this);

        setTitle("RPG Game");
        setSize(Setting.screenWidth, Setting.screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(selectionPanel); // Affiche le panneau de sélection
    }

    @Override
    public void onGameStart(int characterId, String playerName) {
        int mapSize = 80;
        Position initialPosition = new Position((float) mapSize /2*Setting.tileSize, (float) mapSize /2*Setting.tileSize - 100);
        this.startTime = System.currentTimeMillis();
        // Crée la class du joueur qu'il a choisie
        if (characterId == 1) {
            player = new Archer(playerName, initialPosition);
        } else if (characterId == 2) {
            player = new Assassin(playerName, initialPosition);
        }
        else if (characterId == 3) {
            player = new Berserker(playerName, initialPosition);
        }
        else if (characterId == 4) {
            player = new Mage(playerName, initialPosition);
        }
        else if (characterId == 5) {
            player = new Paladin(playerName, initialPosition);
        }

        // Initialiser le reste du jeu
        map = new Map(mapSize, Setting.tileSize);
        monsterManager = new MonsterManager(player, map);

        gameManager = new GameManager(player, map, monsterManager, Setting.screenWidth, Setting.screenHeight);

        // Remplacer le panneau de sélection par le panneau de jeu
        remove(selectionPanel);
        add(gameManager);
        revalidate();
        repaint();

        // Configurer et démarrer la boucle de jeu
        gameTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleKeyActions();
                monsterManager.update();
                if(player.isDead()){
                    endGame(false);
                }
                repaint();
            }
        });
        gameTimer.start();

        // Ajoute les actions possible du jeu
        gameManager.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    eKeyUsed = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_I) {
                    iKeyUsed = false;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}
        });
        gameManager.requestFocusInWindow();
        player.setGameManager(gameManager);
    }


    public void endGame(boolean success) {
        remove(gameManager);
        add(new EndGamePanel(player, startTime, success));
        revalidate();
        repaint();
    }

    private void handleKeyActions() {
        /* Debut Action de mouvement */
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            player.move(0, 0, -1, map, monsterManager);
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            player.move(2, 0, 1, map, monsterManager);
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            player.move(3, -1, 0, map, monsterManager);
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            player.move(1, 1, 0, map, monsterManager);
        }
        /* Fin Action de mouvement */
        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            player.attak(monsterManager);
        }

        // Ouvre la boutique si il est dans le perimetre
        if (pressedKeys.contains(KeyEvent.VK_E) && gameManager.getStore().isPlayerNearby(player) && !eKeyUsed) {
            gameManager.toggleStorePanel();
            gameManager.openInventoryPanel();
            eKeyUsed = true;
        }
        // Fini le jeu si il est arrive a la fin
        if (pressedKeys.contains(KeyEvent.VK_E) && gameManager.getEndGameTrigger().isPlayerNearby(player)) {
            endGame(true);
        }

        // Ouvre et ferme l'inventaire
        if (pressedKeys.contains(KeyEvent.VK_I) && !iKeyUsed) {
            gameManager.toggleInventoryPanel();
            iKeyUsed = true;
        }

        // Ferme le store panel et inventaire si on quitte la zone de la boutique
        if(!gameManager.getStore().isPlayerNearby(player) && gameManager.getStoreVisible()){
            gameManager.closeInventoryPanel();
            gameManager.closeStorePanel();
        }
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.setVisible(true);
    }
}
