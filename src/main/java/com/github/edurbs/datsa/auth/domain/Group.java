package com.github.edurbs.datsa.auth.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "group_system")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group{

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(name = "group_permission",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name="permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    public boolean addPermission(Permission permission) {
        return permissions.add(permission);
    }

    public boolean removePermission(Permission permission){
        return permissions.remove(permission);
    }
}
