package com.zabir.searchweb.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of HerbSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HerbSearchRepositoryMockConfiguration {

    @MockBean
    private HerbSearchRepository mockHerbSearchRepository;

}
