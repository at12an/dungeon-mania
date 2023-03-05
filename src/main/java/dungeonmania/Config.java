package dungeonmania;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Config implements Serializable {

    @SerializedName("ally_attack")
    private int ally_attack;
    
    @SerializedName("ally_defence")
    private int ally_defence;

    @SerializedName("bomb_radius")
    private int bombRadius;

    @SerializedName("bow_durability")
    private int bowDurability;

    @SerializedName("bribe_amount")
    private int bribeAmount;

    @SerializedName("bribe_radius")
    private int bribeRadius;

    @SerializedName("enemy_goal")
    private int enemyGoal;

    @SerializedName("invincibility_potion_duration")
    private int invincibilityPotionDuration;

    @SerializedName("invisibility_potion_duration")
    private int invisibilityPotionDuration;

    @SerializedName("mercenary_attack")
    private int mercenaryAttack;

    @SerializedName("mercenary_health")
    private int mercenaryHealth;

    @SerializedName("player_attack")
    private int playerAttack;

    @SerializedName("player_health")
    private int playerHealth;

    @SerializedName("shield_defence")
    private int shieldDefence;

    @SerializedName("shield_durability")
    private int shieldDurability;

    @SerializedName("spider_attack")
    private int spiderAttack;

    @SerializedName("spider_health")
    private int spiderHealth;

    @SerializedName("spider_spawn_rate")
    private int spiderSpawnRate;

    @SerializedName("sword_attack")
    private int swordAttack;

    @SerializedName("sword_durability")
    private int swordDurability;

    @SerializedName("treasure_goal")
    private int treasureGoal;

    @SerializedName("zombie_attack")
    private int zombieAttack;

    @SerializedName("zombie_health")
    private int zombieHealth;

    @SerializedName("zombie_spawn_rate")
    private int zombieSpawnRate;   

    @SerializedName("assassin_attack")
    private int assassinAttack;

    @SerializedName("assassin_bribe_amount")
    private int assassinBribeAmount;

    @SerializedName("assassin_bribe_fail_rate")
    private double assassinBribeFailRate;

    @SerializedName("assassin_health")
    private int assassinHealth;

    @SerializedName("assassin_recon_radius")
    private int assassinReconRadius;

    @SerializedName("hydra_attack")
    private int hydraAttack;

    @SerializedName("hydra_health")
    private int hydraHealth;

    @SerializedName("hydra_health_increase_rate")
    private double hydraHealthIncreaseRate;

    @SerializedName("hydra_health_increase_amount")
    private int hydraHealthIncreaseAmount;

    @SerializedName("mind_control_duration")
    private int mindControlDuration;

    @SerializedName("midnight_armour_attack")
    private int midnightArmourAttack;

    @SerializedName("midnight_armour_defence")
    private int midnightArmourDefence;

    public int getAlly_attack() {
        return ally_attack;
    }

    public int getAlly_defence() {
        return ally_defence;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    public int getBowDurability() {
        return bowDurability;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public int getEnemyGoal() {
        return enemyGoal;
    }

    public int getInvincibilityPotionDuration() {
        return invincibilityPotionDuration;
    }

    public int getInvisibilityPotionDuration() {
        return invisibilityPotionDuration;
    }

    public int getMercenaryAttack() {
        return mercenaryAttack;
    }

    public int getMercenaryHealth() {
        return mercenaryHealth;
    }

    public int getPlayerAttack() {
        return playerAttack;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getShieldDefence() {
        return shieldDefence;
    }

    public int getShieldDurability() {
        return shieldDurability;
    }

    public int getSpiderAttack() {
        return spiderAttack;
    }

    public int getSpiderHealth() {
        return spiderHealth;
    }

    public int getSpiderSpawnRate() {
        return spiderSpawnRate;
    }

    public int getSwordAttack() {
        return swordAttack;
    }

    public int getSwordDurability() {
        return swordDurability;
    }

    public int getTreasureGoal() {
        return treasureGoal;
    }

    public int getZombieAttack() {
        return zombieAttack;
    }

    public int getZombieHealth() {
        return zombieHealth;
    }

    public int getZombieSpawnRate() {
        return zombieSpawnRate;
    }

    public int getAssassinAttack() {
        return assassinAttack;
    }

    public int getAssassinBribeAmount() {
        return assassinBribeAmount;
    }

    public double getAssassinBribeFailRate() {
        return assassinBribeFailRate;
    }

    public int getAssassinHealth() {
        return assassinHealth;
    }

    public int getAssassinReconRadius() {
        return assassinReconRadius;
    }

    public int getHydraAttack() {
        return hydraAttack;
    }

    public int getHydraHealth() {
        return hydraHealth;
    }

    public double getHydraHealthIncreaseRate() {
        return hydraHealthIncreaseRate;
    }

    public int getHydraHealthIncreaseAmount() {
        return hydraHealthIncreaseAmount;
    }

    public int getMindControlDuration() {
        return mindControlDuration;
    }

    public int getMidnightArmourAttack() {
        return midnightArmourAttack;
    }

    public int getMidnightArmourDefence() {
        return midnightArmourDefence;
    }
}
