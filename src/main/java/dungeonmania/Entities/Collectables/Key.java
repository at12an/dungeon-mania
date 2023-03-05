package dungeonmania.Entities.Collectables;

import dungeonmania.Entities.Collectable;
import dungeonmania.util.Position;

/**
 * @author Max Ovington
 */
public class Key extends Collectable {

    private int keyId;

    public Key(String type, Position position, int keyId) {
        super(type, position);
        this.keyId = keyId; 
    }

    public int getKeyId() {
        return keyId;
    }
    public boolean hasKeyId(int keyId) {
        if (this.keyId == keyId) {
            return true;
        }
        return false;
    }
}
