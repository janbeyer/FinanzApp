package wbh.finanzapp.business;

/**
 * An abstract bean class for all database object.
 */
public class AbstractBean {

    /**
     * The profile id
     */
    protected final long id;

    /**
     * The profile name.
     */
    protected final String name;

    /**
     * The profile description.
     */
    protected final String description;

    /**
     * Create a new ProfileBean.
     */
    AbstractBean(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Return the ID of this bean.
     */
    public long getId() {
        return id;
    }

    /**
     * Return the name of this bean.
     */
    public String getName() {
        return name;
    }

    /**
     * Return the description of this bean.
     */
    public String getDescription() {
        return description;
    }
}
