package com.ccsw.tutorial.loan.model;

import com.ccsw.tutorial.clients.model.Clients;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;

import java.util.Date;

/**
 * @author ccsw
 *
 */
@Entity
@Table(name = "loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Clients client;

    @Column(name = "dateStart", nullable = false)
    private Date dateStart;

    @Column(name = "dateEnd", nullable = false)
    private Date dateEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
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
