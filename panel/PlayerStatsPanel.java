package rpg.panel;

import rpg.personnage.Player;
import javax.swing.*;
import java.awt.*;

public class PlayerStatsPanel extends JPanel {
    private JProgressBar healthBar;
    private JProgressBar manaBar;
    private JLabel experienceLabel;
    private JLabel playerClassLabel;
    private Player player;

    public PlayerStatsPanel(Player player, int width, int height) {
        this.player = player;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // Label pour la classe du joueur
        playerClassLabel = new JLabel("Class : " + player.getClass().getSimpleName(), SwingConstants.CENTER);
        playerClassLabel.setFont(new Font("Arial", Font.BOLD, 14));
        playerClassLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        add(playerClassLabel, BorderLayout.NORTH);

        // Panneau principal pour les statistiques
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panneau pour la santé
        JPanel healthPanel = new JPanel(new BorderLayout());
        JLabel healthLabel = new JLabel("Health : ");
        healthLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        healthBar = new JProgressBar(0, (int) player.getMaxHealth());
        healthBar.setValue((int) player.getHealth());
        healthBar.setString(player.getHealth() + "/" + player.getMaxHealth());
        healthBar.setStringPainted(true);
        healthBar.setForeground(Color.red);
        healthBar.setBackground(Color.DARK_GRAY);
        healthPanel.add(healthLabel, BorderLayout.WEST);
        healthPanel.add(healthBar, BorderLayout.CENTER);
        statsPanel.add(healthPanel);

        // Panneau pour le mana
        JPanel manaPanel = new JPanel(new BorderLayout());
        JLabel manaLabel = new JLabel("Mana : ");
        manaLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        manaBar = new JProgressBar(0, (int) player.getMaxMana());
        manaBar.setString(player.getMana() + "/" + player.getMaxMana());
        manaBar.setValue((int) player.getMana());
        manaBar.setStringPainted(true);
        manaBar.setForeground(Color.blue);
        manaBar.setBackground(Color.DARK_GRAY);
        manaPanel.add(manaLabel, BorderLayout.WEST);
        manaPanel.add(manaBar, BorderLayout.CENTER);
        statsPanel.add(manaPanel);

        add(statsPanel, BorderLayout.CENTER);

        // Label pour l'expérience
        experienceLabel = new JLabel("Experience : " + player.getXp(), SwingConstants.CENTER);
        experienceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        experienceLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(experienceLabel, BorderLayout.SOUTH);
    }

    // Méthode pour mettre à jour les barres et le label en fonction des statistiques actuelles du joueur
    public void refreshStats() {
        healthBar.setMaximum((int) player.getMaxHealth());
        healthBar.setValue((int) player.getHealth());
        healthBar.setString(player.getHealth() + "/" + player.getMaxHealth());

        manaBar.setMaximum((int) player.getMaxMana());
        manaBar.setValue((int) player.getMana());
        manaBar.setString(player.getMana() + "/" + player.getMaxMana());

        experienceLabel.setText("Experience : " + player.getXp());
        playerClassLabel.setText("Class : " + player.getClass().getSimpleName());
    }
}
