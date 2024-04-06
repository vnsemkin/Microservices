package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.exception.LicenseNotFoundException;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationServiceClient;
import com.optimagrowth.license.service.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

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
            throw new LicenseNotFoundException(String.format("License with id %s not found", licenseId));
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

    public String deleteLicense(String organizationId, String licenseId) {
        String responseMessage;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        String messageTemplate = messageSource
            .getMessage("license.delete.message", null, LocaleContextHolder.getLocale());
        responseMessage = String.format(messageTemplate,
            licenseId, organizationId);
        return responseMessage;

    }
}
