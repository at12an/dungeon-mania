package dungeonmania;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Portal;
import dungeonmania.Entities.Collectables.TimeTurner;
import dungeonmania.Entities.*;
import dungeonmania.Goals.GoalFactory;
import dungeonmania.Goals.*;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DungeonManiaController {
    private Dungeon dungeon;
    private Config config;
    private List<String> EntityCache = new ArrayList<>();
    
    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        List<String> dungeons = FileLoader.listFileNamesInResourceDirectory("dungeons");
        if (!dungeons.contains(dungeonName)) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }
        
        List<String> configs = FileLoader.listFileNamesInResourceDirectory("configs");
        if (!configs.contains(configName)) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        Config config;
        try {
            String jsonConfigString = FileLoader.loadResourceFile("configs/" + configName + ".json");
            config = new Gson().fromJson(jsonConfigString, Config.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(configName + " is not a configuration that exists");
        }

        this.EntityCache = new ArrayList<>();
        this.dungeon = newDungeon(dungeonName, config, false);
        this.config = config;
        
        return this.dungeon.getDungeonResponseModel();
    }

    private Dungeon newDungeon(String dungeonName, Config c, boolean timetravel) {
        JsonObject jsonDungeonObject;
        try {
            String jsonDungeonString = FileLoader.loadResourceFile("dungeons/" + dungeonName + ".json");
            jsonDungeonObject = JsonParser.parseString(jsonDungeonString).getAsJsonObject();
        } catch (IOException e) {
            throw new IllegalArgumentException(dungeonName + " is not a dungeon that exists");
        }
        
        List<Entity> entities = new ArrayList<>();

        // Parse the list of entities
        for (JsonElement jsonEntity : jsonDungeonObject.getAsJsonArray("entities")) {
            entities.add(EntityFactory.getEntity(c, jsonEntity.getAsJsonObject(), timetravel));
        }

        //cache entity list
        if (this.EntityCache.size() == 0) {
            for (int i = 0; i < entities.size(); i++) {
                EntityCache.add(new String(entities.get(i).getId()));
            }
        } else {
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) instanceof Player == false || entities.get(i) instanceof OldPlayer == false) {
                    entities.get(i).setId(EntityCache.get(i));  
                } 
            }
        }

        List<Entity> portals = entities.stream().filter(e -> e.getClass().equals(Portal.class) || e.getClass().equals(TimeTravellingPortal.class)).collect(Collectors.toList());
        for (Entity e : portals ){
            for (Entity e2 : portals){
                ((Portal)e).link(((Portal)e2));
            }
        }
    
        // Parse goals
        JsonObject goalCondition = jsonDungeonObject.get("goal-condition").getAsJsonObject();
        Goal goal = GoalFactory.getEntity(c, goalCondition);

        // create dungeon
        return new Dungeon(c, entities, dungeonName, goal);
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        if (dungeon.getTimePortalStatus()) {
            rewind(30);
            dungeon.SetTimePortal(false);
        }
        return this.dungeon.getDungeonResponseModel();
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        if (dungeon.getPlayer() != null) {
            this.dungeon.tick(itemUsedId);
        }
        return getDungeonResponseModel();
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        if (dungeon.getPlayer() != null) {
            this.dungeon.tick(movementDirection);
        }
        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        if (dungeon.getPlayer() != null) {
            dungeon.build(buildable);
        }
        return dungeon.getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        if (dungeon.getPlayer() != null) {
            try{
                this.dungeon.interact(entityId);
            } catch (InvalidActionException e){
                throw e;
            } catch (IllegalArgumentException e){
                throw e;
            }
        }
        return dungeon.getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        String path = System.getProperty("user.dir") + "/saves";
    
        try {
            Files.createDirectories(Paths.get(path));
            FileOutputStream fileOut = new FileOutputStream(path + "/" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.dungeon);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

        return this.getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        String path = System.getProperty("user.dir") + "/saves";
        Dungeon d = null;
        try {
            Files.createDirectories(Paths.get(path));
            
            FileInputStream fileIn = new FileInputStream(path + "/" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            d = (Dungeon) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            throw new IllegalArgumentException("Invalid save name provided");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } 

        this.dungeon = d;

        return this.getDungeonResponseModel();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        List<String> games = new ArrayList<>();

        String path = System.getProperty("user.dir") + "/saves";

        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File[] files = new File(path).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".ser");
            }
        });
        
        for (File f: files) {
            games.add(f.getName().replace(".ser", ""));
        }
        
        return games;

    }

    /**
     * /game/rewind
     * 
     * @param ticks
     * @return
     * @throws IllegalArgumentException
     */
    public DungeonResponse rewind(int ticks) throws IllegalArgumentException {

        if (ticks <= 0 || dungeon.getTimecontroller().getCurrentTime() - ticks < 0 && ticks != 30){
            dungeon.SetTimePortal(false);
            throw new IllegalArgumentException("Cannot time travel");
        }
        dungeon.settimeTravel(true);
        dungeon.getTimecontroller().logRewind(true);

        //save player  
        Player ControllablePlayer = dungeon.getPlayer();

        if (ticks == 1 || ticks == 5) {
            //remove time turner
            dungeon.getPlayer().deleteByType(new TimeTurner("TimeTurner", null));
        }
        
        //save timer, number of rewinds and number of frontend calls
        //reverse time
        TimeController timeState = dungeon.getTimecontroller();
        List<BattleResponse> battles = dungeon.getDungeonResponseModel().getBattles();
        if (ticks == 30 && timeState.getCurrentTime() <= 30) {
            timeState.setCurrentTime(0);
        } else {
            timeState.setCurrentTime(timeState.getCurrentTime() - ticks);
        }
        timeState.setTick(0);

        String dungeonName = dungeon.getDungeonName();

        //create new dungeon, convert old player and add new player
        this.dungeon = newDungeon(dungeonName, this.config, true);
        this.dungeon.setTimecontroller(timeState);
        this.dungeon.setBattles(battles);

        //iterate over all previous action
        try {
            for (int i = 0; i <= timeState.getCurrentTime(); i++) {
                executeTicks(timeState.getPeriods().get(i));
            }
        } catch (Exception e) {
            dungeon.SetTimePortal(false);
            ;
        }   

        //get new player
        OldPlayer tmp = (OldPlayer) this.dungeon.getPlayer();
        tmp.setMoveReference(timeState);
        this.dungeon.setPlayer(ControllablePlayer);
        this.dungeon.addEntity(ControllablePlayer);

        dungeon.settimeTravel(false);
        return this.getDungeonResponseModel();
    }

    /**
     * /game/generateDungeon
     * 
     * @param xStart
     * @param yStart
     * @param xEnd
     * @param yEnd
     * @return
     */
    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd) {
        return null;
    }

    
    private void executeTicks(TimePeriod period) {

        for (String action: period.getActions()) {
            try {
                String[] commands = action.split("_");
                if (commands[0].equals("use")) {
                    this.dungeon.tick(commands[1]);
                } else if (commands[0].equals("move")) {
                    this.dungeon.tick(Direction.valueOf(commands[1]));
                } else if (commands[0].equals("build")) {
                    this.dungeon.build(commands[1]);
                } else if (commands[0].equals("interact")) {
                    this.dungeon.interact(commands[1]);
                }
            } catch (Exception e) {
                ;
            }
        }
    }

}
