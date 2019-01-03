package sec;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;
import sec.controller.DefaultController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringSecurityThymeleafApplicationIntegrationTest {

    @Autowired
    DefaultController defaultController;

    @Autowired
    WebApplicationContext wac;

    @Test
    public void whenConfigured_thenLoadsContext() {
        assertNotNull(defaultController);
        assertNotNull(wac);
    }

}
