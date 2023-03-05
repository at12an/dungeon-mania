package dungeonmania.response.models;

import java.io.Serializable;

public final class ItemResponse implements Serializable {
    private final String id;
    private final String type;

    public ItemResponse(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final String getId() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof ItemResponse)) return false;

        ItemResponse i = (ItemResponse) obj;
        return i.getType().equals(type);
    }
}
