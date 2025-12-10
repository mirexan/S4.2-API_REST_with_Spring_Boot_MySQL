package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.exceptions.DuplicateProviderName;
import cat.itacademy.s04.t02.n02.exceptions.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.exceptions.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.FruitDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	@Test
	void updateProvider_ShouldReturnOk_WhenProviderIdExistsAndDataIsValid() throws Exception {
		ProviderDTO newProviderDTO = new ProviderDTO(1L,"Sanahuja","Spain");
		Mockito.when(providerService.updateProviderById(Mockito.eq(1L), Mockito.any(ProviderDTO.class)))
				.thenReturn(newProviderDTO);
		mockMvc.perform(put("/providers/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newProviderDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Sanahuja"));
	}
	@Test
	void updateProvider_ShouldReturnNotFound_WhenProviderIdDoesNotExists() throws Exception {
		ProviderDTO newProviderDTO = new ProviderDTO(3L,"Sanahuja","Spain");
		Mockito.when(providerService.updateProviderById(Mockito.eq(3L), Mockito.any(ProviderDTO.class)))
				.thenThrow(new ProviderNotFoundException("Provider with id: " + 3L + " not found"));
		mockMvc.perform(put("/providers/{id}", 3L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newProviderDTO)))
				.andExpect(status().isNotFound());
	}
	@Test
	void updateProvider_ShouldReturnBadRequest_WhenNewProviderNameDuplicates() throws Exception {
		ProviderDTO newProviderDTO = new ProviderDTO(3L,"Sitjar","Spain");
		Mockito.when(providerService.updateProviderById(Mockito.eq(3L), Mockito.any(ProviderDTO.class)))
				.thenThrow(new DuplicateProviderName("The provider name is already in use"));
		mockMvc.perform(put("/providers/{id}", 3L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newProviderDTO)))
				.andExpect(status().isBadRequest());
	}
	@Test
	void deleteProviderById_ShouldReturnNoContent_WhenIdExistsAndNoFruits() throws Exception {
		Long providerId = 1L;
		Mockito.doNothing().when(providerService).deleteProviderById(providerId);
		mockMvc.perform(delete("/providers/{id}", providerId))
				.andExpect(status().isNoContent());
	}
	@Test
	void deleteProviderById_ShouldReturnNotFound_WhenIdDoesNotExists() throws Exception {
		Long nonExistentId = 99L;
		Mockito.doThrow(new ProviderNotFoundException("Provider not found"))
				.when(providerService).deleteProviderById(nonExistentId);
		mockMvc.perform(delete("/providers/{id}", nonExistentId))
				.andExpect(status().isNotFound()); // Esperamos HTTP 404
	}
	@Test
	void deleteProviderById_ShouldReturnBadRequest_WhenProviderHasFruits() throws Exception {
		Long providerIdWithFruits = 5L;
		Mockito.doThrow(new ProviderHasFruitsException("Cannot delete provider with fruits"))
				.when(providerService).deleteProviderById(providerIdWithFruits);
		mockMvc.perform(delete("/providers/{id}", providerIdWithFruits))
				.andExpect(status().isBadRequest());
	}
}
