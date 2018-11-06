package wbh.finanzapp.business;

public class GroupBean {

    private final long id;
    private final long profileId;
    private final String name;
    private final String description;

    public GroupBean(long id, long profileId, String name, String description) {
        this.id = id;
        this.profileId = profileId;
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
        return "GroupBean{" +
                "id=" + id +
                ", profileId" + profileId +
                ", name='" + name + '\'' +
                ", description='" + description +
                '}';
    }
}
