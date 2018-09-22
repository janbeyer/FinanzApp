package wbh.finanzapp.business;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    /**
     * Convert the integer stored in the data base to a readable date format.
     * @param i the integer value.
     * @return the converted date or null.
     */
    private Date integerToDate(long i) {
        Long value = i;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = originalFormat.parse(value.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public String toString() {
        return "ProfileBean " + name +
                "\n " + description + ", " + integerToDate(lastUse);
    }
}
