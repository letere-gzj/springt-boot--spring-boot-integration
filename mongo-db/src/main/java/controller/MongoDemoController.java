package controller;

import bean.MongoDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import util.MongoUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author gaozijie
 * @since 2024-04-28
 */
@RequestMapping("/mongo/demo")
@RestController
public class MongoDemoController {

    @Autowired
    private MongoOperations mongoOperations;

    @PostMapping
    public void save(@RequestBody MongoDemo mongoDemo) {
        mongoDemo.setCreateTime(LocalDateTime.now());
        mongoOperations.save(mongoDemo);
    }

    @PutMapping
    public void update(@RequestBody MongoDemo mongoDemo) {
        Update update = MongoUtil.getUpdate(mongoDemo);
        Criteria criteria = new Criteria("demoId").is(mongoDemo.getDemoId());
        Query query = new Query(criteria);
        mongoOperations.updateFirst(query, update, MongoDemo.class);
    }

    @DeleteMapping("/{demoId}")
    public void delete(@PathVariable("demoId") Integer demoId) {
        Criteria criteria = new Criteria("demoId").is(demoId);
        Query query = new Query(criteria);
        mongoOperations.remove(query, MongoDemo.class);
    }

    @GetMapping("/{demoId}")
    public MongoDemo getInfo(@PathVariable("demoId") Integer demoId) {
        Criteria criteria = new Criteria("demoId").is(demoId);
        Query query = new Query(criteria);
        return mongoOperations.findOne(query, MongoDemo.class);
    }

    @GetMapping("/list")
    public List<MongoDemo> list() {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        return mongoOperations.find(query, MongoDemo.class);
    }

    @GetMapping("/page")
    public Page<MongoDemo> page(int pageNum, int pageSize) {
        Criteria criteria = new Criteria();
        Query query = new Query(criteria);
        long total = mongoOperations.count(query, MongoDemo.class);
        PageRequest pageRequest = PageRequest.of(pageNum-1, pageSize);
        query.with(pageRequest);
        List<MongoDemo> mongoDemos = mongoOperations.find(query, MongoDemo.class);
        return new PageImpl<>(mongoDemos, pageRequest, total);
    }
}
