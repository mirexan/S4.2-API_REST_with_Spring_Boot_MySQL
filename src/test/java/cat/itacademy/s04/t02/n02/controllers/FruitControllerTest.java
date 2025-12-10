package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.exceptions.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.exceptions.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.services.FruitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FruitController.class)
public class FruitControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private FruitService fruitService;
	@Autowired
	private ObjectMapper objectMapper;
	private FruitDTO fruitDTO;

	@BeforeEach
	void setUp() throws Exception {
		fruitDTO = new FruitDTO(1L, "Melon", 2,1L);
	}

	@Test
	void newFruitRegister_ShouldReturnCreated_WhenDataIsValid() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(null, "Apple", 2,1L);
		Mockito.when(fruitService.addFruit(Mockito.any(FruitDTO.class)))
				.thenReturn(new FruitDTO(1L, "Apple", 2,1L));
		mockMvc.perform(post("/fruits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newFruitDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Apple"));
	}

	@Test
	void newFruitRegister_ShouldReturnBadRequest_WhenDataIsNotValid() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(null, null, -2,1L);
		mockMvc.perform(post("/fruits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newFruitDTO)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void newFruitRegister_ShouldReturnProviderNotFound_WhenProviderIdDoesNotExist() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(null, "banana", 2,4L);
		Mockito.when(fruitService.addFruit(Mockito.any(FruitDTO.class)))
						.thenThrow(new ProviderNotFoundException("Provider with id "
								+ newFruitDTO.providerId() + " does not exist"));
		mockMvc.perform(post("/fruits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newFruitDTO)))
				.andExpect(status().isNotFound());
	}

	@Test
	void getAllFruits_ShouldReturnOK_WhenListIsNotEmpty() throws Exception {
		Mockito.when(fruitService.getAllFruits()).thenReturn(List.of(fruitDTO));
		mockMvc.perform(get("/fruits"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Melon"));
	}
	@Test
	void getAllFruits_ShouldReturnOK_WhenListIsEmpty() throws Exception {
		mockMvc.perform(get("/fruits"))
				.andExpect(status().isOk());
	}
	@Test
	void getFruitsByProviderId_ShouldReturnOK_WhenProviderIdExists() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(2L, "banana", 2, 1L);
		Mockito.when(fruitService.getFruitsByProviderId(1L)).thenReturn(List.of(newFruitDTO));
		mockMvc.perform(get("/fruits")
						.param("providerId", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].providerId").value(1L));
	}
	@Test
	void getFruitsByProviderId_ShouldReturnNotFound_WhenProviderIdDoesNotExists() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(2L, "banana", 2, 1L);
		Mockito.when(fruitService.getFruitsByProviderId(1L))
				.thenThrow(new ProviderNotFoundException("Provider with id "
						+ newFruitDTO.providerId() + " does not exist"));
		mockMvc.perform(get("/fruits")
						.param("providerId", "1"))
				.andExpect(status().isNotFound());
	}
	@Test
	void getFruitById_ShouldReturnOK_WhenIdExists() throws Exception {
		Mockito.when(fruitService.getFruitById(1L)).thenReturn(fruitDTO);
		mockMvc.perform(get("/fruits/{id}", 1L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Melon"));
	}
	@Test
	void getFruitById_ShouldReturnNotFound_WhenIdDoesNotExists() throws Exception {
		Mockito.when(fruitService.getFruitById(2L)).thenThrow(new FruitNotFoundException("Does not exist"));
		mockMvc.perform(get("/fruits/{id}", 2L))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateExistingFruit_ShouldReturnOk_WhenDataIsValid() throws Exception {
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), 5, fruitDTO.providerId());
		Mockito.when(fruitService.updateFruitById(Mockito.eq(1L), Mockito.any(FruitDTO.class)))
				.thenReturn(newWeigthFruitDTO);
		mockMvc.perform(put("/fruits/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newWeigthFruitDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.weightInKilos").value(5));
	}

	@Test
	void updateExistingFruit_ShouldReturnBadRequest_WhenDataIsNotValid() throws Exception {
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), -5, fruitDTO.providerId());
		Mockito.when(fruitService.updateFruitById(Mockito.eq(1L), Mockito.any(FruitDTO.class)))
				.thenReturn(newWeigthFruitDTO);
		mockMvc.perform(put("/fruits/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newWeigthFruitDTO)))
				.andExpect(status().isBadRequest());
	}
	@Test
	void updateExistingFruit_ShouldReturnNotFound_WhenDataDoesNotExist() throws Exception {
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), 5, fruitDTO.providerId());
		Mockito.when(fruitService.updateFruitById(Mockito.eq(1L), Mockito.any(FruitDTO.class)))
				.thenThrow(new FruitNotFoundException("Does not exist"));
		mockMvc.perform(put("/fruits/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newWeigthFruitDTO)))
				.andExpect(status().isNotFound());
	}
	@Test
	void deleteFruitById_ShouldReturnNoContent_WhenIdExists() throws Exception {
		Mockito.doNothing().when(fruitService).deleteFruitById(1L);
		mockMvc.perform(delete("/fruits/{id}", 1L))
				.andExpect(status().isNoContent());
	}
	@Test
	void deleteFruitById_ShouldReturnNotFound_WhenIdDoesNotExists() throws Exception {
		Mockito.doThrow(new FruitNotFoundException("Does not exist"))
						.when(fruitService).deleteFruitById(2L);
		mockMvc.perform(delete("/fruits/{id}", 2L))
				.andExpect(status().isNotFound());
	}
}
