package wbh.finanzapp.business;

import java.util.Date;

public class TransactionBean extends AbstractBean {

    private final long profileId;
    private final long groupId;

    private final double amount;

    // 1=unique;2=daily;3=weekly;4=monthly;5=yearly.
    private final int state;

    private final Long uniqueDate;      // Set if state is 1 = unique.
    private Date date;
    private final Integer dayOfWeek;    // Set if state is 3 = weekly.
    private final Integer monthlyDay;   // Set if state is 4 = monthly.
    private final Integer yearlyMonth;  // Set if state is 5 = yearly.
    private final Integer yearlyDay;    // Set if state is 5 = yearly.

    public TransactionBean(long id, String name, String description, long profileId, long groupId,
                           double amount, int state, Long uniqueDate, Integer dayOfWeek,
                           Integer monthlyDay, Integer yearlyMonth, Integer yearlyDay) {
        super(id, name, description);
        this.profileId = profileId;
        this.groupId = groupId;
        this.amount = amount;
        this.state = state;
        this.uniqueDate = uniqueDate;
        this.dayOfWeek = dayOfWeek;
        this.monthlyDay = monthlyDay;
        this.yearlyMonth = yearlyMonth;
        this.yearlyDay = yearlyDay;
        if(uniqueDate != null) {
            this.date = new Date(uniqueDate);
        }
    }

    public long getProfileId() {
        return profileId;
    }

    public long getGroupId() {
        return groupId;
    }

    public double getAmount() {
        return amount;
    }

    public int getState() {
        return state;
    }

    public Long getUniqueDate() {
        return uniqueDate;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Integer getMonthlyDay() {
        return monthlyDay;
    }

    public Integer getYearlyMonth() {
        return yearlyMonth;
    }

    public Integer getYearlyDay() {
        return yearlyDay;
    }




    @Override
    public String toString() {
        // 1=unique;2=daily;3=weekly;4=monthly;5=yearly.
        if(state == 1) {
            return super.toString() +
                    "\n" + amount + " Euro" + ", am " + date;
        } else if(state == 2) {
            return super.toString() +
                    "\n" + amount + " Euro" + ", täglich";
        } else if(state == 3) {
            return super.toString() +
                    "\n" + amount + " Euro" + ", wöchentlich am " + (dayOfWeek-1);
        } else if(state == 4) {
            return super.toString() +
                    "\n" + amount + " Euro" + ", monatlich am " + monthlyDay;
        } else if(state == 5) {
            return super.toString() +
                    "\n" + amount + " Euro" + ", jährlich am " + yearlyMonth + "." + yearlyDay;
        }
        return super.toString();
    }
}
