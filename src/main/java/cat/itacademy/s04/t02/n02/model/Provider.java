package cat.itacademy.s04.t02.n02.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Provider {
	@Id
	@GeneratedValue
	private Long id;
	@Setter
	private String name;
	@Setter
	private String country;

	public Provider(Long id, String name, String country) {
		this.id = id;
		this.name = name;
		this.country = country;
	}
}
