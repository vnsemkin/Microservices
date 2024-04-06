package com.optimagrowth.organization.events.publisher;

public interface Publisher {
    void publishOrganizationChange(String action, String organizationId);
}
