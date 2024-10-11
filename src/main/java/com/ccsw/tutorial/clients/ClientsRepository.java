package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author sandra
 *
 */
public interface ClientsRepository extends CrudRepository<Clients, Long> {

    /**
     * Método para encontrar un cliente
     * @param name nombre del cliente
     * @return cliente
     */
    Optional<Clients> findByName(String name);
}
