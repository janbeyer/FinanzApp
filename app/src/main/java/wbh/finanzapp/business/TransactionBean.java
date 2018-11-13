package wbh.finanzapp.business;

/**
 * A GroupBean class store every properties of a single Group.
 */
public class TransactionBean extends AbstractBean {

    /**
     * A reference to the Profile ID.
     */
    private final long profileId;

    /**
     * A reference to the Group ID.
     */
    private final long groupId;

    /**
     * The amount of the transaction.
     */
    private final long amount;

    /**
     * true for expenditures and false for revenues.
     */
    private final boolean expenditure;

    /**
     * The state of the transaction.
     * 1=unique;2=daily;3=weekly;4=monthly;5=yearly.
     */
    private final int state;

    /**
     * The unique date for a unique transaction.
     */
    private final Long uniqueDate;

    /**
     * The day of week for a weeky transaction.
     */
    private final Integer dayOfWeek;

    /**
     * The monthly day for a monthly transaction.
     */
    private final Integer monthlyDay;

    /**
     * The yearly month for a yearly transaction.
     */
    private final Integer yearlyMonth;

    /**
     * The yearly day for a yearly transaction.
     */
    private final Integer yearlyDay;

    /**
     * Create a new GroupBean.
     */
    public TransactionBean(long id, String name, String description, long profileId, long groupId,
                           long amount, boolean expenditure, int state, Long uniqueDate,
                           Integer dayOfWeek, Integer monthlyDay, Integer yearlyMonth,
                           Integer yearlyDay) {
        super(id, name, description);
        this.profileId = profileId;
        this.groupId = groupId;
        this.amount = amount;
        this.expenditure = expenditure;
        this.state = state;
        this.uniqueDate = uniqueDate;
        this.dayOfWeek = dayOfWeek;
        this.monthlyDay = monthlyDay;
        this.yearlyMonth = yearlyMonth;
        this.yearlyDay = yearlyDay;
    }
}
