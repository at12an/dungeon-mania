package dungeonmania.Entities;

import java.io.Serializable;

import dungeonmania.Interact.Interact;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public abstract class Entity implements Interact, Serializable {
    
    private static int idNum;
    private String id;
    private String type;
    private Position position;

    public Entity(String type, Position position) {
        this.type = type;
        this.position = position;
        this.id = "Entity" + idNum;
        idNum++;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position p){
        this.position = p;
    }

    public Boolean isInteractable() {
        return false;
    }

    public EntityResponse getEntityResponse() {
        return new EntityResponse(id, getType(), position, isInteractable());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Entity)) return false;
        
        Entity e = (Entity) obj;
        return 
            e.getType().equals(this.getType()) &&
            e.getId().equals(this.getId())     &&
            e.getPosition().equals(this.getPosition()); 
    }

    public static int getIdNum() {
        return idNum;
    }   
    
}
