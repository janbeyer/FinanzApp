package wbh.finanzapp.business;

public class ProfileBean {

    private final long id;
    private final String name;
    private final String description;
    private final long lastUse;
    private final int startValue;

    public ProfileBean(long id, String name, String description, long lastUse, int startValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUse = lastUse;
        this.startValue = startValue;
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
        return "ProfileBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lastUse=" + lastUse +
                ", startValue=" + startValue +
                '}';
    }
}
