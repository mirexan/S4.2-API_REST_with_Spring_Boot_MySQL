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
public class Fruit{
		@Id
		@GeneratedValue
		Long id;
		@Setter
		String name;
		@Setter
		int weightInKilos;
	public Fruit(Long id,String name, int weightInKilos) {
		this.id = id;
		this.name = name;
		this.weightInKilos = weightInKilos;
	}
}
