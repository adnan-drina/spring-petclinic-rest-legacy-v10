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

import jakarta.persistence.PersistenceException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;


/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
public interface ClinicService {

	Pet findPetById(int id) throws PersistenceException;
	Collection<Pet> findAllPets() throws PersistenceException;
	void savePet(Pet pet) throws PersistenceException;
	void deletePet(Pet pet) throws PersistenceException;

	Collection<Visit> findVisitsByPetId(int petId);
	Visit findVisitById(int visitId) throws PersistenceException;
	Collection<Visit> findAllVisits() throws PersistenceException;
	void saveVisit(Visit visit) throws PersistenceException;
	void deleteVisit(Visit visit) throws PersistenceException;
	
	Vet findVetById(int id) throws PersistenceException;
	Collection<Vet> findVets() throws PersistenceException;
	Collection<Vet> findAllVets() throws PersistenceException;
	void saveVet(Vet vet) throws PersistenceException;
	void deleteVet(Vet vet) throws PersistenceException;
	
	Owner findOwnerById(int id) throws PersistenceException;
	Collection<Owner> findAllOwners() throws PersistenceException;
	void saveOwner(Owner owner) throws PersistenceException;
	void deleteOwner(Owner owner) throws PersistenceException;
	Collection<Owner> findOwnerByLastName(String lastName) throws PersistenceException;

	PetType findPetTypeById(int petTypeId);
	Collection<PetType> findAllPetTypes() throws PersistenceException;
	Collection<PetType> findPetTypes() throws PersistenceException;
	void savePetType(PetType petType) throws PersistenceException;
	void deletePetType(PetType petType) throws PersistenceException;
	
	Specialty findSpecialtyById(int specialtyId);
	Collection<Specialty> findAllSpecialties() throws PersistenceException;
	void saveSpecialty(Specialty specialty) throws PersistenceException;
	void deleteSpecialty(Specialty specialty) throws PersistenceException;

}
