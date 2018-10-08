package wbh.finanzapp.business;

import android.content.res.Resources;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import wbh.finanzapp.R;
import wbh.finanzapp.activity.ProfilesActivity;

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

    public Boolean getWritable() {
        return writable;
    }

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
