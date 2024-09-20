package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;

import java.util.Date;
import java.util.List;

public interface LoanService {

    /**
     * Recupera los prestamos filtrando opcionalmente por título de juego, cliente, fecha.
     *
     * @param title título del juego
     * @param idClients PK de Clients
     * @param date fecha
     * @return {@link List} de {@link Game}
     */
    List<Loan> find(String title, Long idClients, Date date);

    /**
     * Guardar un prestamo.
     * @param id PK del prestamo
     * @param dto información del prestamo.
     */
    void save(Long id, LoanDto dto);

    void delete(Long id) throws Exception;
}
