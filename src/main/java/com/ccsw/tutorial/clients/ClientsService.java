package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;

import java.util.List;

public interface ClientsService {

    /**
     * Método para recuperar todas los {@link Clients}
     *
     * @return {@link List} de {@link Clients}
     */
    //El servicio responde con una Entity y el controlador ya se encargará de transformarlo a Dto.
    List<Clients> findAll();

    /**
     * Método para crear o actualizar un {@link Clients}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    //Los datos del controlador se pasan como dto.
    void save(Long id, ClientsDto dto) throws Exception;

    /**
     * Método para borrar un {@link Clients}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}
