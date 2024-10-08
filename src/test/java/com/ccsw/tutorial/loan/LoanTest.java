package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

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
