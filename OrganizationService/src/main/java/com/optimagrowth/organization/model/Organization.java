package com.optimagrowth.organization.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "organizations")
public class Organization {
    @Id
    @JsonIgnore
    @Column(name = "organization_id", nullable = false)
    String id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "contact_name", nullable = false)
    String contactName;
    @Column(name = "contact_email", nullable = false)
    String contactEmail;
    @Column(name = "contact_phone", nullable = false)
    String contactPhone;
}
