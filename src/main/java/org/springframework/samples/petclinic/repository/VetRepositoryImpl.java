package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import org.springframework.samples.petclinic.model.Vet;

/**
 * Spring Data fragment implementation of the {@link VetRepository} interface.
 *
 * <p>Implements the fragment methods (findAll, save, delete) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVetRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(VetRepositoryImpl.class)
public class VetRepositoryImpl implements VetRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Vet findById(int id) throws PersistenceException {
        return this.em.find(Vet.class, id);
    }

    @Override
    public Collection<Vet> findAll() throws PersistenceException {
        List<Vet> vets = this.em.createQuery("SELECT vet FROM Vet vet", Vet.class).getResultList();
        return vets;
    }

    @Override
    public void save(Vet vet) throws PersistenceException {
        if (vet.getId() == null) {
            this.em.persist(vet);
        } else {
            this.em.merge(vet);
        }
    }

    @Override
    public void delete(Vet vet) throws PersistenceException {
        this.em.remove(this.em.contains(vet) ? vet : this.em.merge(vet));
    }
}
