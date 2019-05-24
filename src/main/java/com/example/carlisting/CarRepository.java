package com.example.carlisting;

import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
    void deleteById(long id);
}
