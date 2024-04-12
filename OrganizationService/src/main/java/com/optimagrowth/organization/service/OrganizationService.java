package com.optimagrowth.organization.service;

import com.optimagrowth.organization.events.publisher.Publisher;
import com.optimagrowth.organization.model.Organization;
import com.optimagrowth.organization.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository repository;

    @Autowired
    @Qualifier("streamBridge")
    Publisher publisher;

    public Organization findById(String organizationId) {
        Optional<Organization> opt = repository.findById(organizationId);
        publisher.publishOrganizationChange("GET", organizationId);
        return opt.orElse(null);
    }

    public Organization create(Organization organization){
        organization.setOrganizationId( UUID.randomUUID().toString());
        organization = repository.save(organization);
        publisher.publishOrganizationChange("SAVE", organization.getOrganizationId());
        return organization;

    }

    public void update(Organization organization){
        repository.save(organization);
        publisher.publishOrganizationChange("UPDATE", organization.getOrganizationId());
    }

    public void delete(String organizationId){
        repository.deleteById(organizationId);
        publisher.publishOrganizationChange("DELETE", organizationId);
    }
}