/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import jakarta.persistence.PersistenceException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers
 * Also a placeholder for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
@Service

public class ClinicServiceImpl implements ClinicService {

    private PetRepository petRepository;
    private VetRepository vetRepository;
    private OwnerRepository ownerRepository;
    private VisitRepository visitRepository;
    private SpecialtyRepository specialtyRepository;
	private PetTypeRepository petTypeRepository;

    @Autowired
     public ClinicServiceImpl(
       		 PetRepository petRepository,
    		 VetRepository vetRepository,
    		 OwnerRepository ownerRepository,
    		 VisitRepository visitRepository,
    		 SpecialtyRepository specialtyRepository,
			 PetTypeRepository petTypeRepository) {
        this.petRepository = petRepository;
        this.vetRepository = vetRepository;
        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
        this.specialtyRepository = specialtyRepository; 
		this.petTypeRepository = petTypeRepository;
    }

	@Override
	@Transactional
	public Collection<Pet> findAllPets() throws PersistenceException {
		return petRepository.findAll();
	}

	@Override
	@Transactional
	public void deletePet(Pet pet) throws PersistenceException {
		petRepository.delete(pet);
	}

	@Override
	@Transactional
	public Visit findVisitById(int visitId) throws PersistenceException {
		Visit visit = null;
		try {
			visit = visitRepository.findById(visitId);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return visit;
	}

	@Override
	@Transactional
	public Collection<Visit> findAllVisits() throws PersistenceException {
		return visitRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteVisit(Visit visit) throws PersistenceException {
		visitRepository.delete(visit);
	}

	@Override
	@Transactional
	public Vet findVetById(int id) throws PersistenceException {
		Vet vet = null;
		try {
			vet = vetRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return vet;
	}

	@Override
	@Transactional
	public Collection<Vet> findAllVets() throws PersistenceException {
		return vetRepository.findAll();
	}

	@Override
	@Transactional
	public void saveVet(Vet vet) throws PersistenceException {
		vetRepository.save(vet);
	}

	@Override
	@Transactional
	public void deleteVet(Vet vet) throws PersistenceException {
		vetRepository.delete(vet);
	}

	@Override
	@Transactional
	public Collection<Owner> findAllOwners() throws PersistenceException {
		return ownerRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteOwner(Owner owner) throws PersistenceException {
		ownerRepository.delete(owner);
	}

	@Override
	@Transactional
	public PetType findPetTypeById(int petTypeId) {
		PetType petType = null;
		try {
			petType = petTypeRepository.findById(petTypeId);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return petType;
	}

	@Override
	@Transactional
	public Collection<PetType> findAllPetTypes() throws PersistenceException {
		return petTypeRepository.findAll();
	}

	@Override
	@Transactional
	public void savePetType(PetType petType) throws PersistenceException {
		petTypeRepository.save(petType);
	}

	@Override
	@Transactional
	public void deletePetType(PetType petType) throws PersistenceException {
		petTypeRepository.delete(petType);
	}

	@Override
	@Transactional
	public Specialty findSpecialtyById(int specialtyId) {
		Specialty specialty = null;
		try {
			specialty = specialtyRepository.findById(specialtyId);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return specialty;
	}

	@Override
	@Transactional
	public Collection<Specialty> findAllSpecialties() throws PersistenceException {
		return specialtyRepository.findAll();
	}

	@Override
	@Transactional
	public void saveSpecialty(Specialty specialty) throws PersistenceException {
		specialtyRepository.save(specialty);
	}

	@Override
	@Transactional
	public void deleteSpecialty(Specialty specialty) throws PersistenceException {
		specialtyRepository.delete(specialty);
	}

	@Override
	@Transactional
	public Collection<PetType> findPetTypes() throws PersistenceException {
		return petRepository.findPetTypes();
	}

	@Override
	@Transactional
	public Owner findOwnerById(int id) throws PersistenceException {
		Owner owner = null;
		try {
			owner = ownerRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return owner;
	}

	@Override
	@Transactional
	public Pet findPetById(int id) throws PersistenceException {
		Pet pet = null;
		try {
			pet = petRepository.findById(id);
		} catch (PersistenceException e) {
		// just ignore not found exceptions for Jdbc/Jpa realization
			return null;
		}
		return pet;
	}

	@Override
	@Transactional
	public void savePet(Pet pet) throws PersistenceException {
		petRepository.save(pet);
		
	}

	@Override
	@Transactional
	public void saveVisit(Visit visit) throws PersistenceException {
		visitRepository.save(visit);
		
	}

	@Override
	@Transactional
    @Cacheable(value = "vets")
	public Collection<Vet> findVets() throws PersistenceException {
		return vetRepository.findAll();
	}

	@Override
	@Transactional
	public void saveOwner(Owner owner) throws PersistenceException {
		ownerRepository.save(owner);
		
	}

	@Override
	@Transactional
	public Collection<Owner> findOwnerByLastName(String lastName) throws PersistenceException {
		return ownerRepository.findByLastName(lastName);
	}

	@Override
	@Transactional
	public Collection<Visit> findVisitsByPetId(int petId) {
		return visitRepository.findByPetId(petId);
	}
	
	


}
