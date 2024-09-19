package com.ccsw.gestionclientes.clients;

import com.ccsw.gestionclientes.clients.model.Clients;
import com.ccsw.gestionclientes.clients.model.ClientsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientsTest {

    @Mock
    private ClientsRepository clientsRepository;

    //Pone los simulacros creados con las anotaciones @Mock en esta instancia.
    @InjectMocks
    private ClientsServiceImpl clientsService;

    @Test
    public void findAllShouldReturnAllClients() {
        List<Clients> list = new ArrayList<>();
        //mock se usa para crear un objeto simulado de clients.
        list.add(mock(Clients.class));

        //Como clientsService hace una llamada a clientsRepository tengo que decirle lo que me tiene que devolver.
        when(clientsRepository.findAll()).thenReturn(list);
        List<Clients> clients = clientsService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String CLIENT_NAME = "SANDRA";

    @Test
    public void saveNotExistsClientIdShouldInsert() throws Exception {

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(CLIENT_NAME);

        //Como quiero comprobar que el nombre que se ha puesto en el clientRepository es el
        //mismo que el anterior hay que capturar el cliente
        ArgumentCaptor<Clients> client = ArgumentCaptor.forClass(Clients.class);
        clientsService.save(null, clientsDto);

        //Mirar que el save se ha llamado con los argumentos esperados y se capturan esos argumentos.
        verify(clientsRepository).save(client.capture());
        assertEquals(CLIENT_NAME, client.getValue().getName());
    }

    private static final Long ID_CLIENT_EXISTS = 1L;

    @Test
    public void saveExistsClientIdShouldUpdate() throws Exception {
        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(CLIENT_NAME);
        Clients client = mock(Clients.class);
        when(clientsRepository.findById(ID_CLIENT_EXISTS)).thenReturn(Optional.of(client));
        clientsService.save(ID_CLIENT_EXISTS, clientsDto);
        verify(clientsRepository).save(client);
    }

    @Test
    public void saveExistsNameShouldThrowException() throws Exception {
        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(CLIENT_NAME);
        Clients client = mock(Clients.class);
        when(clientsRepository.findByName(CLIENT_NAME)).thenReturn(Optional.of(client));
        Exception exception = assertThrows(Exception.class, () -> {
            clientsService.save(ID_CLIENT_EXISTS, clientsDto);
        });

        assertTrue(exception.getMessage().contains("Ja existeix un usuari amb aquest nom"));
        verify(clientsRepository).findByName(CLIENT_NAME);
        verify(clientsRepository, org.mockito.Mockito.never()).save(client);
    }

    @Test
    public void deleteExistsClientIdShouldDelete() throws Exception {
        Clients client = mock(Clients.class);
        when(clientsRepository.findById(ID_CLIENT_EXISTS)).thenReturn(Optional.of(client));
        clientsService.delete(ID_CLIENT_EXISTS);
        verify(clientsRepository).deleteById(ID_CLIENT_EXISTS);
    }

    private static final Long ID_CLIENT_NOT_EXISTS = 2L;

    @Test
    public void deleteNotExistsClientIdShouldNotDelete() throws Exception {
        Clients client = mock(Clients.class);
        when(clientsRepository.findById(ID_CLIENT_NOT_EXISTS)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            clientsService.delete(ID_CLIENT_NOT_EXISTS);
        });

        //Ver que el mensaje enviado es el correcto.
        assertTrue(exception.getMessage().contains("Not Exists"));
        //Mirar que se ha llamado al find
        verify(clientsRepository).findById(ID_CLIENT_NOT_EXISTS);
        //Mirar que el delete no se ha llamado ninguna vez.
        verify(clientsRepository, org.mockito.Mockito.never()).deleteById(ID_CLIENT_NOT_EXISTS);
    }
}
