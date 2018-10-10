package wbh.finanzapp.business;

public class GroupBean {

    private long id;
    private String name;
    private String description;
    private Boolean writable;

    public GroupBean(long id, String name, String description, Boolean writable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.writable = writable;
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

    @SuppressWarnings("unused")
    public Boolean getWritable() {
        return writable;
    }

    @SuppressWarnings("unused")
    public void setWritable(Boolean writable) {
        this.writable = writable;
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
