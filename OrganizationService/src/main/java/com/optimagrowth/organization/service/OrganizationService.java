package com.optimagrowth.organization.service;

import com.optimagrowth.organization.events.publisher.OrganizationChangePublisher;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository repository;

    @Autowired
    OrganizationChangePublisher organizationChangePublisher;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        organizationChangePublisher.publishOrganizationChange("GET", organizationId);
        return (opt.isPresent()) ? opt.get() : null;
    }

    public Organization create(Organization organization){
        organization.setId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        organizationChangePublisher.publishOrganizationChange("SAVE", organization.getId());
        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
        organizationChangePublisher.publishOrganizationChange("UPDATE", organization.getId());
    }

    public void delete(String organizationId){
        repository.deleteById(organizationId);
        organizationChangePublisher.publishOrganizationChange("DELETE", organizationId);
    }
}