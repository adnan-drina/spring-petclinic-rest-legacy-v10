package org.springframework.samples.petclinic.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import org.springframework.samples.petclinic.model.User;

/**
 * Spring Data fragment implementation of the {@link UserRepository} interface.
 *
 * <p>Implements the fragment method (save) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataUserRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(UserRepositoryImpl.class)
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(User user) throws PersistenceException {
        if (this.em.find(User.class, user.getUsername()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }
}
