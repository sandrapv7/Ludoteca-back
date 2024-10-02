package com.ccsw.tutorial.clients;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.common.exceptions.ExcepcioExistsName;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientsServiceImpl implements ClientsService {

    @Autowired
    ClientsRepository clientsRepository;

    //Responde con una Entity
    @Override
    public List<Clients> findAll() {
        return (List<Clients>) this.clientsRepository.findAll();
    }

    @Override
    public void save(Long id, ClientsDto dto) throws Exception {
        if (this.clientsRepository.findByName(dto.getName()).isPresent()) {
            throw new ExcepcioExistsName();
        }
        Clients client;
        if (id == null) {
            client = new Clients();

        } else {
            client = this.get(id);
        }

        client.setName(dto.getName());
        this.clientsRepository.save(client);
    }

    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not Exists");
        }
        this.clientsRepository.deleteById(id);
    }

    @Override
    public Clients get(Long id) {
        return this.clientsRepository.findById(id).orElse(null);
    }
}
