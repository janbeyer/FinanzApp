package wbh.finanzapp.business;

/**
 * A TransactionBean class store every properties of a single Transaction.
 */
public class TransactionBean extends AbstractBean {

    /**
     * Create a new TransactionBean.
     */
    public TransactionBean(long id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
