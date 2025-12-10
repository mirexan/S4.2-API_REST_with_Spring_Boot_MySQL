package cat.itacademy.s04.t02.n02.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FruitDTO (
		Long id,
		@NotBlank(message = "Name can't be blank")
		@NotNull(message = "Name can't be null")
		String name,
		@Positive(message = "weight has to be bigger than zero")
		int weightInKilos
){
}
