package rpg.game;

import rpg.items.*;
import rpg.items.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory {
    private List<Item> items;
    public static int itemCollected = 0;

    public Inventory() {
        items = new ArrayList<>();
    }

    // Ajoute la quantité de 1 ou crée un nouveau element dans la liste
    public void addItemInInventory(Item item){
        if(!(item instanceof Weapon)){
            int itemId = getItemInInventory(item);
            if(itemId!=-1){
                this.items.get(itemId).setQuantity(1);
            }
            else {
                item.resetQuantity();
                this.items.add(item);
            }
        } else {
            this.items.add(item);
        }
        itemCollected++;
    }

    // Supprimer l'item ou retire la quantité de 1 si c n'est pas une arme
    public void removeItemInInventory(Item item){
        if(!(item instanceof Weapon)){
            int itemId = getItemInInventory(item);
            if(itemId!=-1){
                Item inventoryItem = this.items.get(itemId);
                inventoryItem.setQuantity(-1);
                if(inventoryItem.getQuantity()<=0){
                    this.items.remove(item);
                }
            }

        } else {
            this.items.remove(item);
        }
    }

    // Retourne la position de l'item dans la liste
    public int getItemInInventory(Item item){
        for (int i = 0; i < this.items.size(); i++) {
            if(Objects.equals(this.items.get(i).getName(), item.getName())){
                return i;
            }
        }
        return -1;
    }

    // Récupère tous les items de l'inventaire
    public List<Item> getItems() {
        return items;
    }

    // Récupère le nombre total d'objets dans l'inventaire
    public int getSize() {
        return items.size();
    }
}
