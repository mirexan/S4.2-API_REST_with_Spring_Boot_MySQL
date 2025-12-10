package cat.itacademy.s04.t02.n02.services;

import cat.itacademy.s04.t02.n02.model.FruitDTO;
import cat.itacademy.s04.t02.n02.model.ProviderDTO;

import java.util.List;

public interface ProviderService {
	ProviderDTO addProvider(ProviderDTO provider);
	List<ProviderDTO> getAllProviders();
	ProviderDTO updateProviderById(long id, ProviderDTO newProviderDTO);
	void deleteProviderById(long id);
}
