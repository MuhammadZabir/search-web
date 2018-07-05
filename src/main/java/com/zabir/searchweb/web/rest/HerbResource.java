package com.zabir.searchweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zabir.searchweb.domain.Herb;
import com.zabir.searchweb.repository.HerbRepository;
import com.zabir.searchweb.repository.search.HerbSearchRepository;
import com.zabir.searchweb.web.rest.errors.BadRequestAlertException;
import com.zabir.searchweb.web.rest.util.HeaderUtil;
import com.zabir.searchweb.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Herb.
 */
@RestController
@RequestMapping("/api")
public class HerbResource {

    private final Logger log = LoggerFactory.getLogger(HerbResource.class);

    private static final String ENTITY_NAME = "herb";

    private final HerbRepository herbRepository;

    private final HerbSearchRepository herbSearchRepository;

    public HerbResource(HerbRepository herbRepository, HerbSearchRepository herbSearchRepository) {
        this.herbRepository = herbRepository;
        this.herbSearchRepository = herbSearchRepository;
    }

    /**
     * POST  /herbs : Create a new herb.
     *
     * @param herb the herb to create
     * @return the ResponseEntity with status 201 (Created) and with body the new herb, or with status 400 (Bad Request) if the herb has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/herbs")
    @Timed
    public ResponseEntity<Herb> createHerb(@RequestBody Herb herb) throws URISyntaxException {
        log.debug("REST request to save Herb : {}", herb);
        if (herb.getId() != null) {
            throw new BadRequestAlertException("A new herb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        herb.setDirectory("/images/" + herb.getName().toLowerCase());
        File dir = new File(System.getProperty("user.dir") + herb.getDirectory());
        if (!dir.exists()) {
            dir.mkdir();
        }
        Herb result = herbRepository.save(herb);
        herbSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/herbs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /herbs : Updates an existing herb.
     *
     * @param herb the herb to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated herb,
     * or with status 400 (Bad Request) if the herb is not valid,
     * or with status 500 (Internal Server Error) if the herb couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/herbs")
    @Timed
    public ResponseEntity<Herb> updateHerb(@RequestBody Herb herb) throws URISyntaxException {
        log.debug("REST request to update Herb : {}", herb);
        if (herb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        herb.setDirectory("/images/" + herb.getName().toLowerCase());
        File dir = new File(System.getProperty("user.dir") + herb.getDescription());
        if (!dir.exists()) {
            dir.mkdir();
        }
        Herb result = herbRepository.save(herb);
        herbSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, herb.getId().toString()))
            .body(result);
    }

    /**
     * GET  /herbs : get all the herbs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of herbs in body
     */
    @GetMapping("/herbs")
    @Timed
    public ResponseEntity<List<Herb>> getAllHerbs(Pageable pageable) {
        log.debug("REST request to get a page of Herbs");
        Page<Herb> page = herbRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/herbs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /herbs/:id : get the "id" herb.
     *
     * @param id the id of the herb to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the herb, or with status 404 (Not Found)
     */
    @GetMapping("/herbs/{id}")
    @Timed
    public ResponseEntity<Herb> getHerb(@PathVariable Long id) {
        log.debug("REST request to get Herb : {}", id);
        Optional<Herb> herb = herbRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(herb);
    }

    /**
     * DELETE  /herbs/:id : delete the "id" herb.
     *
     * @param id the id of the herb to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/herbs/{id}")
    @Timed
    public ResponseEntity<Void> deleteHerb(@PathVariable Long id) {
        log.debug("REST request to delete Herb : {}", id);

        herbRepository.deleteById(id);
        herbSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/herbs?query=:query : search for the herb corresponding
     * to the query.
     *
     * @param query the query of the herb search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/herbs")
    @Timed
    public ResponseEntity<List<Herb>> searchHerbs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Herbs for query {}", query);
        Page<Herb> page = herbSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/herbs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
