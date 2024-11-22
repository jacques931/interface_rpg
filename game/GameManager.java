package rpg.game;

import rpg.items.Projectile;
import rpg.panel.InventoryPanel;
import rpg.panel.PlayerStatsPanel;
import rpg.panel.StorePanel;
import rpg.personnage.Player;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GameManager extends JPanel {
    private Player player;
    private Map map;
    private MonsterManager monsterManager;

    private int viewWidth;
    private int viewHeight;
    //Panel
    private StorePanel storePanel;
    private InventoryPanel inventoryPanel;
    private PlayerStatsPanel playerStatsPanel;
    //Objet
    private Store store;
    private EndGameTrigger endGameTrigger;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public GameManager(Player player, Map map, MonsterManager monsterManager, int viewWidth, int viewHeight) {
        this.player = player;
        this.map = map;
        this.monsterManager = monsterManager;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        setLayout(null);
        setPreferredSize(new Dimension(viewWidth, viewHeight));

        // Crée la boutique
        store = new Store(new Position(1600, 1700), 150, 150);
        // Crée la porte d sortie
        this.endGameTrigger = new EndGameTrigger(new Position(3670, 200), 50, 50);
        //Crée le panel pour la boutique
        storePanel = new StorePanel(store, this, player);
        storePanel.setBounds(100, 150, 300, 400);
        storePanel.setVisible(false);
        add(storePanel);

        // Crée le panel pour l'inventaire
        inventoryPanel = new InventoryPanel(this, player); // Passe GamePanel au StorePanel pour la gestion du focus
        inventoryPanel.setBounds(1200, 150, 300, 400);
        inventoryPanel.setVisible(false);
        add(inventoryPanel);

        // Crée le panel pour les stastiques du joueur
        int playerStatsPanelWidth = 250;
        int playerStatsPanelHeight = 130;
        playerStatsPanel = new PlayerStatsPanel(player, playerStatsPanelWidth, playerStatsPanelHeight);
        playerStatsPanel.setBounds(10, 10, playerStatsPanelWidth, playerStatsPanelHeight); // Positionner le panneau dans le GamePanel
        add(playerStatsPanel);
    }

    public void toggleStorePanel() {
        storePanel.setVisible(!storePanel.isVisible());
    }

    public void toggleInventoryPanel(){
        inventoryPanel.setVisible(!inventoryPanel.isVisible());
    }

    public void closeStorePanel() {
        storePanel.setVisible(false);
    }

    public void openInventoryPanel() {
        inventoryPanel.setVisible(true);
    }

    public void closeInventoryPanel(){
        inventoryPanel.setVisible(false);
    }

    public boolean getStoreVisible(){
        return storePanel.isVisible();
    }

    public Store getStore() {
        return store;
    }

    public void updatePlayerStats() {
        playerStatsPanel.refreshStats();
    }

    public void updateInventory() {
        inventoryPanel.refreshPanel();
    }

    public void destroyMonster(Monster monster){
        monsterManager.destroyMonster(monster);
    }

    public EndGameTrigger getEndGameTrigger(){
        return this.endGameTrigger;
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    // Mettre a jour la position des projectiles et les supprimer apres le temps impartie
    public void updateProjectiles() {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update(); // Met à jour la position du projectile

            // Supprimer le projectile si il a depasser le temps impartie
            if(!projectile.isActive()){
                iterator.remove();
                continue;
            }

            // Vérifie si le projectile est encore dans une zone marchable
            Rectangle projectileRect = projectile.getBounds();
            if (!map.isWalkable(projectile.getPosition(), projectile.getDirection(), projectileRect.width, projectileRect.height)) {
                // Retire le projectile s'il touche une zone non marchable
                iterator.remove();
                continue;
            }

            // Vérifie les collisions avec les monstres
            Monster targetMonster = monsterManager.getMonsterByPosition(projectile.getPosition(), projectile.getDirection(), 20);
            if (targetMonster != null && targetMonster.getBounds().intersects(projectileRect)) {
                if(!targetMonster.hit(projectile.getDamage())){
                    player.destroyMonster(targetMonster);
                }
                // Retire le projectile après impact
                iterator.remove();
            }
        }
    }

    // Méthode pour dessiner tous les projectiles
    public void drawProjectiles(Graphics g, int offsetX, int offsetY) {
        for (Projectile projectile : projectiles) {
            projectile.draw(g, offsetX, offsetY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateProjectiles();
        // Calcul du décalage pour centrer la caméra sur le joueur
        int offsetX = (viewWidth / 2) - (int) player.getPosition().getX();
        int offsetY = (viewHeight / 2) - (int) player.getPosition().getY();

        // Empêcher la caméra de sortir des limites de la carte
        offsetX = Math.max(offsetX, (int) (viewWidth - map.getWidth()));
        offsetX = Math.min(offsetX, 0);
        offsetY = Math.max(offsetY, viewHeight - map.getHeight());
        offsetY = Math.min(offsetY, 0);

        // Dessiner la carte et le joueur avec le décalage
        this.map.draw(g, offsetX, offsetY);
        this.monsterManager.draw(g, offsetX, offsetY);
        this.store.draw(g, offsetX, offsetY);
        this.endGameTrigger.draw(g, offsetX, offsetY);
        this.drawProjectiles(g, offsetX, offsetY);
        this.player.draw(g, offsetX, offsetY);

        // Affiche le texte si le joueur est à proximité du magasin
        if (store.isPlayerNearby(player) || endGameTrigger.isPlayerNearby(player)) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String message = "'E' to interact";
            int textWidth = g.getFontMetrics().stringWidth(message);
            int textHeight = g.getFontMetrics().getHeight();
            int padding = 5;

            int textX = (int) player.getPosition().getX() + offsetX - textWidth / 2;
            int textY = (int) player.getPosition().getY() + offsetY - 20;

            // Dessiner le fond rectangulaire derrière le texte
            g.setColor(new Color(50, 50, 50, 180));
            g.fillRect(textX - padding, textY - textHeight + padding, textWidth + 2 * padding, textHeight);

            g.setColor(Color.WHITE);
            g.drawString(message, textX, textY);
        }
    }
}
