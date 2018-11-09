package wbh.finanzapp.business;

/**
 * The ProfileBean class store every properties of a single Profile.
 */
public class ProfileBean extends AbstractBean {

    /**
     * The last use time stamp.
     */
    private final long lastUse;

    /**
     * Create a new ProfileBean.
     */
    public ProfileBean(long id, String name, String description, long lastUse) {
        super(id, name, description);
        this.lastUse = lastUse;
    }

    @Override
    public String toString() {
        return name + "(id: " + id + ") " + description + " last use: " + lastUse;
    }
}
