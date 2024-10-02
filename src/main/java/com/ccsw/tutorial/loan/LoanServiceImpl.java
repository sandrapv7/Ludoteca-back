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

import java.util.Date;
import java.util.List;

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
        LoanSpecification dateSpec = new LoanSpecification(new SearchCriteria("date", ":", date));

        // Combina las especificaciones teniendo en cuenta la navegación de propiedades
        Specification<Loan> spec = Specification.where(gameSpec).and(clientSpec).and(dateSpec);

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
    public void save(LoanDto dto) {
        Loan loan;
        loan = new Loan();

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
