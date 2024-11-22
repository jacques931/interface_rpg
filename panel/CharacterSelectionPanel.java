package rpg.panel;

import rpg.personnage.*;
import rpg.utils.Position;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionPanel extends JPanel {
    private List<JButton> characterButtons;
    private CharacterSelectionListener selectionListener;
    private JTextField nameField;
    private JButton startButton;
    private Integer selectedCharacterId = null;

    public CharacterSelectionPanel(CharacterSelectionListener selectionListener) {
        this.selectionListener = selectionListener;

        // Liste des classes de personnages
        Class<? extends Player>[] characterClasses = new Class[]{
                Archer.class, Assassin.class, Berserker.class, Mage.class, Paladin.class
        };

        setLayout(new BorderLayout());

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setOpaque(true);

        // Panneau pour les boutons des personnages
        JPanel buttonPanel = new JPanel(new GridLayout(1, characterClasses.length));
        buttonPanel.setOpaque(false);
        characterButtons = new ArrayList<>();

        for (int i = 0; i < characterClasses.length; i++) {
            Class<? extends Player> characterClass = characterClasses[i];

            try {
                Player character = characterClass.getDeclaredConstructor(String.class, Position.class)
                        .newInstance("PlayerName", new Position(0, 0));

                // Création des boutons avec le nom et les statistiques
                JButton button = new JButton("<html><center><span style='font-size:19px; font-weight:bold; color:red'>"
                        + characterClass.getSimpleName() + "</span><br><br>"
                        + "<span style='font-size:12px;'>" + character.getStats() + "</span></center></html>");

                button.setHorizontalAlignment(SwingConstants.CENTER);
                button.setBackground(new Color(70, 70, 70));
                button.setForeground(Color.WHITE);
                button.setFont(new Font("Serif", Font.PLAIN, 14));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 75, 0), 3),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                button.setFocusPainted(false);

                int characterId = i + 1;
                button.addActionListener(e -> {
                    selectedCharacterId = characterId;
                    highlightSelectedButton(button);
                });
                characterButtons.add(button);
                buttonPanel.add(button);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

        // Panneau d'instructions
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setBackground(Color.BLACK);
        instructionPanel.setOpaque(true);
        add(instructionPanel, BorderLayout.NORTH);

        // Panneau pour le champ de texte et le bouton de démarrage
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(Color.BLACK);
        namePanel.setOpaque(true);

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setForeground(Color.LIGHT_GRAY);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePanel.add(nameLabel);

        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        namePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        namePanel.add(nameField);
        namePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        startButton = new JButton("START");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(70, 150, 70));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Serif", Font.BOLD, 16));
        startButton.setFocusPainted(false);

        startButton.addActionListener(e -> startGame());

        namePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        namePanel.add(startButton);

        add(namePanel, BorderLayout.SOUTH);
    }

    // Function pour mettre en évidence le bouton sélectionné
    private void highlightSelectedButton(JButton selectedButton) {
        for (JButton button : characterButtons) {
            button.setBackground(new Color(70, 70, 70));
        }
        selectedButton.setBackground(new Color(120, 120, 200));
    }

    // Function pour démarrer le jeu
    private void startGame() {
        String playerName = nameField.getText().trim();

        if (selectedCharacterId == null) {
            JOptionPane.showMessageDialog(this, "Please select a character!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Passer l'ID du personnage et le nom au listener
        selectionListener.onGameStart(selectedCharacterId, playerName);
    }

    // Panneau d'instructions
    private static class InstructionPanel extends JPanel {
        public InstructionPanel() {
            setLayout(new GridLayout(5, 1));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Instructions
            add(createLabel("←↑→↓ : Move"));
            add(createLabel("E : Interact"));
            add(createLabel("Space : Attack"));
            add(createLabel("Click on object: Use/Equip"));
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(Color.LIGHT_GRAY);
            return label;
        }
    }
}
