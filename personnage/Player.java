package rpg.personnage;

import rpg.*;
import rpg.game.*;
import rpg.items.*;
import rpg.items.equipment.RemoteWeapon;
import rpg.items.equipment.Weapon;
import rpg.utils.*;

import java.awt.*;
import java.util.Objects;

public abstract class Player {
    private Position position;
    private Animation animation;
    // Taile du player
    private int playerWidth;
    private int playerHeight;
    // Derniere action
    private long lastAttackTime;
    private int lastDirection;

    private Inventory inventory;
    private GameManager gameManager;

    //Statistique
    private double maxHealth;
    private double health;
    private double maxMana;
    private double mana;
    private double damage;
    private double xp;
    private int gold;
    private int attackRange;
    private double cooldown;
    private float speed;

    private Weapon weapon;

    public Player(Position position, double health, double mana, double damage,double cooldown, int attackRange, float speed) {
        this.position = position;
        this.animation = new Animation(this.getImageSrc(), 150);
        this.playerWidth = Setting.playerWidth;
        this.playerHeight = Setting.playerHeight;
        this.cooldown = cooldown;
        this.attackRange = attackRange;
        this.lastDirection = 1; // Se met a droite par defaut
        this.inventory = new Inventory();

        //Statistique du joueur
        this.health = health;
        this.maxHealth = this.health;
        this.mana = mana;
        this.maxMana = this.mana;
        this.speed = speed;
        this.damage = damage;
        this.xp = 0;
        this.gold = 1000;
    }

    public void useItem(Item item){
        /* Si il clique sur une arme, il l'equipe ou le desequipe selon son statut de base */
        if((item instanceof Weapon)){
            Weapon weapon = (Weapon) item;
            if(this.getWeapon()==weapon){
                this.setWeapon(null);
            } else{
                this.setWeapon(weapon);
            }
            gameManager.updateInventory();
        }
        // Utilise une potion uniquement si a pas tous ses vie ou mana
        if((item instanceof Potion)){
            boolean useItem = ((Potion) item).usePotion(this);
            if(useItem){
                this.inventory.removeItemInInventory(item);
                gameManager.updateInventory();
            }
        }
    }

    public void buyItem(Item item){
        this.gold -= item.getPrice();
        this.inventory.addItemInInventory(item);
        gameManager.updateInventory();
    }

    public void hit(double damage){
        this.health -= damage;
        gameManager.updatePlayerStats();
    }

    public boolean heal(double heal){
        //Evite de gaspiller les potions
        if(this.health==this.maxHealth) return false;

        //Ne Recupere que le maximun de pv qu'il peut
        this.health = Math.min(this.health + heal, this.maxHealth);
        gameManager.updatePlayerStats();
        return true;
    }

    public boolean restoreMana(double mana){
        //Evite de gaspiller les potions
        if(this.mana==this.maxMana) return false;

        //Ne Recupere que le maximun de mana qu'il peut
        this.mana = Math.min(this.mana + mana, this.maxMana);
        gameManager.updatePlayerStats();
        return true;
    }

    public void setMana(double mana){
        this.mana -= mana;
        gameManager.updatePlayerStats();
    }

    // Renvoie les degats avec les degat par defaut, degat de l'arme et degat de l'arme specifique
    public double getFinalDamage(){
        double addDamage = this.getWeapon()!=null? haveDamageSup(this.getWeapon().getClassNameDamageSup())? this.getWeapon().getDamageSup() : 0 : 0;
        double weaponDamage = this.getWeapon()!=null?this.getWeapon().getDamage() : 0;
        return this.damage + weaponDamage + addDamage;
    }

    private boolean haveDamageSup(String currentClassNameDamageSup){
        return (this instanceof Archer) && Objects.equals(currentClassNameDamageSup, "Archer") ||
                (this instanceof Assassin) && Objects.equals(currentClassNameDamageSup, "Assassin") ||
                (this instanceof Mage) && Objects.equals(currentClassNameDamageSup, "Mage") ||
                (this instanceof Berserker) && Objects.equals(currentClassNameDamageSup, "Berserker") ||
                (this instanceof Paladin) && Objects.equals(currentClassNameDamageSup, "Paladin");
    }

    // Déplacement avec vérification des limites de la carte
    public void move(int direction, float dx, float dy, Map map , MonsterManager monsterManager) {
        dx *= speed;
        dy*= speed;
        // Met à jour la direction dans l'animation et l'avance de frame
        animation.setDirection(direction);
        animation.update();
        // Calculer la nouvelle position
        Position newPosition = new Position(position.getX() + dx, position.getY() + dy);
        lastDirection = direction;
        // Vérifier si la nouvelle position est dans les limites de la carte, en tenant compte de la largeur et de la hauteur du joueur
        if (map.isWalkable(newPosition, direction, playerWidth, playerHeight) &&
                !monsterManager.isPlayerCollidingWithMonster(newPosition, playerWidth, playerHeight)) {
            position.update(dx, dy);
        }
    }

    public void attak(MonsterManager monsterManager) {
        long currentTime = System.currentTimeMillis();
        // Vérifier si le cooldown est respecté
        if (currentTime - lastAttackTime >= cooldown * 1000) {
            Weapon weapon = this.getWeapon();
            // Si c'est une arme a distance
            if((this.getWeapon() instanceof RemoteWeapon)){
                if (weapon.canAttack(this.mana)) {
                    RemoteWeapon remoteWeapon = (RemoteWeapon) weapon;
                    // Lancer un projectile dans la direction actuelle
                    Projectile projectile = remoteWeapon.getProjectile();
                    projectile.setProjectile(new Position(position.getX(), position.getY()), lastDirection, this.getFinalDamage());
                    gameManager.addProjectile(projectile);

                    this.setMana(weapon.getUseMana());
                    lastAttackTime = currentTime;
                }
            } else{
                Monster attackMonster = monsterManager.getMonsterByPosition(position, lastDirection, attackRange);
                // Si il a un monstre proche du joueur
                if(attackMonster!=null){
                    // Il attaque soit avec son arme soit par defaut
                    if(this.getWeapon()!=null){
                        if(!weapon.canAttack(this.mana)){
                            return;
                        } else{
                            this.setMana(this.getWeapon().getUseMana());
                        }
                    }
                    // Inflige les degats et si il meurt, on le detruit et recuperer l'argent et l'experience
                    if(!attackMonster.hit(this.getFinalDamage())){
                        this.destroyMonster(attackMonster);
                    }
                    lastAttackTime = currentTime;
                }
            }
        }
    }

    public void destroyMonster(Monster monster){
        this.gold += monster.getGiveGold();
        this.xp += monster.getGiveXp();
        // Met a jour l'affichage
        gameManager.updatePlayerStats();
        gameManager.updateInventory();
        gameManager.destroyMonster(monster);
    }

    public String getStats() {
        return "<html>"
                + "<b>Health:</b> "+ this.health +"<br>"
                + "<b>Mana:</b> "+ this.mana +"<br>"
                + "<b>Damage:</b> "+ this.damage +"<br>"
                + "<b>Speed:</b> "+ this.speed +"<br>"
                + "<b>Cooldown:</b> "+ this.cooldown +" sec</html>";
    }

    public void draw(Graphics g, int offsetX, int offsetY) {
        g.drawImage(animation.getCurrentFrame(),
                (int) (position.getX() + offsetX),
                (int) (position.getY() + offsetY),
                playerWidth,
                playerHeight,
                null);
    }

    public abstract String getImageSrc();

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
        gameManager.updateInventory();
    }

    // Initialise GameManager apres la creation
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Inventory getInventory(){
        return this.inventory;
    }

    public int getGold(){
        return this.gold;
    }

    public boolean isDead(){
        return this.health<=0;
    }

    public Weapon getWeapon(){
        return this.weapon;
    }

    public Position getPosition() {
        return position;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public double getHealth(){
        return this.health;
    }

    public double getMana(){
        return this.mana;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public double getXp(){
        return this.xp;
    }
}
