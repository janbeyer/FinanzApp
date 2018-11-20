package wbh.finanzapp.business;

public class GroupBean extends AbstractBean {

    private final long profileId;

    public GroupBean(long id, String name, String description, long profileId) {
        super(id, name, description);
        this.profileId = profileId;
    }

    public long getProfileId() {
        return profileId;
    }
}
