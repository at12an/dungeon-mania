package dungeonmania.Entities;

import dungeonmania.reactions.Reaction;
import dungeonmania.reactions.Teleport;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class Portal extends Entity{
    
    private String colour;
    private Portal linkedPortal;
    
    public Portal(String type, Position position, String colour) {
        super(type, position);
        this.colour = colour;
        this.linkedPortal = null; // awaits complete initialization 
    }

    @Override
    public EntityResponse getEntityResponse() {
        String rtype = "portal";
        if (colour.equals("BLUE")) {
            rtype += "_blue";
        } else if (colour.equals("RED")) {
            rtype += "_red";
        } else if (colour.equals("GREY")) {
            rtype += "_grey";
        } else if (colour.equals("YELLOW")) {
            rtype += "_yellow";
        } else if (colour.equals("GREEN")) {
            rtype += "_green";
        }
        return new EntityResponse(super.getId(), rtype, super.getPosition(), isInteractable());
    }

    public Reaction interact(Entity e){
        if (e instanceof Player){
            return new Teleport(e, this);
        }
        return null;
    }
    public Portal getLinkedPortal(){
        return this.linkedPortal;
    }

    public void link(Portal p){
        if (p.linkedPortal == null && this.linkedPortal == null && 
            p.colour.equals(this.colour) && !this.equals(p))
            {
                this.linkedPortal = p;
                p.linkedPortal = this;
            }
    }
}
