package cat.itacademy.s04.t02.n02.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Fruit{
		@Id
		@GeneratedValue
		Long id;
		@Setter
		String name;
		@Setter
		int weightInKilos;
		@ManyToOne
		@JoinColumn(name = "provider_id")
		Provider provider;
	public Fruit(Long id,String name, int weightInKilos, Provider provider) {
		this.id = id;
		this.name = name;
		this.weightInKilos = weightInKilos;
		this.provider = provider;
	}
}
