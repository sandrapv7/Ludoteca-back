package com.ccsw.gestionclientes.clients;

import com.ccsw.gestionclientes.clients.model.Clients;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientsRepository extends CrudRepository<Clients, Long> {
    Optional<Clients> findByName(String name);
}
