package dungeonmania.Entities.Logical;

import java.io.Serializable;
import java.util.List;

import dungeonmania.Entities.Entity;

public interface LogicalStrategy extends Serializable {   
    public boolean shouldBeActivated(List<Entity> allEntities, Entity entity);
    public boolean shouldBeDeactivated(List<Entity> allEntities, Entity entity);
}
