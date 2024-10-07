package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author sandra
 */
public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    /**
     * Método para recuperar un listado paginado de {@link Loan} que cumplen con la especificación dada.
     *
     * @param pageable pageable
     * @param spec Especificación que deben cumplir los préstamos
     * */
    @Override
    @EntityGraph(attributePaths = { "game", "clients" })
    Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

}
