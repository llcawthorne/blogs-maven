package guru.springframework.service;

import java.util.Date;
import java.util.List;

public interface DogService {

    void addDog(String name, Date rescued, Boolean vaccinated);

    void deleteDog(String name, Long id);

    List<String> atRiskDogs(Date rescued);

    long getGeneratedKey(String name, Date rescued, Boolean vaccinated);
}
