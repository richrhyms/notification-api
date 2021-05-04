package com.richotaru.notificationapi.dao;

import com.richotaru.notificationapi.entity.ClientAccount;
import com.richotaru.notificationapi.enumeration.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {

    @Query("select ca from ClientAccount ca" +
            " where lower(ca.apiKey) = lower(?1) AND ca.status =?2 ")
    Optional<ClientAccount> findByApiKeyAndStatus(String apiKey, GenericStatusConstant constant);

    @Query("select ca from ClientAccount ca" +
            " where lower(ca.displayName) = lower(?1) AND ca.status =?2 ")
    Optional<ClientAccount> findByDisplayNameAndStatus(String displayName, GenericStatusConstant constant);
}
