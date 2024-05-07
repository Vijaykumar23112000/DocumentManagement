package com.magret.securedoc.repository;

import com.magret.securedoc.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialsEntity , Long> {

    Optional<CredentialsEntity> getCredentialByUserEntityId(Long userId);
}
