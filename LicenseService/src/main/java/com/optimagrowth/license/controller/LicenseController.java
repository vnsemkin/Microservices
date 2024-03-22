package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;
import com.optimagrowth.license.service.utils.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/organization/{organizationId}/license")
public class LicenseController {
    private final LicenseService licenseService;

@GetMapping(value="/")
public List<License> getLicenses( @PathVariable("organizationId") String organizationId) throws TimeoutException {
    log.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
    log.debug("License was requested");
    return licenseService.getLicensesByOrganization(organizationId);
}

    @GetMapping(value = "/{licenseId}")
    public ResponseEntity<License> getLicense(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {
        License license = licenseService.getLicense(licenseId, organizationId);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<License> updateLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request));
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License request) {
        return ResponseEntity.ok(licenseService.createLicense(request));
    }

    @DeleteMapping(value = "/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
    }
}

