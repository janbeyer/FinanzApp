package wbh.finanzapp.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileBeanTest {

    long id = 0;
    String name = "Profile";
    String description = "Description";
    long lastUse = 0;

    ProfileBean TestProfileBean = new ProfileBean(0, "Profile", "Description", 0);

        @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getId() {
        assertEquals(id, TestProfileBean.getId());
    }

    @Test
    public void getName() {
        assertEquals(name, TestProfileBean.getName());
    }

    @Test
    public void getDescription() {
        assertEquals(description, TestProfileBean.getDescription());
    }

    @Test
    public void getLastUse() {
        assertEquals(lastUse, TestProfileBean.getLastUse());
    }

    //@Test
    //public void toString() {
    //}
}