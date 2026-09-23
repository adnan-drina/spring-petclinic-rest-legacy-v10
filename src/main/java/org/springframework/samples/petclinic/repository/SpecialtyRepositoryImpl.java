package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import org.springframework.samples.petclinic.model.Specialty;

/**
 * Spring Data fragment implementation of the {@link SpecialtyRepository} interface.
 *
 * <p>Implements the fragment methods (findAll, save, delete) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataSpecialtyRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(SpecialtyRepositoryImpl.class)
public class SpecialtyRepositoryImpl implements SpecialtyRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Specialty findById(int id) throws PersistenceException {
        return this.em.find(Specialty.class, id);
    }

    @Override
    public Collection<Specialty> findAll() throws PersistenceException {
        List<Specialty> specialties = this.em.createQuery("SELECT s FROM Specialty s", Specialty.class).getResultList();
        return specialties;
    }

    @Override
    public void save(Specialty specialty) throws PersistenceException {
        if (specialty.getId() == null) {
            this.em.persist(specialty);
        } else {
            this.em.merge(specialty);
        }
    }

    @Override
    public void delete(Specialty specialty) throws PersistenceException {
        this.em.remove(this.em.contains(specialty) ? specialty : this.em.merge(specialty));
        Integer specId = specialty.getId();
        this.em.createNativeQuery("DELETE FROM vet_specialties WHERE specialty_id = " + specId).executeUpdate();
        this.em.createQuery("DELETE FROM Specialty specialty WHERE id = " + specId).executeUpdate();
    }
}
