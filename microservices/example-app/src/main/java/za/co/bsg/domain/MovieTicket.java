package za.co.bsg.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A MovieTicket.
 */
@Entity
@Table(name = "movie_ticket")
public class MovieTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 40)
    @Column(name = "movie_name", length = 40)
    private String movieName;

    @Column(name = "user_login")
    private String userLogin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public MovieTicket movieName(String movieName) {
        this.movieName = movieName;
        return this;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public MovieTicket userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MovieTicket movieTicket = (MovieTicket) o;
        if (movieTicket.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), movieTicket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MovieTicket{" +
            "id=" + getId() +
            ", movieName='" + getMovieName() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
