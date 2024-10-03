package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientsService clientsService;

    @Autowired
    GameService gameService;

    @Override
    public List<Loan> find(Long idGame, Long idClient, Date date) {

        LoanSpecification gameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", idGame));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("clients.id", ":", idClient));
        LoanSpecification dateStartSpec = new LoanSpecification(new SearchCriteria("dateStart", "<=", date));
        LoanSpecification dateEndSpec = new LoanSpecification(new SearchCriteria("dateEnd", ">=", date));

        // Combina las especificaciones teniendo en cuenta la navegación de propiedades
        Specification<Loan> spec = Specification.where(gameSpec).and(clientSpec).and(dateStartSpec).and(dateEndSpec);

        return this.loanRepository.findAll(spec);

    }

    @Override
    public List<Loan> findAll() {

        return (List<Loan>) this.loanRepository.findAll();
    }

    @Override
    public Page<Loan> findPage(LoanSearchDto dto) {
        return this.loanRepository.findAll(dto.getPageable().getPageable());
    }

    @Override
    public void save(LoanDto dto) throws Exception {
        Loan loan;
        loan = new Loan();

        if (dto.getDateEnd().before(dto.getDateStart())) {
            throw new Exception("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        long diffMilliSec = Math.abs(dto.getDateEnd().getTime() - dto.getDateStart().getTime());
        long diffDays = TimeUnit.DAYS.convert(diffMilliSec, TimeUnit.MILLISECONDS);
        if (diffDays > 14) {
            throw new Exception("El periodo de préstamo no puede ser mayor a 14 días");
        }

        List<Loan> existingLoansStart = find(dto.getGame().getId(), null, dto.getDateStart());
        List<Loan> existingLoansEnd = find(dto.getGame().getId(), null, dto.getDateEnd());
        if (!existingLoansEnd.isEmpty() || !existingLoansStart.isEmpty()) {
            throw new Exception("El juego ya está prestado a otro cliente en el periodo de tiempo seleccionado.");
        }

        Calendar start = Calendar.getInstance();
        start.setTime(dto.getDateStart());
        Calendar end = Calendar.getInstance();
        end.setTime(dto.getDateEnd());
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            existingLoansStart = find(null, dto.getClient().getId(), dto.getDateStart());
            existingLoansEnd = find(null, dto.getClient().getId(), dto.getDateEnd());
            if (!existingLoansEnd.isEmpty() || !existingLoansStart.isEmpty()) {

                throw new Exception("Este cliente ya tiene un juego prestado el mismo día.");
            }
        }

        BeanUtils.copyProperties(dto, loan, "client", "game");
        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientsService.get(dto.getClient().getId()));
        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not Exists");
        }

        this.loanRepository.deleteById(id);
    }

    @Override
    public Loan get(Long id) {
        return this.loanRepository.findById(id).orElse(null);
    }
}
