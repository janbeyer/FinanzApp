package wbh.finanzapp.business;

/**
 * An abstract bean class for all database object.
 */
public class AbstractBean {

    /**
     * The bean id
     */
    protected final long id;

    /**
     * The bean name.
     */
    protected final String name;

    /**
     * The bean description.
     */
    protected final String description;

    /**
     * Create a new AbstractBean.
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

    @Override
    public String toString() {
        return id + ": " + name + " [" + description + "]";
    }
}
