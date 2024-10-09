package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsService;
import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.common.exceptions.Exceptions;
import com.ccsw.tutorial.game.GameService;
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
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientsService clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void testFindAllParametersNull() {
        when(loanRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        List<Loan> loans = loanService.find(null, null, null);
        verify(loanRepository, times(1)).findAll(any(Specification.class));
        assertTrue(loans.isEmpty());
        assertEquals(0, loans.size());
    }

    @Test
    public void testFindWithIdGame() {
        Loan loan = new Loan();
        loan.setId(1L);
        Game game = new Game();
        game.setId(1L);
        loan.setGame(game);
        when(loanRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(loan));
        List<Loan> loans = loanService.find(1L, null, null);
        verify(loanRepository, times(1)).findAll(any(Specification.class));
        assertEquals(1, loans.size());
        assertEquals(1L, loans.get(0).getId());
    }

    @Test
    public void testFindWithIdClient() {
        Loan loan = new Loan();
        loan.setId(2L);
        Clients client = new Clients();
        client.setId(2L);
        loan.setClient(client);
        when(loanRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(loan));
        List<Loan> loans = loanService.find(null, 2L, null);
        verify(loanRepository, times(1)).findAll(any(Specification.class));
        assertEquals(1, loans.size());
        assertEquals(2L, loans.get(0).getId());
    }

    @Test
    public void testFindWithDate() {
        Loan loan = new Loan();
        loan.setId(3L);
        Date date = new Date();
        loan.setDateStart(date);
        when(loanRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(loan));
        List<Loan> loans = loanService.find(null, null, date);
        verify(loanRepository, times(1)).findAll(any(Specification.class));
        assertEquals(1, loans.size());
        assertEquals(3L, loans.get(0).getId());
    }

    @Test
    public void testFindWithAllParameters() {
        Loan loan = new Loan();
        loan.setId(4L);
        Game game = new Game();
        game.setId(1L);
        loan.setGame(game);
        Clients client = new Clients();
        client.setId(2L);
        loan.setClient(client);
        Date date = new Date();
        loan.setDateStart(date);

        when(loanRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(loan));
        List<Loan> loans = loanService.find(1L, 2L, date);
        verify(loanRepository, times(1)).findAll(any(Specification.class));

        assertEquals(1, loans.size());
    }

    @Test
    public void testSaveThrowsExceptionWhenEndDateBeforeStartDate() {
        LoanDto dto = new LoanDto();
        dto.setDateStart(new Date(System.currentTimeMillis() + 86400000)); // mañana
        dto.setDateEnd(new Date(System.currentTimeMillis())); // hoy

        Exception exception = assertThrows(Exceptions.class, () -> {
            loanService.save(dto);
        });

        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio", exception.getMessage());
    }

    @Test
    public void testSaveThrowsExceptionWhenLoanPeriodExceeds14Days() {
        LoanDto dto = new LoanDto();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 16);
        dto.setDateStart(new Date());
        dto.setDateEnd(calendar.getTime());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setId(1L);
        dto.setClient(clientsDto);

        Exception exception = assertThrows(Exceptions.class, () -> {
            loanService.save(dto);
        });

        assertEquals("El periodo de préstamo no puede ser mayor a 14 días", exception.getMessage());
    }

    @Test
    public void testSaveThrowsExceptionWhenGameAlreadyLoanedInSelectedPeriod() {
        LoanDto dto = new LoanDto();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        dto.setDateStart(new Date());
        dto.setDateEnd(calendar.getTime());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setId(1L);
        dto.setClient(clientsDto);

        List<Loan> existingLoans = new ArrayList<>();
        existingLoans.add(new Loan());
        when(loanService.find(gameDto.getId(), null, any())).thenReturn(existingLoans);
        Exception exception = assertThrows(Exceptions.class, () -> loanService.save(dto));
        assertEquals("El juego ya está prestado a otro cliente en el periodo de tiempo seleccionado.", exception.getMessage());

    }

    @Test
    public void testSaveLoanSuccess() throws Exception {
        LoanDto dto = new LoanDto();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        dto.setDateStart(new Date());
        dto.setDateEnd(calendar.getTime());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientsDto clientsDto = new ClientsDto();
        clientsDto.setId(1L);
        dto.setClient(clientsDto);

        Game game = new Game();
        game.setId(1L);
        when(gameService.get(gameDto.getId())).thenReturn(game);

        Clients client = new Clients();
        client.setId(1L);
        when(clientService.get(clientsDto.getId())).thenReturn(client);

        ArgumentCaptor<Loan> savedLoan = ArgumentCaptor.forClass(Loan.class);
        loanService.save(dto);
        verify(loanRepository).save(savedLoan.capture());

        assertNotNull(savedLoan.getValue());
        assertEquals(1L, savedLoan.getValue().getGame().getId());
        assertEquals(1L, savedLoan.getValue().getClient().getId());
        assertEquals(dto.getDateStart(), savedLoan.getValue().getDateStart());
        assertEquals(dto.getDateEnd(), savedLoan.getValue().getDateEnd());
    }

    @Test
    public void getExistsLoanIdShouldReturnLoan() {
        Loan loan = mock(Loan.class);
        when(loan.getId()).thenReturn(1L);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Loan loanResponse = loanService.get(1L);
        assertNotNull(loanResponse);
        assertEquals(1L, loanResponse.getId());
    }

    @Test
    public void getNotExistsLoanIdShouldReturnNull() {
        when(loanRepository.findById(8L)).thenReturn(Optional.empty());
        Loan loan = loanService.get(8L);
        assertNull(loan);
    }

    @Test
    public void deleteExistsClientIdShouldDelete() throws Exception {
        Loan loan = mock(Loan.class);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        loanService.delete(1L);
        verify(loanRepository).deleteById(1L);
    }

    @Test
    public void deleteNotExistsClientIdShouldNotDelete() throws Exception {
        when(loanRepository.findById(8L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            loanService.delete(8L);
        });

        assertTrue(exception.getMessage().contains("Not Exists"));
        verify(loanRepository).findById(8L);
        verify(loanRepository, org.mockito.Mockito.never()).deleteById(8L);
    }
}
