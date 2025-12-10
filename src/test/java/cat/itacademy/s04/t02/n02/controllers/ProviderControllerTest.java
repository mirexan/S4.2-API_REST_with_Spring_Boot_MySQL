package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.exceptions.DuplicateProviderName;
import cat.itacademy.s04.t02.n02.model.ProviderDTO;
import cat.itacademy.s04.t02.n02.services.ProviderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private ProviderService providerService;
	@Autowired
	private ObjectMapper objectMapper;
	private ProviderDTO providerDTO;

	@BeforeEach
	void setUp() throws Exception {
		providerDTO = new ProviderDTO(1L,"Sitjar","Spain");
	}
	@Test
	void newProviderRegister_ShouldReturnCreated_WhenDataIsValid() throws Exception {
		ProviderDTO newProviderDTO = new ProviderDTO(null,"Fruits Gomez","Spain");
		Mockito.when(providerService.addProvider(Mockito.any(ProviderDTO.class)))
				.thenReturn(new ProviderDTO(1L,"Fruits Gomez","Spain"));
		mockMvc.perform(post("/providers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProviderDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.country").value("Spain"));
	}
	@Test
	void newProviderRegister_ShouldReturnBadRequest_WhenNameIsDuplicated() throws Exception {
		ProviderDTO newProviderDTO = new ProviderDTO(null,"Sitjar","France");
		Mockito.when(providerService.addProvider(Mockito.any(ProviderDTO.class)))
				.thenThrow(new DuplicateProviderName("The provider name is already in use"));
		mockMvc.perform(post("/providers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newProviderDTO)))
				.andExpect(status().isBadRequest());
	}

}
