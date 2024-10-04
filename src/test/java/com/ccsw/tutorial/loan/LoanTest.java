package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsServiceImpl;
import com.ccsw.tutorial.game.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    public static final String CLIENT_NAME = "Sandra";

    @Test
    public void saveNotExistsLoanIdShouldInsert() throws Exception {

    }

    @Test
    public void saveStartDateafterEndDateShouldNotInsert() throws Exception {

    }

    @Test
    public void deleteExistsLoanIdShouldDelete() throws Exception {

    }

    private static final Long ID_LOAN_NOT_EXISTS = 2L;

    @Test
    public void deleteNotExistsClientIdShouldNotDelete() throws Exception {

    }

}