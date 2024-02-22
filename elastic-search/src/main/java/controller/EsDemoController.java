package controller;

import bean.EsDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gaozijie
 * @since 2024-02-21
 */
@RestController
@RequestMapping("/demo/es")
public class EsDemoController {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostMapping
    public void save(@RequestBody EsDemo esDemo) throws Exception{
        esDemo.setCreateTime(LocalDateTime.now());
        elasticsearchOperations.save(esDemo);
    }

    @PutMapping
    public void update(@RequestBody EsDemo esDemo) {
        elasticsearchOperations.update(esDemo);
    }

    @DeleteMapping
    public void delete(Integer demoId) {
        elasticsearchOperations.delete(demoId.toString(), EsDemo.class);
    }

    @GetMapping("/list")
    public List<EsDemo> list() {
        Criteria criteria = new Criteria();
        Query query = new CriteriaQuery(criteria);
        SearchHits<EsDemo> searchHits = elasticsearchOperations.search(query, EsDemo.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @GetMapping("/page")
    public Page<EsDemo> page(int pageNum, int pageSize) {
        Criteria criteria = new Criteria();
        PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
        Query query = new CriteriaQuery(criteria)
                .setPageable(pageRequest);
        SearchHits<EsDemo> searchHits = elasticsearchOperations.search(query, EsDemo.class);
        List<EsDemo> esDemos = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageImpl<>(esDemos, pageRequest, searchHits.getTotalHits());
    }
}
