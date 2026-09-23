package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

/**
 * Spring Data fragment implementation of the {@link PetTypeRepository} interface.
 *
 * <p>Implements the fragment methods (findAll, save, delete) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetTypeRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(PetTypeRepositoryImpl.class)
public class PetTypeRepositoryImpl implements PetTypeRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PetType findById(int id) throws PersistenceException {
        return this.em.find(PetType.class, id);
    }

    @Override
    public Collection<PetType> findAll() throws PersistenceException {
        List<PetType> petTypes = this.em.createQuery("SELECT ptype FROM PetType ptype", PetType.class).getResultList();
        return petTypes;
    }

    @Override
    public void save(PetType petType) throws PersistenceException {
        if (petType.getId() == null) {
            this.em.persist(petType);
        } else {
            this.em.merge(petType);
        }
    }

    @Override
    public void delete(PetType petType) throws PersistenceException {
        Integer petTypeId = petType.getId();

        List<Pet> pets = this.em.createQuery("SELECT pet FROM Pet pet WHERE type.id = " + petTypeId, Pet.class).getResultList();
        for (Pet pet : pets) {
            List<Visit> visits = pet.getVisits();
            for (Visit visit : visits) {
                this.em.createQuery("DELETE FROM Visit visit WHERE id = " + visit.getId()).executeUpdate();
            }
            this.em.createQuery("DELETE FROM Pet pet WHERE id = " + pet.getId()).executeUpdate();
        }
        this.em.createQuery("DELETE FROM PetType pettype WHERE id = " + petTypeId).executeUpdate();
    }
}
