package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author sandra
 */
public interface LoanService {

    /**
     * Recupera un listado de prestamos filtrando opcionalmente por juego, cliente o fecha.
     * @param idGame PK del juego
     * @param idClient PK del cliente
     * @param date fecha
     * @return {@link List} de {@link Loan}
     */
    List<Loan> find(Long idGame, Long idClient, Date date);

    Page<Loan> findLoans(Long idGame, Long idClient, Date date, LoanSearchDto dto);

    /**
     * Guardar un prestamo.
     * @param dto información del prestamo.
     */
    void save(LoanDto dto) throws Exception;

    /**
     * Eliminar un prestamo.
     * @param id PK del prestamo.
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera una {@link Loan} a partir de su ID
     *
     * @param id PK de la entidad
     * @return {@link Loan}
     */
    Loan get(Long id);
}