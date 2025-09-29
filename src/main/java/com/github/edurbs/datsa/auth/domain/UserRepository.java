package com.github.edurbs.datsa.auth.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long>{

    Optional<MyUser> findByEmail(String email);
}