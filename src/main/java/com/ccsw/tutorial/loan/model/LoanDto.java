package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.clients.model.ClientsDto;
import com.ccsw.tutorial.game.model.GameDto;

import java.util.Date;

public class LoanDto {

    private Long id;

    private GameDto game;

    private ClientsDto client;

    private Date dateStart;

    private Date dateEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public ClientsDto getClient() {
        return client;
    }

    public void setClient(ClientsDto client) {
        this.client = client;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
