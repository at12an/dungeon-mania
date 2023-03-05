package dungeonmania.Entities;

import dungeonmania.Entities.Collectables.*;
import dungeonmania.BecomeAlly;
import dungeonmania.Enemy;
import dungeonmania.Movement;
import dungeonmania.reactions.*;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

import java.util.stream.Collectors;
import java.util.*;
import dungeonmania.Entities.Weapons.*;
import dungeonmania.exceptions.InvalidActionException;

public class Player extends Entity implements Movement, Cloneable {

    private double health;
    private double attackDamage;
    private boolean invisibleStatus;
    private boolean invincibleStatus;
    private List<Entity> inventory = new ArrayList<>();
    private LinkedList<Position> positionHistory = new LinkedList<>();;
    private List<BecomeAlly> allies = new ArrayList<>();
    private Queue<Potion> potionsUsing = new LinkedList<Potion>();
    
    public Player(String type, Position position, int health, int attackDamage) {
        super(type, position);
        this.health = health;
        this.attackDamage = attackDamage;
        this.invisibleStatus = false;
        this.invincibleStatus = false;
    }

    public Reaction interact(Entity e){
        // Adding if the entity is a player, then start battle
        if (e instanceof Spider || e instanceof ZombieToast || (e instanceof Mercenary && !((Mercenary)e).isAllied()) 
            || (e instanceof Assassin && !((Assassin)e).isAllied()) || (e instanceof Hydra)
            || (e instanceof OldPlayer && this instanceof OldPlayer == false)) {
                if (!isPlayerInvisible() && this.checkItemCount(SunStone.class) <= 0 && this.checkItemCount(MidnightArmour.class) <= 0) {
                    return new Battle((Enemy) e, this, super.getPosition());
                }
        }
        return null;
    }

    public double getHealth() {
        return health;
    }

    /**
     * @return the current perRound attack damage of Player.
     */
    public double getAttackDamage() {
        return (attackDamage + getSwordDamage() + (getAllyAttackBonus()) * allies.size()) * ((hasBow()) ? 2 : 1);
    }

    public boolean hasBow(){
        return inventory.stream().anyMatch(e -> e instanceof Bow);
    }

    public double getSwordDamage(){
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof Sword).findFirst();
        return (s.isEmpty()) ? 0 : ((Sword) s.get()).getDamage();
    }
    
    public double getAllyAttackBonus(){
        int sum = 0;
        for (BecomeAlly m : this.allies) {sum += m.getAllyAttack();}
        return sum;
    }

    /**
     * @param enemyDamage incoming damage
     * @return enemyDamage - defenceBonuses
     */
    public double getEnemyDamage(double enemyDamage) {
        double damage = enemyDamage - getShieldDefense() - getAllyDefenceBonus();
        return (damage < 0) ? 0.0 : damage;
    }

    public double getShieldDefense(){
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof Shield).findFirst();
        return (s.isEmpty()) ? 0 : ((Shield) s.get()).getDefense();
    }

    public double getMidnightArmourDefense(){
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof MidnightArmour).findFirst();
        return (s.isEmpty()) ? 0 : ((MidnightArmour) s.get()).getDefense();
    }

    public double getMidnightArmourAttack(){
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof MidnightArmour).findFirst();
        return (s.isEmpty()) ? 0 : ((MidnightArmour) s.get()).getDamage();
    }

    public int getSceptreDuration(){
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof Sceptre).findFirst();
        return (s.isEmpty()) ? 0 : ((Sceptre) s.get()).getDuration();
    }

    public double getAllyDefenceBonus(){
        int sum = 0;
        for (BecomeAlly m : this.allies) {sum += m.getAllyDefence();}
        return sum;
    }

    public List<ItemResponse> getWeapons(){
        return getinventoryResponse().stream().filter(
                                            e -> e.getType().equals("bow") || 
                                            e.getType().equals("sword") || 
                                            e.getType().equals("shield")).
                                        collect(Collectors.toList());
    }

    public boolean addAlly(BecomeAlly e){
        if (this.allies.contains(e)) return false;
        this.allies.add(e);
        List<Entity> moneyToRemove = this.inventory.stream().filter(t -> t.getClass().equals(Treasure.class)).collect(Collectors.toList());
        for (int i = 0; i < e.getBribeAmount(); i ++){
            this.inventory.remove(moneyToRemove.remove(0));
        }
        return true;
    }

    public boolean addAlly2(BecomeAlly e){
        if (this.allies.contains(e)) return false;
        this.allies.add(e);
        return true;
    }

    public void wasteGold(BecomeAlly e) {
        List<Entity> moneyToRemove = this.inventory.stream().filter(t -> t.getClass().equals(Treasure.class)).collect(Collectors.toList());
        for (int i = 0; i < e.getBribeAmount(); i ++){
            this.inventory.remove(moneyToRemove.remove(0));
        }
    }

    public int numAllies(){
        return this.allies.size();
    }

    /**
     * moves entity, performs no logic, assumes input will not place 
     * entity in illegal position.
     * @param p
     */
    public void move(Position p){
        positionHistory.addFirst(this.getPosition());
        this.setPosition(p);
    }

    public Position getPositionForAlly(BecomeAlly ally){
        Entity reCast = (Entity) ally;
        int index = allies.indexOf(ally);
        return ((positionHistory.size() - index - 1) < 0) 
            ? reCast.getPosition() : positionHistory.get(index);
    }

    public void addItem(Entity e) {
        inventory.add(e);
    }

    public <T> int checkItemCount(Class<T> type) {
        return inventory.stream().filter(e -> e.getClass().equals(type)).collect(Collectors.toList()).size();
    }

    public void deleteItemInventory(Entity e) {
        inventory.removeIf(x -> x.equals(e));
    }

    public void deleteByType(Entity e) {
        inventory.remove(inventory.stream().filter(x -> x.getClass().equals(e.getClass())).findFirst().orElse(null));
    }
    
    public List<Entity> getInventory() {
        return this.inventory;
    }

    public List<ItemResponse> getinventoryResponse() {
        List<ItemResponse> returnList = inventory.stream().map(a -> new ItemResponse(a.getId(), a.getType())).collect(Collectors.toList());
        return returnList;
    }

    public List<String> getBuildableResponse() {
        //bow
        List<String> returnList = new ArrayList<>();
        if (new Bow("bow", null, 10, 10).isBuildable(this) == true) {
            returnList.add("bow");
        }
        //shield
        if (new Shield("shield", null, 10, 10).isBuildable(this) == true) {
            returnList.add("shield");
        }
        //sceptre
        if (new Sceptre("sceptre", null, 10).isBuildable(this) == true) {
            returnList.add("sceptre");
        }
        //midnight armour
        if (new MidnightArmour("midnight_armour", null, 10, 10).isBuildable(this) == true) {
            returnList.add("midnight_armour");
        }
        return returnList;

    }

    public boolean hasKey(int keyId) {
        Optional<Entity> s = inventory.stream().filter(e -> e instanceof Key).findFirst();
        return (s.isEmpty()) ? false : ((Key) s.get()).hasKeyId(keyId);
    }

    public void useKey() {
        inventory.removeIf(x -> x instanceof Key);
    }

    public void useWeapons() {

        List<Entity> brokenWeapons = new LinkedList<>();
        inventory.stream().filter(e -> e instanceof Weapon).map(e -> (Weapon) e).forEach(e -> e.useWeapon(brokenWeapons));
        for (Entity bw : brokenWeapons) {
            this.inventory.remove(bw);
        }
    }

    public Reaction addItemsBeingUsed(Collectable c) {
        if (c instanceof Bomb) {
            return new PlaceBomb((Bomb) c, getPosition());
        } else if (c instanceof Potion) {
            return new UsePotion((Potion)c, this);
        }
        return null;
    }

    public void addCurrentlyUsing(Potion p) {
        potionsUsing.add(p);
    }

    // here we decrease the duration of the potion 
    public void managePotions() {
        Potion currPotion = this.potionsUsing.peek();
        if (currPotion != null) {
            if (currPotion.hasExpired()) {
                this.potionsUsing.remove();
                if (this.potionsUsing.peek() != null) {
                    this.potionsUsing.peek().lessDuration();
                }
            } else {
                currPotion.lessDuration();
            }
        }
    }

    public boolean isPlayerInvisible() {
        return (this.potionsUsing.peek() instanceof InvisibilityPotion) ? true : false;
    }

    public boolean isPlayerInvincible() {
        return (this.potionsUsing.peek() instanceof InvincibilityPotion) ? true : false;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void tickMindControlAllies() {
        for (BecomeAlly a: allies) {
            if (isControlled(a)) {
                tickDuration(a);
                if (checkMindControlDuration(a)) {
                    a.becomeEnemy();
                }
            }
        }
        allies.removeIf(x->(checkMindControlDuration(x) && isControlled(x)));
    }

    public boolean checkMindControlDuration(BecomeAlly ally) {
        if (ally instanceof Mercenary) {
            return ((Mercenary)ally).getDuration() == 0;
        } else if (ally instanceof Assassin) {
            return ((Assassin)ally).getDuration() == 0;
        }
        return false;
    }
    public void tickDuration(BecomeAlly ally) {
        if (ally instanceof Mercenary) {
            ((Mercenary)ally).tickDuration();
        } else if (ally instanceof Assassin) {
            ((Assassin)ally).tickDuration();
        }
    }
    public boolean isControlled(BecomeAlly ally) {
        if (ally instanceof Mercenary) {
            return ((Mercenary)ally).isControlled();
        } else if (ally instanceof Assassin) {
            return ((Assassin)ally).isControlled();
        }
        return false;
    }

    public Reaction interactOnClick(Entity e) throws InvalidActionException {
        try {
            if (e instanceof ZombieToastSpawner){
                return new KillToaster(this, (ZombieToastSpawner) e);
            } 
            if (e instanceof Mercenary){
                if (this.checkItemCount(Sceptre.class) >= 1) {
                    return new MindControl(this, e, this.getSceptreDuration());
                } else {
                    return new BribeMerc(this, (Mercenary) e);
                }
            } 
            if (e instanceof Assassin) {
                if (this.checkItemCount(Sceptre.class) >= 1) {
                    return new MindControl(this, e, this.getSceptreDuration());
                } else {
                    return new BribeAssassin(this, (Assassin) e);
                }
            }
        } catch (InvalidActionException exp){
            throw exp;
        }
        return null;
    }
}
