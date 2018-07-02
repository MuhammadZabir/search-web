package com.zabir.searchweb.repository;

import com.zabir.searchweb.domain.Herb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HerbRepository extends JpaRepository<Herb, Long> {
    Herb findByDirectory(String directory);
}
