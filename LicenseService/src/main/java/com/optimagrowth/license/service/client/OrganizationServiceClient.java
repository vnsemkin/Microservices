package com.optimagrowth.license.service.client;

import com.optimagrowth.license.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "organization-service", configuration = FeignConfig.class)
public interface OrganizationServiceClient {
    @GetMapping("/v1/organization/{organizationId}")
    Organization getOrganization(@PathVariable("organizationId")String organizationId);
}
