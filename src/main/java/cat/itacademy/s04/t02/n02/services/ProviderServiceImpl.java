package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.exceptions.DuplicateProviderName;
import cat.itacademy.s04.t02.n02.exceptions.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.exceptions.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.model.Fruit;
import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.model.Provider;
import cat.itacademy.s04.t02.n02.model.ProviderDTO;
import cat.itacademy.s04.t02.n02.repository.FruitRepository;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
	private final FruitRepository fruitRepository;
	private final ProviderRepository providerRepository;
	public ProviderServiceImpl(ProviderRepository providerRepository, FruitRepository fruitRepository){
		this.providerRepository = providerRepository;
		this.fruitRepository = fruitRepository;
	}
	public ProviderDTO providerToProviderDTO(Provider provider){
		return new ProviderDTO(
				provider.getId(),
				provider.getName(),
				provider.getCountry()
		);
	}
	public void duplicateProviderNameCheck(ProviderDTO providerDTO) {
		Provider duplicateProvider = providerRepository.findAll()
				.stream()
				.filter(provider -> provider.getName().equalsIgnoreCase(providerDTO.name()))
				.findFirst().orElse(null);
		if(duplicateProvider != null){
			throw new DuplicateProviderName("Provider with name " + providerDTO.name() + " already exists");
		}
	}
	public ProviderDTO addProvider(ProviderDTO providerDTO){
		duplicateProviderNameCheck(providerDTO);
		Provider newProvider = providerRepository.save(new Provider(
				null,
				providerDTO.name(),
				providerDTO.country()));
		return providerToProviderDTO(newProvider);
	}
	public List<ProviderDTO> getAllProviders(){
		return providerRepository.findAll()
				.stream()
				.map(this::providerToProviderDTO)
				.toList();
	}
	public ProviderDTO updateProviderById(long id, ProviderDTO newProviderDTO){
		Provider foundProvider = providerRepository.findById(id)
				.orElseThrow(()-> new ProviderNotFoundException("Provider with id " + id + " not found"));
		duplicateProviderNameCheck(newProviderDTO);
		foundProvider.setName(newProviderDTO.name());
		foundProvider.setCountry(newProviderDTO.country());
		return providerToProviderDTO(providerRepository.save(foundProvider));
	}
	public void deleteProviderById(long id){
		Provider foundProvider = providerRepository.findById(id)
				.orElseThrow(()-> new ProviderNotFoundException("Provider with id " + id + " not found"));
		if (fruitRepository.fruitsExistsByProviderId(id)) {
			throw new ProviderHasFruitsException("Cannot delete provider with id " + id + " because it has associated fruits.");
		}
		providerRepository.delete(foundProvider);
	}
}
