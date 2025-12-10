package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.model.ProviderDTO;
import cat.itacademy.s04.t02.n02.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
	private final ProviderRepository providerRepository;
	public ProviderServiceImpl(ProviderRepository providerRepository){
		this.providerRepository = providerRepository;
	}
	public ProviderDTO addProvider(ProviderDTO provider){
		return null;
	}
	public List<ProviderDTO> getAllProviders(){
		return null;
	}
	public ProviderDTO updateProviderById(long id, ProviderDTO newProviderDTO){
		return null;
	}
	public List<FruitDTO> listFruitsFromProviderId(long id){
		return null;
	}
	public void deleteProviderById(long id){
	}
}
