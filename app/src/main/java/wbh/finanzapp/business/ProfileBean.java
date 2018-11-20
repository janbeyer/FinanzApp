package wbh.finanzapp.business;

public class ProfileBean extends AbstractBean {

    private final long lastUse; // Date value.

    public ProfileBean(long id, String name, String description, long lastUse) {
        super(id, name, description);
        this.lastUse = lastUse;
    }

    public long getLastUse() {
        return lastUse;
    }
}
