package org.txemasv.mycharacters.apiserver;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MainControllerTest {
	
	@Autowired
    private CharacterRepository characterRepository;
	
	@Before
    public void setup() throws Exception {

    }

	@Test
    public void dummyTest() throws Exception {
		assertTrue(true);
    }
}
