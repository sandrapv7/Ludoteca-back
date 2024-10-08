package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.common.exceptions.Exceptions;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

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
        when(loanService.find(gameDto.getId(), null, any())).thenReturn(existingLoans);

        existingLoans.add(new Loan()); // Simulate existing loan
        assertThrows(Exceptions.class, () -> loanService.save(dto));
    }

    @Test
    public void testSaveThrowsExceptionWhenClientAlreadyLoaned2Times() {
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
        existingLoans.add(new Loan());
        when(loanService.find(null, clientsDto.getId(), any())).thenReturn(existingLoans);
        assertThrows(Exceptions.class, () -> loanService.save(dto));
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
