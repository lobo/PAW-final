package ar.edu.itba.paw.model.clan;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by juanfra on 17/06/17.
 */
@Entity
@Table(name = "clansbattle")
public class ClanBattle implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clanid")
    private Clan clan;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "versusid")
    private Clan versus;

    @Column(name = "initialscore", nullable = false)
    private BigDecimal initialScore;

    ClanBattle(){}

    public ClanBattle(Clan clan, Clan versus, BigDecimal initialScore) {
        this.clan = clan;
        this.versus = versus;
        this.initialScore = initialScore;
    }

    public Clan getClan() {
        return clan;
    }

    public Clan getVersus() {
        return versus;
    }

    public BigDecimal getInitialScore() {
        return initialScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClanBattle that = (ClanBattle) o;

        if (clan != null ? !clan.equals(that.clan) : that.clan != null) return false;
        return versus != null ? versus.equals(that.versus) : that.versus == null;

    }

    @Override
    public int hashCode() {
        int result = clan != null ? clan.hashCode() : 0;
        result = 31 * result + (versus != null ? versus.hashCode() : 0);
        return result;
    }
}
