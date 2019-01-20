package wbh.finanzapp.business;

/**
 * An abstract bean class for all database object.
 */
public class AbstractBean {

    protected final long id;
    protected final String name;
    protected final String description;

    AbstractBean(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " [" + description + "]";
    }
}
