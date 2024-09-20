package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.clients.ClientsService;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientsService clientService;

    @Autowired
    GameService gameService;

    @Override
    public List<Loan> find(String title, Long idClients, Date date) {
        return List.of();
    }

    @Override
    public void save(Long id, LoanDto dto) {
        Loan loan;
        loan = new Loan();

        BeanUtils.copyProperties(dto, loan, "id", "client", "game");
        loan.setClient(clientService.get(dto.getClient().getId()));
        loan.setGame(gameService.get(dto.getGame().getId()));
    }

    @Override
    public void delete(Long id) throws Exception {

    }
}
