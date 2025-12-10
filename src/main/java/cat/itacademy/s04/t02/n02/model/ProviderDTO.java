package cat.itacademy.s04.t02.n02.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProviderDTO(
		Long id,
		@NotNull
		@NotBlank
		String name,
		@NotNull
		@NotBlank
		String country
) {
}
