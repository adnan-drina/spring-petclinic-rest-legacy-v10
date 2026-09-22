package org.springframework.samples.petclinic.repository;

import jakarta.persistence.PersistenceException;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository {

    void save(User user) throws PersistenceException;
}
