package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import org.springframework.samples.petclinic.model.Owner;

/**
 * Spring Data fragment implementation of the {@link OwnerRepository} interface.
 *
 * <p>Implements the fragment methods (save, findAll, delete) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataOwnerRepository}
 * interface delegates to this class. The {@code @Query} methods declared on
 * the Spring Data interface are handled by the generated repository proxy.
 */
@ApplicationScoped
@Typed(OwnerRepositoryImpl.class)
public class OwnerRepositoryImpl implements OwnerRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Owner> findByLastName(String lastName) throws PersistenceException {
        TypedQuery<Owner> query = this.em.createQuery(
                "SELECT DISTINCT owner FROM Owner owner LEFT JOIN FETCH owner.pets WHERE owner.lastName LIKE :lastName", Owner.class);
        query.setParameter("lastName", lastName + "%");
        return query.getResultList();
    }

    @Override
    public Owner findById(int id) throws PersistenceException {
        TypedQuery<Owner> query = this.em.createQuery(
                "SELECT owner FROM Owner owner LEFT JOIN FETCH owner.pets WHERE owner.id = :id", Owner.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void save(Owner owner) throws PersistenceException {
        if (owner.getId() == null) {
            this.em.persist(owner);
        } else {
            this.em.merge(owner);
        }
    }

    @Override
    public Collection<Owner> findAll() throws PersistenceException {
        List<Owner> owners = this.em.createQuery("SELECT owner FROM Owner owner", Owner.class).getResultList();
        return owners;
    }

    @Override
    public void delete(Owner owner) throws PersistenceException {
        this.em.remove(this.em.contains(owner) ? owner : this.em.merge(owner));
    }
}
