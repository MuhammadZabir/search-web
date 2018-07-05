package com.zabir.searchweb.repository;

import com.zabir.searchweb.domain.Herb;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Herb entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HerbRepository extends JpaRepository<Herb, Long> {
    Herb findByDirectory(String directory);
}
