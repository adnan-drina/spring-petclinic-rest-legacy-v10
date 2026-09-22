package org.springframework.samples.petclinic.repository.springdatajpa;

import io.quarkus.arc.profile.IfBuildProfile;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;

@IfBuildProfile("spring-data-jpa")
public interface SpringDataUserRepository extends UserRepository, Repository<User, Integer>  {

}
