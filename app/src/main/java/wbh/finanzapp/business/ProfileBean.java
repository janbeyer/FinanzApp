package wbh.finanzapp.business;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wbh.finanzapp.R;
import wbh.finanzapp.activity.ProfilesActivity;

public class ProfileBean {

    private long id;
    private String name;
    private String description;
    private long lastUse;
    private int startValue;

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

    public int getStartValue() { return startValue; }

    public void setStartValue(int startValue) { this.startValue = startValue; }

    @Override
    public String toString() {
        Resources resources = ProfilesActivity.getContext().getResources();
        return resources.getString(R.string.profile_name) + ": " + name +
            "\n" + resources.getString(R.string.profile_description) + ": " + description +
            "\n" + resources.getString(R.string.profile_lastUse) + ": " + convertTime(lastUse) +
            "\n" + resources.getString(R.string.profile_startValue) + ": " + startValue;
    }

    private String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return format.format(date);
    }
}