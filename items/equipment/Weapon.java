package rpg.items.equipment;

import rpg.items.Item;

public abstract class Weapon extends Item {
    protected double damage;
    protected double useMana;
    protected double damageSup;
    protected String classNameDamageSup;

    public Weapon(String imageSrc, double damage, int price, String name, double useMana, double damageSup, String classNameDamageSup) {
        super(name,imageSrc,price);
        this.damage = damage;
        this.useMana = useMana;
        this.damageSup = damageSup;
        this.classNameDamageSup = classNameDamageSup;
    }

    public boolean canAttack(double mana){
        return this.useMana<=mana;
    }

    public double getUseMana(){
        return this.useMana;
    }

    public String getClassNameDamageSup(){
        return this.classNameDamageSup;
    }

    public double getDamageSup(){
        return this.damageSup;
    }

    public double getDamage(){
        return this.damage;
    }

    @Override
    public String getDescription() {
        String manaString = this.useMana > 0 ? "Mana Required: " + this.useMana + "<br>" : "";
        return "<html><b>Base Damage:</b> " + this.damage + "<br>" +  // Bold for the label
                manaString +
                "<b>Additional Damage:</b> " + this.damageSup + "<br>" +
                "<b>Class-Specific Bonus Damage:</b> " + this.classNameDamageSup + "<br></html>";
    }
}
