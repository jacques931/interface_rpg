package rpg.items;


import rpg.personnage.Player;
import java.util.Objects;

public class Potion extends Item {
    private String type;
    private double amount;

    public Potion(String name, String imageSrc,int price, String type, double amount) {
        super(name,imageSrc,price);
        this.type = type;
        this.amount = amount;
    }

    public String getType(){
        return this.type;
    }

    public double getAmount(){
        return this.amount;
    }

    public boolean usePotion(Player player){
        boolean useItem = false;
        if(Objects.equals(this.getType(), "health")){
            useItem = player.heal(this.getAmount());
        } else if(Objects.equals(this.getType(), "mana")){
            useItem = player.restoreMana(this.getAmount());
        }
        return useItem;
    }

    @Override
    public String getDescription() {
        String typeTxt = Objects.equals(this.type, "health") ? "life point" : Objects.equals(this.type, "mana") ? "mana point" : "Inconnu";
        return "Effects: restores " + this.amount + " " + typeTxt;
    }
}
