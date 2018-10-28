package wbh.finanzapp.business;

public class GroupBean {

    private final long id;
    private final String name;
    private final String description;
    private final Boolean writable;

    public GroupBean(long id, String name, String description, Boolean writable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.writable = writable;
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
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", writable=" + writable +
                '}';
    }
}
