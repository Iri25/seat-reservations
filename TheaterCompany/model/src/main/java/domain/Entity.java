package domain;

import java.io.Serializable;

/**
 * 
 */
public class Entity<ID> implements Serializable {

    /**
     * Default constructor
     */
    public Entity() {
    }

    /**
     * 
     */
    protected ID id;

    /**
     *
     * @param id
     */
    public Entity(ID id) {

        this.id = id;
    }

    /**
     * @return
     */
    public ID getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(ID id) {
        this.id = id;
    }

}