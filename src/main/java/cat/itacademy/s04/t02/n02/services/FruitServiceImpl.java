package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.exceptions.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.exceptions.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.Fruit;
import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.model.Provider;
import cat.itacademy.s04.t02.n02.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FruitServiceImpl implements FruitService{
private final FruitRepository fruitRepository;
private final ProviderRepository providerRepository;

public FruitServiceImpl(FruitRepository fruitRepository, ProviderRepository providerRepository){
	this.fruitRepository = fruitRepository;
	this.providerRepository = providerRepository;
}
	public FruitDTO fruitToFruitDTO(Fruit fruit){
		return new FruitDTO(
				fruit.getId(),
				fruit.getName(),
				fruit.getWeightInKilos(),
				fruit.getProvider().getId()
		);
	}
	@Override
	public FruitDTO addFruit(FruitDTO newFruitDTO) {
		Provider provider = providerRepository.findById(newFruitDTO.providerId())
				.orElseThrow(()-> new ProviderNotFoundException("provider not found"));
		Fruit newFruit = new Fruit(
				null,
				newFruitDTO.name(),
				newFruitDTO.weightInKilos(),
				provider
		);
		Fruit savedNewFruit = fruitRepository.save(newFruit);
		return fruitToFruitDTO(savedNewFruit);
	}
	public FruitDTO getFruitById(long id) {
	Fruit foundFruit = fruitRepository.findById(id)
			.orElseThrow(()-> new FruitNotFoundException("Fruit not found with id : " + id));
	return fruitToFruitDTO(foundFruit);
	}
	public List<FruitDTO> getAllFruits() {
	return fruitRepository.findAll()
			.stream()
			.map(fruit -> fruitToFruitDTO(fruit))
			.toList();
	}
	public List<FruitDTO> getFruitsByProviderId(Long providerId) {
	return fruitRepository.findAll()
			.stream()
			.map(this::fruitToFruitDTO)
			.filter(fruit -> fruit.providerId().equals(providerId))
			.toList();
	}
	public FruitDTO updateFruitById(long id, FruitDTO newFruitDTO) {
		Fruit foundFruit = fruitRepository.findById(id)
				.orElseThrow(()-> new FruitNotFoundException("Fruit not found with id : " + id));
		foundFruit.setName(newFruitDTO.name());
		foundFruit.setWeightInKilos(newFruitDTO.weightInKilos());
		Fruit changedFruit = fruitRepository.save(foundFruit);
		return fruitToFruitDTO(changedFruit);
	}
	public void deleteFruitById(long id){
		Fruit foundFruit = fruitRepository.findById(id)
				.orElseThrow(()-> new FruitNotFoundException("Fruit not found with id : " + id));
	fruitRepository.deleteById(id);
	}
}
