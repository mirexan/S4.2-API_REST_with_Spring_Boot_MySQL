package cat.itacademy.s04.t02.n02.controllers;

import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.services.FruitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fruits")
public class FruitController {
	private final FruitService fruitService;

	public FruitController(FruitService newFruitService){
		this.fruitService = newFruitService;
	}

	@PostMapping
	public ResponseEntity<FruitDTO> createFruitController(@Valid @RequestBody FruitDTO newFruitDTO){
		FruitDTO savedFruit = fruitService.addFruit(newFruitDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedFruit);
	}
	@GetMapping
	public ResponseEntity<List<FruitDTO>> getAllFruitsController(@RequestParam(name = "providerId",required = false) Long providerId){
		if(providerId!=null){
			return ResponseEntity.ok(fruitService.getFruitsByProviderId(providerId));
		}
		return ResponseEntity.status(HttpStatus.OK).body(fruitService.getAllFruits());
	}
	@GetMapping("/{id}")
	public ResponseEntity<FruitDTO> getFruitByIdController(@PathVariable long id){
		return ResponseEntity.status(HttpStatus.OK).body(fruitService.getFruitById(id));
	}
	@PutMapping("/{id}")
	public ResponseEntity<FruitDTO> updateFruit(@PathVariable long id, @Valid @RequestBody FruitDTO newFruitDTO){
		return ResponseEntity.status(HttpStatus.OK).body(fruitService.updateFruitById(id, newFruitDTO));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<FruitDTO> deleteFruitById(@PathVariable long id){
		fruitService.deleteFruitById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
