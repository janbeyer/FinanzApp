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

    public GroupBean(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public String toString() {
        Resources resources = ProfilesActivity.getContext().getResources();
        return resources.getString(R.string.field_name) + ": " + name +
            "\n" + resources.getString(R.string.field_description) + ": " + description;
    }
}
