package rpg.panel;

import rpg.game.GameManager;
import rpg.game.Store;
import rpg.items.Item;
import rpg.personnage.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StorePanel extends JPanel {
    private static final int FIXED_WIDTH = 300;
    private static final int FIXED_HEIGHT = 400;
    private static final int SLOT_SIZE = 50;
    private JPanel itemsPanel;
    private Player player;
    private GameManager gameManager;

    public StorePanel(Store store, GameManager gameManager, Player player) {
        this.player = player;
        this.gameManager = gameManager;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(FIXED_WIDTH, FIXED_HEIGHT));
        setBackground(Color.LIGHT_GRAY);

        // Barre de titre avec bouton de fermeture
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setPreferredSize(new Dimension(FIXED_WIDTH, 50));
        titlePanel.setBackground(Color.LIGHT_GRAY);

        // Titre du shop
        JLabel titleLabel = new JLabel("Store", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Bouton de fermeture
        JButton closeButton = new JButton("x");
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(Color.BLACK);

        // Ajouter une action pour fermer le StorePanel
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Cache le StorePanel
                gameManager.requestFocusInWindow();
            }
        });

        // Ajouter le titre et le bouton de fermeture au panel du titre
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(closeButton, BorderLayout.EAST);

        // Ajouter le panel de titre au nord du StorePanel
        add(titlePanel, BorderLayout.NORTH);

        // Panel pour les slots d'articles
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(0, 5, 5, 5));
        itemsPanel.setBackground(new Color(230, 230, 230));

        // Ajouter les slots d'articles
        for (int i = 0; i < store.getItems().size(); i++) {
            JButton itemSlot = getStoreItemButton(store, i);
            itemsPanel.add(itemSlot);
        }

        // Remplir les slots restants avec des cases vides si nécessaire
        int emptySlots = 25 - store.getItems().size();
        for (int i = 0; i < emptySlots; i++) {
            JButton emptySlot = new JButton();
            emptySlot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
            emptySlot.setBackground(Color.DARK_GRAY);
            emptySlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            itemsPanel.add(emptySlot);
        }

        // Ajouter le panel des items dans un JScrollPane pour permettre le défilement
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton getStoreItemButton(Store store, int i) {
        Item item = store.getItems().get(i);
        ImageIcon imageIcon = new ImageIcon(item.getImageSrc());
        JButton itemSlot = new JButton();

        // Définir le tooltip avec les informations de l'item
        itemSlot.setToolTipText(item.getTooltipDescription());

        itemSlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Vérifie si le joueur a assez d'argent
                if (player.getGold() < item.getPrice()) {
                    JOptionPane.showMessageDialog(
                            itemSlot,
                            "You don't have enough money, you only have: " + player.getGold() + " gold.",
                            "Unable to purchase",
                            JOptionPane.WARNING_MESSAGE
                    );
                    gameManager.requestFocusInWindow();
                    return; // Sortie de l'action si le joueur n'a pas assez d'argent
                }

                // Demande de confirmation pour l'achat
                int confirm = JOptionPane.showConfirmDialog(
                        itemSlot,
                        "Do you really want to buy " + item.getName() + " to " + item.getPrice() + " gold ?",
                        "Purchase confirmation",
                        JOptionPane.YES_NO_OPTION
                );

                // Vérifie si l'utilisateur a cliqué sur "Oui"
                if (confirm == JOptionPane.YES_OPTION) {
                    // Effectuer l'achat : ajouter l'item à l'inventaire et retirer le prix de l'or du joueur
                    player.buyItem(item);
                }

                gameManager.requestFocusInWindow();
            }
        });

        itemSlot.setPreferredSize(new Dimension(SLOT_SIZE, SLOT_SIZE));
        itemSlot.setIcon(imageIcon);
        itemSlot.setBackground(Color.DARK_GRAY);
        itemSlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        // Forcer l'affichage immédiat du tooltip dans StorePanel
        ToolTipManager.sharedInstance().setInitialDelay(0);
        return itemSlot;
    }
}
