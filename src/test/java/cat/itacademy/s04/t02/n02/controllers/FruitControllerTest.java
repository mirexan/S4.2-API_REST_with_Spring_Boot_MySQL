package cat.itacademy.s04.t02.n01.controllers;

import cat.itacademy.s04.t02.n01.exceptions.FruitNotFoundException;
import cat.itacademy.s04.t02.n01.model.FruitDTO;
import cat.itacademy.s04.t02.n01.services.FruitService;
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
		fruitDTO = new FruitDTO(1L, "Melon", 2);
	}

	@Test
	void newFruitRegister_ShouldReturnOK_WhenDataIsValid() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(null, "Apple", 2);
		Mockito.when(fruitService.addFruit(Mockito.any(FruitDTO.class)))
				.thenReturn(new FruitDTO(1L, "Apple", 2));
		mockMvc.perform(post("/fruits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newFruitDTO)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Apple"));
	}

	@Test
	void newFruitRegister_ShouldReturnBadRequest_WhenDataIsNotValid() throws Exception {
		FruitDTO newFruitDTO = new FruitDTO(null, null, -2);
		mockMvc.perform(post("/fruits")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newFruitDTO)))
				.andExpect(status().isBadRequest());
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
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), 5);
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
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), -5);
		Mockito.when(fruitService.updateFruitById(Mockito.eq(1L), Mockito.any(FruitDTO.class)))
				.thenReturn(newWeigthFruitDTO);
		mockMvc.perform(put("/fruits/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newWeigthFruitDTO)))
				.andExpect(status().isBadRequest());
	}
	@Test
	void updateExistingFruit_ShouldReturnNotFound_WhenDataDoesNotExist() throws Exception {
		FruitDTO newWeigthFruitDTO = new FruitDTO(fruitDTO.id(), fruitDTO.name(), 5);
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
