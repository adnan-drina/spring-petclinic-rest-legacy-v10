package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;

/**
 * Spring Data fragment implementation of the {@link PetRepository} interface.
 *
 * <p>Implements the fragment methods (findPetTypes, save, findAll, delete) that
 * the {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPetRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(PetRepositoryImpl.class)
public class PetRepositoryImpl implements PetRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<PetType> findPetTypes() throws PersistenceException {
        return this.em.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name", PetType.class).getResultList();
    }

    @Override
    public Pet findById(int id) throws PersistenceException {
        return this.em.find(Pet.class, id);
    }

    @Override
    public void save(Pet pet) throws PersistenceException {
        if (pet.getId() == null) {
            this.em.persist(pet);
        } else {
            this.em.merge(pet);
        }
    }

    @Override
    public Collection<Pet> findAll() throws PersistenceException {
        List<Pet> pets = this.em.createQuery("SELECT pet FROM Pet pet", Pet.class).getResultList();
        return pets;
    }

    @Override
    public void delete(Pet pet) throws PersistenceException {
        String petId = pet.getId().toString();
        this.em.createQuery("DELETE FROM Visit visit WHERE pet_id = " + petId).executeUpdate();
        this.em.createQuery("DELETE FROM Pet pet WHERE id = " + petId).executeUpdate();
        if (em.contains(pet)) {
            em.remove(pet);
        }
    }
}
