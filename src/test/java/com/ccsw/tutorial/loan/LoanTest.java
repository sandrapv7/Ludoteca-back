package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsService;
import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.common.exceptions.Exceptions;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private GameService gameService;

    @Mock
    private ClientsService clientsService;

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

    /*
    @Test
    public void testSaveThrowsExceptionWhenGameAlreadyLoanedInSelectedPeriod() {
        LoanDto dto = new LoanDto();
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.add(Calendar.DAY_OF_YEAR, 1);
        dto.setDateStart(calendarStart.getTime());
        Date d = dto.getDateStart();
        Date e = dto.getDateEnd();

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.add(Calendar.DAY_OF_YEAR, 5);
        dto.setDateEnd(calendarEnd.getTime());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientsDto clientDto = new ClientsDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        // Simulamos que hay un préstamo existente en el período
        LoanDto existingLoanDto = new LoanDto();
        ClientsDto clientDto2 = new ClientsDto();
        clientDto.setId(2L);
        dto.setClient(clientDto2);
        existingLoanDto.setGame(gameDto);
        existingLoanDto.setClient(clientDto2);
        existingLoanDto.setDateStart(d);
        existingLoanDto.setDateStart(e);

        // Simula el comportamiento de los métodos find() para devolver préstamos existentes
        when(loanRepository.find(eq(gameDto.getId()), isNull(), any(Date.class))).thenReturn(Collections.singletonList(existingLoan)); // Simula préstamo existente para la fecha de inicio

        Exception exception = assertThrows(Exceptions.class, () -> {
            loanService.save(dto);
        });

        assertEquals("El juego ya está prestado a otro cliente en el periodo de tiempo seleccionado.", exception.getMessage());
    }*/

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
        Loan loan = mock(Loan.class);
        when(loanRepository.findById(8L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> {
            loanService.delete(8L);
        });

        assertTrue(exception.getMessage().contains("Not Exists"));
        verify(loanRepository).findById(8L);
        verify(loanRepository, org.mockito.Mockito.never()).deleteById(8L);
    }
}
