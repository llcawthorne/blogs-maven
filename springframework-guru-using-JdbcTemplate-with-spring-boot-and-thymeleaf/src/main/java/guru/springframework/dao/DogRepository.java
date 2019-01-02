package guru.springframework.dao;

import guru.springframework.model.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends CrudRepository<Dog, Long> {

    Dog findByName(String name);
}
