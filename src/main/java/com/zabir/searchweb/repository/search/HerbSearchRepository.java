package com.zabir.searchweb.repository.search;

import com.zabir.searchweb.domain.Herb;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Herb entity.
 */
public interface HerbSearchRepository extends ElasticsearchRepository<Herb, Long> {
}
