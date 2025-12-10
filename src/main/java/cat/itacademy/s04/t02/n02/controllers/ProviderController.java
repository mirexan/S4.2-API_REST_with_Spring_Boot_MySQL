package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.model.ProviderDTO;
import cat.itacademy.s04.t02.n02.services.ProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {
	private final ProviderService providerService;
	public ProviderController(ProviderService providerService) {
		this.providerService = providerService;
	}
	@PostMapping
	public ResponseEntity<ProviderDTO> createProviderController(@Valid @RequestBody ProviderDTO providerDTO){
		ProviderDTO savedProvider = providerService.addProvider(providerDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProvider);
	}
	@GetMapping
	public ResponseEntity<List<ProviderDTO>> getAllProvidersController(){
		return ResponseEntity.status(HttpStatus.OK).body(providerService.getAllProviders());
	}
	@PutMapping("/{id}")
	public ResponseEntity<ProviderDTO> updateProviderController(@PathVariable long id, @Valid @RequestBody ProviderDTO providerDTO){
		ProviderDTO updatedProviderDTO = providerService.updateProviderById(id, providerDTO);
		return ResponseEntity.ok(updatedProviderDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ProviderDTO> deleteProviderController(@PathVariable long id){
		providerService.deleteProviderById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
