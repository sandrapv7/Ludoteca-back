package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsServiceImpl;
import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.game.GameServiceImpl;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {
    @Mock
    private LoanRepository loanRepository;

    //Pone los simulacros creados con las anotaciones @Mock en esta instancia.
    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private ClientsServiceImpl clientsService;

    private static final Long ID_LOAN_EXISTS = 1L;
    public static final String CLIENT_NAME = "SANDRA";

    @Test
    public void saveNotExistsLoanIdShouldInsert() throws Exception {

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setName(CLIENT_NAME);

        Date inici = new Date(124, 5, 2);
        Date fi = new Date(124, 5, 10);

        GameDto gameDto = new GameDto();
        gameDto.setId(2L);
        LoanDto loanDto = new LoanDto();
        loanDto.setClient(clientsDto);
        loanDto.setDateEnd(fi);
        loanDto.setGame(gameDto);
        loanDto.setDateStart(inici);

        Game mockGame = mock(Game.class);
        Clients mockClient = mock(Clients.class);

        // Simulamos las respuestas de los servicios
        when(gameService.get(2L)).thenReturn(mockGame);
        when(clientsService.get(CLIENT_NAME)).thenReturn(mockClient);

        //Como quiero comprobar que el nombre que se ha puesto en el clientRepository es el
        //mismo que el anterior hay que capturar el cliente
        ArgumentCaptor<Loan> loan = ArgumentCaptor.forClass(Loan.class);
        loanService.save(loanDto);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());

        Loan savedLoan = loanCaptor.getValue();

        //Verificar que se llaman los servicios correctos
        verify(gameService).get();  // Verifica que se llama al servicio de juegos con el ID correcto
        verify(clientsService).get(1L);  // Verifica que se llama al servicio de clientes con el ID correcto

        //Verificar que las propiedades del DTO se copian correctamente al Loan
        assertEquals(loanDto.getDateStart(), savedLoan.getDateStart());
        assertEquals(loanDto.getDateEnd(), savedLoan.getDateEnd());
        assertEquals(mockGame, savedLoan.getGame());
        assertEquals(mockClient, savedLoan.getClient());
    }

    @Test
    public void deleteExistsLoanIdShouldDelete() throws Exception {
        Loan loan = mock(Loan.class);
        when(loanRepository.findById(ID_LOAN_EXISTS)).thenReturn(Optional.of(loan));
        this.loanService.delete(ID_LOAN_EXISTS);
        verify(loanRepository).deleteById(ID_LOAN_EXISTS);
    }

    private static final Long ID_LOAN_NOT_EXISTS = 2L;

    @Test
    public void deleteNotExistsClientIdShouldNotDelete() throws Exception {
        Loan loan = mock(Loan.class);
        when(loanRepository.findById(ID_LOAN_NOT_EXISTS)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            loanService.delete(ID_LOAN_NOT_EXISTS);
        });

        //Ver que el mensaje enviado es el correcto.
        assertTrue(exception.getMessage().contains("Not Exists"));
        //Mirar que se ha llamado al find
        verify(loanRepository).findById(ID_LOAN_NOT_EXISTS);
        //Mirar que el delete no se ha llamado ninguna vez.
        verify(loanRepository, org.mockito.Mockito.never()).deleteById(ID_LOAN_NOT_EXISTS);
    }

}