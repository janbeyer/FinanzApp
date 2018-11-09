package wbh.finanzapp.business;

/**
 * A GroupBean class store every properties of a single Group.
 */
public class GroupBean extends AbstractBean {

    /**
     * A reference to the Profile ID.
     */
    private final long profileId;

    /**
     * Create a new GroupBean.
     */
    public GroupBean(long id, String name, String description, long profileId) {
        super(id, name, description);
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return  name + "(id: " + id + ", profileId: " + profileId + ")" + description;
    }
}
