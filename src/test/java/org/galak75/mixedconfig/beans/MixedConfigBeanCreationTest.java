package org.galak75.mixedconfig.beans;

import org.galak75.mixedconfig.beans.model.MainObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class MixedConfigBeanCreationTest {

    @Autowired
    private MainObject rootObject;

    @Test
    public void test() throws Exception {
        assertThat(rootObject, is(notNullValue()));
        assertThat(rootObject.getMiddle(), is(notNullValue()));
        assertThat(rootObject.getMiddle().getLeaf(), is("Inner String"));
    }
}
