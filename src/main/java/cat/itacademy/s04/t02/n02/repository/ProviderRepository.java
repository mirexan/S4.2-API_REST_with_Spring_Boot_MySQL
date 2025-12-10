package cat.itacademy.s04.t02.n02.repository;

import cat.itacademy.s04.t02.n02.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
