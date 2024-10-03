package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface LoanService {

    /**
     * Recupera los prestamos filtrando opcionalmente por título de juego, cliente, fecha.
     *
     * @param idClient PK de Clients
     * @param date fecha
     * @return {@link List} de {@link Loan}
     */
    List<Loan> find(Long idGame, Long idClient, Date date);

    List<Loan> findAll();

    Page<Loan> findPage(LoanSearchDto dto);

    /**
     * Guardar un prestamo.
     * @param dto información del prestamo.
     */
    void save(LoanDto dto) throws Exception;

    void delete(Long id) throws Exception;

    Loan get(Long id);
}
