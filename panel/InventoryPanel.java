package rpg.panel;

import rpg.game.GameManager;
import rpg.game.Inventory;
import rpg.items.Item;
import rpg.items.equipment.Weapon;
import rpg.personnage.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InventoryPanel extends JPanel {
    private static final int FIXED_WIDTH = 300;
    private static final int FIXED_HEIGHT = 400;
    private static final int SLOT_SIZE = 50; // Taille fixe de chaque slot
    private JPanel itemsPanel; // Panel qui contient les slots d'articles
    private Inventory inventory;
    private GameManager gameManager;
    private JLabel goldLabel;
    private Player player;

    public InventoryPanel(GameManager gameManager, Player player) {
        this.gameManager = gameManager;
        this.player = player;
        this.inventory = player.getInventory();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(FIXED_WIDTH, FIXED_HEIGHT));
        setBackground(Color.LIGHT_GRAY);

        // Initialisation de l'interface
        initTitlePanel();
        initItemsPanel();
        initGoldLabel();
    }

    // Initialise le panneau de titre avec le bouton de fermeture
    private void initTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(FIXED_WIDTH, 50));
        titlePanel.setBackground(Color.LIGHT_GRAY);

        JLabel titleLabel = new JLabel("Inventory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JButton closeButton = getjButton();

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(closeButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);
    }

    private JButton getjButton() {
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(Color.BLACK);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                gameManager.requestFocusInWindow();
            }
        });
        return closeButton;
    }

    // Initialise le panneau des items et le fait défiler si nécessaire
    private void initItemsPanel() {
        itemsPanel = new JPanel(new GridLayout(0, 5, 5, 5));
        itemsPanel.setBackground(new Color(230, 230, 230));
        int slotLimit = 20;
        int itemCount = inventory.getItems().size();

        // Ajouter les items de l'inventaire
        for (int i = 0; i < slotLimit; i++) {
            if (i < itemCount) {
                JButton itemSlot = getInventoryItemButton(inventory.getItems().get(i));
                itemsPanel.add(itemSlot);
            } else {
                JButton emptySlot = new JButton();
                emptySlot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
                emptySlot.setBackground(Color.DARK_GRAY);
                emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                itemsPanel.add(emptySlot);
            }
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Initialise le label pour afficher l'or du joueur
    private void initGoldLabel() {
        goldLabel = new JLabel("Gold : " + player.getGold() + " coins", SwingConstants.CENTER);
        goldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        goldLabel.setForeground(Color.YELLOW);
        add(goldLabel, BorderLayout.SOUTH);
    }

    // Rafraîchit tout le panneau d'inventaire
    public void refreshPanel() {
        // Supprime tous les composants actuels
        removeAll();

        // Reconstruit le panneau en appelant les méthodes d'initialisation
        initTitlePanel();
        initItemsPanel();
        initGoldLabel();

        // Revalide et repeint le panneau pour appliquer les changements
        revalidate();
        repaint();
    }

    private JButton getInventoryItemButton(Item item) {
        ImageIcon imageIcon = new ImageIcon(item.getImageSrc());
        JButton itemSlot = new JButton();
        itemSlot.setLayout(new BorderLayout());

        // Définir le tooltip avec les informations de l'item
        itemSlot.setToolTipText(item.getTooltipDescription());

        // Vérifie si l'item n'est pas une instance de Weapon et ajoute un label avec la quantité
        if (!(item instanceof Weapon)) {
            JLabel quantityLabel = new JLabel(String.valueOf(item.getQuantity()));
            quantityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            quantityLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            quantityLabel.setForeground(Color.WHITE);
            quantityLabel.setFont(new Font("Arial", Font.BOLD, 12));
            itemSlot.add(quantityLabel, BorderLayout.NORTH);
        }
        // Vérifie que si l'item est une arme equipe par le joueur
        if ((item instanceof Weapon) && item==player.getWeapon()){
            JLabel equipLabel = new JLabel("Equipped");
            equipLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            equipLabel.setVerticalAlignment(SwingConstants.BOTTOM);
            equipLabel.setForeground(Color.WHITE);
            equipLabel.setFont(new Font("Arial", Font.BOLD, 12));
            itemSlot.add(equipLabel, BorderLayout.NORTH);
        }
        
        // Crée l'action de clique
        itemSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.useItem(item);
                gameManager.requestFocusInWindow();
            }
        });

        itemSlot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        itemSlot.setIcon(imageIcon);
        itemSlot.setBackground(Color.DARK_GRAY);
        itemSlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return itemSlot;
    }

}
