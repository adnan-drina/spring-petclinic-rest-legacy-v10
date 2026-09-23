/*
 * Copyright 2016-2017 the original author or authors.
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

package org.springframework.samples.petclinic.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.SpecialtyDto;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Validator;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Vitaliy Fedoriv
 */

@RestController
@RequestMapping("api/specialties")
public class SpecialtyRestController {

    private final ClinicService clinicService;

    private final SpecialtyMapper specialtyMapper;
    private final Validator validator;

    public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper, Validator validator) {
        this.clinicService = clinicService;
        this.specialtyMapper = specialtyMapper;
        this.validator = validator;
    }

    @PreAuthorize("@securityMode.disabled() or hasRole(@roles.VET_ADMIN)")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Collection<SpecialtyDto>> getAllSpecialtys() {
        Collection<SpecialtyDto> specialties = new ArrayList<SpecialtyDto>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return new ResponseEntity<Collection<SpecialtyDto>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<SpecialtyDto>>(specialties, HttpStatus.OK);
    }

    @PreAuthorize("@securityMode.disabled() or hasRole(@roles.VET_ADMIN)")
    @RequestMapping(value = "/{specialtyId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SpecialtyDto> getSpecialty(@PathVariable("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<SpecialtyDto>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<SpecialtyDto>(specialtyMapper.toSpecialtyDto(specialty), HttpStatus.OK);
    }

    @PreAuthorize("@securityMode.disabled() or hasRole(@roles.VET_ADMIN)")
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<SpecialtyDto> addSpecialty(SpecialtyDto specialtyDto, UriInfo uriInfo) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        var violations = this.validator.validate(specialtyDto);
        if (!violations.isEmpty() || (specialtyDto == null)) {
            errors.addAllErrors(violations);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<SpecialtyDto>(headers, HttpStatus.BAD_REQUEST);
        }
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        this.clinicService.saveSpecialty(specialty);
        headers.setLocation(uriInfo.getBaseUriBuilder().path("/api/specialtys/{id}").build(specialty.getId()));
        return new ResponseEntity<SpecialtyDto>(specialtyMapper.toSpecialtyDto(specialty), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("@securityMode.disabled() or hasRole(@roles.VET_ADMIN)")
    @RequestMapping(value = "/{specialtyId}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<SpecialtyDto> updateSpecialty(@PathVariable("specialtyId") int specialtyId, SpecialtyDto specialtyDto) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        var violations = this.validator.validate(specialtyDto);
        if (!violations.isEmpty() || (specialtyDto == null)) {
            errors.addAllErrors(violations);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<SpecialtyDto>(headers, HttpStatus.BAD_REQUEST);
        }
        Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return new ResponseEntity<SpecialtyDto>(HttpStatus.NOT_FOUND);
        }
        currentSpecialty.setName(specialtyDto.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return new ResponseEntity<SpecialtyDto>(specialtyMapper.toSpecialtyDto(currentSpecialty), HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@securityMode.disabled() or hasRole(@roles.VET_ADMIN)")
    @RequestMapping(value = "/{specialtyId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteSpecialty(@PathVariable("specialtyId") int specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        try {
            // The service owns the transaction: its rollback finishes before
            // a refused write is translated into the source's HTTP contract.
            this.clinicService.deleteSpecialty(specialty);
        } catch (org.hibernate.exception.ConstraintViolationException failure) {
            if (!"23503".equals(failure.getSQLState()) || failure.getConstraintName() == null) {
                throw failure;
            }
            // Preserve the legacy Spring/Hibernate error envelope for a real
            // FK failure. The constraint is database metadata, never a fixture ID.
            String operation = "could not execute statement";
            String message = operation + "; SQL [n/a]; constraint ["
                    + failure.getConstraintName() + "]; nested exception is "
                    + failure.getClass().getName() + ": " + operation;
            String body = com.fasterxml.jackson.databind.node.JsonNodeFactory.instance.objectNode()
                    .put("className", "org.springframework.dao.DataIntegrityViolationException")
                    .put("exMessage", message).toString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/plain;charset=UTF-8");
            return new ResponseEntity<String>(body, headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

}
