package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Typed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import org.springframework.samples.petclinic.model.Visit;

/**
 * Spring Data fragment implementation of the {@link VisitRepository} interface.
 *
 * <p>Implements the fragment methods (save, findAll, delete) that the
 * {@link org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVisitRepository}
 * interface delegates to this class.
 */
@ApplicationScoped
@Typed(VisitRepositoryImpl.class)
public class VisitRepositoryImpl implements VisitRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void save(Visit visit) throws PersistenceException {
        if (visit.getId() == null) {
            this.em.persist(visit);
        } else {
            this.em.merge(visit);
        }
    }

    @Override
    public List<Visit> findByPetId(Integer petId) {
        TypedQuery<Visit> query = this.em.createQuery("SELECT v FROM Visit v WHERE v.pet.id = :id", Visit.class);
        query.setParameter("id", petId);
        return query.getResultList();
    }

    @Override
    public Visit findById(int id) throws PersistenceException {
        return this.em.find(Visit.class, id);
    }

    @Override
    public Collection<Visit> findAll() throws PersistenceException {
        List<Visit> visits = this.em.createQuery("SELECT v FROM Visit v", Visit.class).getResultList();
        return visits;
    }

    @Override
    public void delete(Visit visit) throws PersistenceException {
        this.em.remove(this.em.contains(visit) ? visit : this.em.merge(visit));
    }
}
