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

    public CharacterSelectionPanel(CharacterSelectionListener selectionListener) {
        this.selectionListener = selectionListener;

        // Liste des classes de personnages
        Class<? extends Player>[] characterClasses = new Class[]{
                Archer.class, Assassin.class, Berserker.class, Mage.class, Paladin.class
        };

        setLayout(new BorderLayout());

        // Panneau de fond qui sera en noir et prendra toute la taille
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setOpaque(true); // S'assurer qu'il est opaque

        // Panneau pour les boutons des personnages
        JPanel buttonPanel = new JPanel(new GridLayout(1, characterClasses.length));
        buttonPanel.setOpaque(false);
        characterButtons = new ArrayList<>();

        for (int i = 0; i < characterClasses.length; i++) {
            Class<? extends Player> characterClass = characterClasses[i];

            try {
                Player character = characterClass.getDeclaredConstructor(Position.class)
                        .newInstance(new Position(0, 0));

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
                button.addActionListener(e -> selectionListener.onCharacterSelected(characterId));
                characterButtons.add(button);
                buttonPanel.add(button);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);

        // Ajout du panneau d'instructions superposé avec fond noir
        InstructionPanel instructionPanel = new InstructionPanel();
        instructionPanel.setBackground(Color.BLACK);
        instructionPanel.setOpaque(true);
        add(instructionPanel, BorderLayout.NORTH);
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
