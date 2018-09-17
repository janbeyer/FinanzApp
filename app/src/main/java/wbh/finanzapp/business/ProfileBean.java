package wbh.finanzapp.business;

public class ProfileBean {

    private long id;
    private String name;
    private String description;
    private long lastUse;

    public ProfileBean(long id, String name, String description, long lastUse) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUse = lastUse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getLastUse() {
        return lastUse;
    }

    public void setLastUse(long lastUse) {
        this.lastUse = lastUse;
    }

    @Override
    public String toString() {
        return "ProfileBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lastUse=" + lastUse +
                '}';
    }
}
