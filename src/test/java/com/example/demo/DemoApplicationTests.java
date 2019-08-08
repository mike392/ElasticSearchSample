package com.example.demo;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.search.MatchQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private ElasticsearchTemplate template;
	@Autowired
	private ArticleRepository repo;
	@Before
	public void setUp()
	{
		template.createIndex(Article.class);
		Article article = new Article().setTitle("Spring Data Elasticsearch");
		article.setAuthors(asList(new Author().setName("John Smith")));
		Article article1 = new Article().setTitle("Spring1 Data Elasticsearch");
		article1.setAuthors(asList(new Author().setName("John Doe")));
		Article article2 = new Article().setTitle("Spring2 Data Elasticsearch");
		article2.setAuthors(asList(new Author().setName("Johnnny Doe")));
		repo.deleteAll();
		repo.save(article);
		repo.save(article1);
		repo.save(article2);
	}
	@Test
	public void testExactMatch()
	{
		String nameToFind = "John";
		Page<Article> articleByAuthorName
			= repo.findByAuthorsNameUsingCustomQueryIncluding(nameToFind, PageRequest.of(0, 10));
		assertThat(articleByAuthorName.getTotalElements()).isEqualTo(2);
	}

	@Test
	public void testApproximateMatch()
	{
		String nameToFind = "Johnnny Doe";
		SearchQuery query = new NativeSearchQueryBuilder()
			.withQuery(matchQuery("authors.name", nameToFind)).build();
		List<Article> articleByAuthorName
			= template.queryForList(query, Article.class);
		assertThat(articleByAuthorName.size()).isEqualTo(2);
	}

}
