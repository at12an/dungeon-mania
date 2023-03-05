package dungeonmania.reactions;

import dungeonmania.*;
import java.util.*;
import dungeonmania.Entities.*;
import dungeonmania.Entities.Weapons.MidnightArmour;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Position;

public class Battle extends Reaction{

    private Enemy enemy;
    private Player player;
    private Position dest;

    public Battle(Enemy enemy, Player player, Position dest) {
        this.player = player;
        this.enemy = enemy;
        this.dest = dest;
    }

    public void execute(Dungeon d, Collection<Reaction> reactions) {

        BattleResponse battle = 
            new BattleResponse(((Entity)enemy).getType(), new ArrayList<>(), player.getHealth(), enemy.getHealth());

        if (!player.isPlayerInvincible()) {
        // battle still active whilst noones died, keep having rounds
            while (enemy.getHealth() > 0.0 && player.getHealth() > 0.0) {

                double playerRoundHealth = player.getHealth();
                double enemyRoundHealth = enemy.getHealth();
                double midnight_armour = 0;
                double midnight_attack = 0;
                if (player.checkItemCount(MidnightArmour.class) > 0 && d.checkEntityCount(ZombieToast.class) == 0) {
                    midnight_armour = player.getMidnightArmourDefense();
                    midnight_attack = player.getMidnightArmourAttack();
                }
                player.setHealth(
                    round(player.getHealth() - (player.getEnemyDamage(enemy.getAttack()) -midnight_armour)/10.0, 1));
                
                if (enemy instanceof Hydra) {
                    Hydra h = (Hydra) enemy;
                    if (h.isIncreased()) {
                        enemy.setHealth(
                            round(enemy.getHealth() + h.getIncreaseAmount(), 1));
                    } else {
                        enemy.setHealth(
                            round(enemy.getHealth() - (player.getAttackDamage()+midnight_attack)/5.0, 1)
                        );
                    }
                } else {
                    enemy.setHealth(
                        round(enemy.getHealth() - (player.getAttackDamage()+midnight_attack)/5.0, 1)
                    );
                }

                battle.getRounds().add(
                    new RoundResponse(
                        round(player.getHealth() - playerRoundHealth, 1), 
                        round(enemy.getHealth() - enemyRoundHealth, 1), 
                        player.getWeapons()));

                if (player.getHealth() <= 0.0) {
                    reactions.add(new LoseGame(player));
                } else if (enemy.getHealth() <= 0.0) {
                    reactions.add(new KillEnemy((Entity) enemy));
                    reactions.add(new Move(player, dest));
                }        
            }
        } else {
            reactions.add(new KillEnemy((Entity) enemy));
            battle.getRounds().add(
                    new RoundResponse(
                        round(player.getHealth(), 1), 
                        round(enemy.getHealth(), 1), 
                        player.getWeapons()));
            reactions.add(new Move(player, dest));
        }

        //iterate and use weapons
        player.useWeapons();

        //add battle to dungeon
        d.addBattle(battle);
    }
    /* stolen from here.
     * https://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
     * TN
     */
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    
    // TODO change priority
    public int getPriority() {
        return 2;
    }

    public String getType() {
        return "Battle";
    }
}
