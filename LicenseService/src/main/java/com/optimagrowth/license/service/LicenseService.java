package com.optimagrowth.license.service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.optimagrowth.license.configuration.ServiceConfig;
import com.optimagrowth.license.exception.LicenseNotFoundException;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.service.client.OrganizationServiceClient;
import com.optimagrowth.license.service.utils.UserContextHolder;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.repository.LicenseRepository;

@Slf4j
@Service
public class LicenseService {
    @Autowired
    private OrganizationServiceClient organizationServiceClient;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private ServiceConfig config;

    public List<License> getAllLicense() {
        return (List<License>) licenseRepository.findAll();
    }


    public License getLicense(String licenseId, String organizationId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(String.format(messageSource
                    .getMessage("license.search.error.message", null, null)
                , licenseId, organizationId));
        }
        Organization organization = getOrganizationInfo(organizationId);
        if (organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    //	@CircuitBreaker(name = "licenseService")
//	@RateLimiter(name = "licenseService")
//	@Retry(name = "retryLicenseService")
//	@Bulkhead(name = "bulkheadLicenseService", type= Bulkhead.Type.THREADPOOL)
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        log.debug("getLicensesByOrganization Correlation id: {}",
            UserContextHolder.getContext().getCorrelationId());
//        randomlyRunLong();
        List<License> byOrganizationId = licenseRepository.findByOrganizationId(organizationId);
        if (byOrganizationId.isEmpty()) {
            throw new LicenseNotFoundException("License for organizationId : " + organizationId + " not found.");
        }
        return byOrganizationId;
    }

    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;
        if (randomNum == 3) sleep();
    }

    private void sleep() throws TimeoutException {
        try {
            System.out.println("Sleep");
            Thread.sleep(5000);
            throw new java.util.concurrent.TimeoutException();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private Organization getOrganizationInfo(String organizationId) {
        return organizationServiceClient.getOrganization(organizationId);
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messageSource
            .getMessage("license.delete.message", null, null), licenseId);
        return responseMessage;

    }
}
