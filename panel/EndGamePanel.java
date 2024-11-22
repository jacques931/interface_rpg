package rpg.panel;

import rpg.game.Inventory;
import rpg.personnage.Player;
import javax.swing.*;
import java.awt.*;

public class EndGamePanel extends JPanel {
    public EndGamePanel(Player player, long startTime, boolean success) {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34)); // Fond sombre pour le contraste

        // Message de fin de jeu
        String endMessageTxt = success ? "Victory! You are a true hero!" : "Defeat... Try again, brave soul!";
        JLabel endMessage = new JLabel(endMessageTxt, SwingConstants.CENTER);
        endMessage.setFont(new Font("Serif", Font.BOLD, 42));
        endMessage.setForeground(new Color(255, 215, 0)); // Couleur dorée pour le texte
        endMessage.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(endMessage, BorderLayout.NORTH);

        // Panneau central pour afficher les statistiques du joueur
        JPanel statsPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        statsPanel.setBackground(new Color(44, 44, 44));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 183, 107), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Classe du joueur
        JLabel classLabel = new JLabel("Class: " + player.getClass().getSimpleName(), SwingConstants.CENTER);
        classLabel.setFont(new Font("Serif", Font.BOLD, 24));
        classLabel.setForeground(new Color(240, 210, 140));
        statsPanel.add(classLabel);

        // Expérience du joueur
        JLabel experienceLabel = new JLabel("Total Experience: " + player.getXp(), SwingConstants.CENTER);
        experienceLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        experienceLabel.setForeground(Color.LIGHT_GRAY);
        statsPanel.add(experienceLabel);

        // Temps écoulé
        long timeElapsed = System.currentTimeMillis() - startTime;
        String formattedTime = String.format("%02d:%02d:%02d", timeElapsed / 3600000, (timeElapsed / 60000) % 60, (timeElapsed / 1000) % 60);
        JLabel timeLabel = new JLabel("Time Taken: " + formattedTime, SwingConstants.CENTER);
        timeLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        timeLabel.setForeground(new Color(173, 216, 230));
        statsPanel.add(timeLabel);

        // Or restant
        JLabel goldLabel = new JLabel("Remaining Gold: " + player.getGold() + " coins", SwingConstants.CENTER);
        goldLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        goldLabel.setForeground(new Color(255, 223, 0)); // Doré
        statsPanel.add(goldLabel);

        // Nombre d'objets dans l'inventaire
        int itemCount = Inventory.itemCollected;
        JLabel itemCountLabel = new JLabel("Items Collected: " + itemCount, SwingConstants.CENTER);
        itemCountLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        itemCountLabel.setForeground(Color.LIGHT_GRAY);
        statsPanel.add(itemCountLabel);

        add(statsPanel, BorderLayout.CENTER);

        // Bouton pour quitter le jeu, avec un design RPG
        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("Serif", Font.BOLD, 20));
        exitButton.setBackground(new Color(70, 70, 70));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(34, 34, 34));
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
