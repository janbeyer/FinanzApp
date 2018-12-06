package wbh.finanzapp.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionBeanTest {

    Long uniqueDate;      // Set if state is 1 = unique.
    Integer dayOfWeek;    // Set if state is 3 = weekly.
    Integer monthlyDay;   // Set if state is 4 = monthly.
    Integer yearlyMonth;  // Set if state is 5 = yearly.
    Integer yearlyDay;    // Set if state is 5 = yearly.

    long id = 0;
    String name = "Name";
    String description = "Description";
    long profileId = 0;
    long groupId = 0;
    double amount = 0;
    int state = 0;

    TransactionBean TestTransactionBean = new TransactionBean(id, name, description, profileId, groupId,
                           amount, state, uniqueDate, dayOfWeek, monthlyDay, yearlyMonth, yearlyDay);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileId() {
        assertEquals(id, TestTransactionBean.getProfileId());
    }

    @Test
    public void getGroupId() {
        assertEquals(groupId, TestTransactionBean.getGroupId());
    }

    @Test
    public void getAmount() {
        assertEquals(amount, TestTransactionBean.getAmount(), 0);
    }

    @Test
    public void getState() {
        assertEquals(state, TestTransactionBean.getState());
    }

       @Test
    public void getId() {
        assertEquals(id, TestTransactionBean.getId());
    }

    @Test
    public void getName() {
        assertEquals(name, TestTransactionBean.getName());
    }

    @Test
    public void getDescription() {
        assertEquals(description, TestTransactionBean.getDescription());
    }

}