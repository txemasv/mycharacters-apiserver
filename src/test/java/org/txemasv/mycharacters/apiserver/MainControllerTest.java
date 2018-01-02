package org.txemasv.mycharacters.apiserver;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.txemasv.mycharacters.apiserver.model.Character;
import org.txemasv.mycharacters.apiserver.model.CharacterRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class MainControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private CharacterRepository characterRepository;

	private Character character;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		this.characterRepository.deleteAll();

		this.characterRepository.save(new Character("Jack", "Sparrow", "dirty pirate"));
		this.characterRepository.save(new Character("King", "Kong", "big monkey"));

		character = this.characterRepository.findAll().get(0);
	}

	@Test
	public void getOneCharacter_NotFound() throws Exception {
		mockMvc.perform(get("/characters/1234567890")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getOneCharacter_Ok() throws Exception {
		mockMvc.perform(get("/characters/" + character.getId())
				.contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(this.character.getId())))
				.andExpect(jsonPath("$.firstName", is(this.character.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(this.character.getLastName())))
				.andExpect(jsonPath("$.description", is(this.character.getDescription())));
	}

	@Test
	public void getAllCharacters_Ok() throws Exception {
		mockMvc.perform(get("/characters").contentType(contentType)).andExpect(status().isOk())
				.andExpect(jsonPath("$.characters", hasSize(2)))
				.andExpect(jsonPath("$.count", is(2)))
				.andExpect(jsonPath("$.limit", is(10)))
				.andExpect(jsonPath("$.page", is(0)))
				.andExpect(jsonPath("$.next", is(IsNull.nullValue())))
				.andExpect(jsonPath("$.previous", is(IsNull.nullValue())))
				.andExpect(jsonPath("$.characters[0].id", is(this.character.getId())))
				.andExpect(jsonPath("$.characters[0].firstName", is(this.character.getFirstName())))
				.andExpect(jsonPath("$.characters[0].lastName", is(this.character.getLastName())))
				.andExpect(jsonPath("$.characters[0].description", is(this.character.getDescription())));
	}
	
	@Test
    public void createCharacter_isCreated() throws Exception {
		String characterJson = json(new Character("George", "Of the Jungle", "crazy tarzan"));
        
        this.mockMvc.perform(post("/characters")
                .contentType(contentType)
                .content(characterJson))
                .andExpect(status().isCreated());
    }
	
	@Test
    public void upateCharacter_isUpdated() throws Exception {
      
        character.setFields(new Character("George", "Of the Jungle", "crazy tarzan"));
        String characterJson = json(character);
        
        this.mockMvc.perform(put("/characters")
                .contentType(contentType)
                .content(characterJson))
                .andExpect(status().isNoContent());
    }
	
	@Test
    public void updateCharacter_isNoExistent() throws Exception {
		character.setId("a-non-existent-id-string");
		String characterJson = json(character);
        
        this.mockMvc.perform(put("/characters")
                .contentType(contentType)
                .content(characterJson))
                .andExpect(status().isNotFound());
    }
	
	@Test
    public void updateCharacter_idNoSpecify() throws Exception {
		character.setId(null);
		String characterJson = json(character);
        
        this.mockMvc.perform(put("/characters")
                .contentType(contentType)
                .content(characterJson))
                .andExpect(status().isBadRequest());
    }
	
	@Test
    public void deleteCharacter_isDeleted() throws Exception {
		
		boolean before = this.characterRepository.findOne(character.getId()) != null;
		
        this.mockMvc.perform(delete("/characters/" + character.getId())
                .contentType(contentType))
                .andExpect(status().isNoContent());
        
        boolean after = this.characterRepository.findOne(character.getId()) != null;
        
        assertTrue(before != after); 
    }
	
	@Test
    public void deleteCharacter_isNoExistent() throws Exception {
		character.setId("a-non-existent-id-string");
        
        this.mockMvc.perform(delete("/characters/" + character.getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
