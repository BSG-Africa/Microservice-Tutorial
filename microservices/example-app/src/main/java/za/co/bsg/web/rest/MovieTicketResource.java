package za.co.bsg.web.rest;

import com.codahale.metrics.annotation.Timed;
import za.co.bsg.domain.MovieTicket;

import za.co.bsg.repository.MovieTicketRepository;
import za.co.bsg.web.rest.errors.BadRequestAlertException;
import za.co.bsg.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MovieTicket.
 */
@RestController
@RequestMapping("/api")
public class MovieTicketResource {

    private final Logger log = LoggerFactory.getLogger(MovieTicketResource.class);

    private static final String ENTITY_NAME = "movieTicket";

    private final MovieTicketRepository movieTicketRepository;

    public MovieTicketResource(MovieTicketRepository movieTicketRepository) {
        this.movieTicketRepository = movieTicketRepository;
    }

    /**
     * POST  /movie-tickets : Create a new movieTicket.
     *
     * @param movieTicket the movieTicket to create
     * @return the ResponseEntity with status 201 (Created) and with body the new movieTicket, or with status 400 (Bad Request) if the movieTicket has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/movie-tickets")
    @Timed
    public ResponseEntity<MovieTicket> createMovieTicket(@Valid @RequestBody MovieTicket movieTicket) throws URISyntaxException {
        log.debug("REST request to save MovieTicket : {}", movieTicket);
        if (movieTicket.getId() != null) {
            throw new BadRequestAlertException("A new movieTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovieTicket result = movieTicketRepository.save(movieTicket);
        return ResponseEntity.created(new URI("/api/movie-tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /movie-tickets : Updates an existing movieTicket.
     *
     * @param movieTicket the movieTicket to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated movieTicket,
     * or with status 400 (Bad Request) if the movieTicket is not valid,
     * or with status 500 (Internal Server Error) if the movieTicket couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/movie-tickets")
    @Timed
    public ResponseEntity<MovieTicket> updateMovieTicket(@Valid @RequestBody MovieTicket movieTicket) throws URISyntaxException {
        log.debug("REST request to update MovieTicket : {}", movieTicket);
        if (movieTicket.getId() == null) {
            return createMovieTicket(movieTicket);
        }
        MovieTicket result = movieTicketRepository.save(movieTicket);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, movieTicket.getId().toString()))
            .body(result);
    }

    /**
     * GET  /movie-tickets : get all the movieTickets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of movieTickets in body
     */
    @GetMapping("/movie-tickets")
    @Timed
    public List<MovieTicket> getAllMovieTickets() {
        log.debug("REST request to get all MovieTickets");
        return movieTicketRepository.findAll();
        }

    /**
     * GET  /movie-tickets/:id : get the "id" movieTicket.
     *
     * @param id the id of the movieTicket to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the movieTicket, or with status 404 (Not Found)
     */
    @GetMapping("/movie-tickets/{id}")
    @Timed
    public ResponseEntity<MovieTicket> getMovieTicket(@PathVariable Long id) {
        log.debug("REST request to get MovieTicket : {}", id);
        MovieTicket movieTicket = movieTicketRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(movieTicket));
    }

    /**
     * DELETE  /movie-tickets/:id : delete the "id" movieTicket.
     *
     * @param id the id of the movieTicket to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/movie-tickets/{id}")
    @Timed
    public ResponseEntity<Void> deleteMovieTicket(@PathVariable Long id) {
        log.debug("REST request to delete MovieTicket : {}", id);
        movieTicketRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
