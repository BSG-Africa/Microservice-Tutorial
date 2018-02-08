package za.co.bsg.repository;

import za.co.bsg.domain.MovieTicket;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MovieTicket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieTicketRepository extends JpaRepository<MovieTicket, Long> {

}
