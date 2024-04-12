package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.OrganizationRedisRepository;
import com.optimagrowth.license.service.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationFeignTemplate {
    @Autowired
    OrganizationServiceClient template;

    @Autowired
    OrganizationRedisRepository redisRepository;

    public Organization getOrganization(String organizationId){
        log.info("In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());
        Organization organization = checkRedisCache(organizationId);
        if (organization != null){
            log.info("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }
        log.info("Unable to locate organization from the redis cache: {}.", organizationId);
        organization = template.getOrganization(organizationId);
        if (organization != null) {
            cacheOrganizationObject(organization);
        }
        return organization;
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return redisRepository.findById(organizationId).orElse(null);
        }catch (Exception ex){
            log.info("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}",
                organizationId, ex.getMessage());
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            log.info("Id for organization {} is {}", organization.getOrganizationId(), organization);
            redisRepository.save(organization);
        }catch (Exception ex){
            log.info("Unable to cache organization {} in Redis. Exception {}",
                organization.getOrganizationId(), ex.getMessage());
        }
    }
}
