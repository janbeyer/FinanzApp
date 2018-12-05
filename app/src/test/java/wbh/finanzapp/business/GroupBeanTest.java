package wbh.finanzapp.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GroupBeanTest {

    long id = 0;
    String name = "Group";
    String description = "Description";
    long profileId = 0;

    GroupBean TestGroupBean = new GroupBean(id, name, description, profileId);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileId() {
        assertEquals(profileId, TestGroupBean.getProfileId());
    }

    @Test
    public void getId() {
        assertEquals(id, TestGroupBean.getId());
    }

    @Test
    public void getName() {
        assertEquals(name, TestGroupBean.getName());
    }

    @Test
    public void getDescription() {
        assertEquals(description, TestGroupBean.getDescription());
    }

}