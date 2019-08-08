package com.example.demo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "blog", type = "article")
@Data
@Accessors(chain = true)
public class Article {

	@Id
	private String id;

	private String title;

	@Field(type = FieldType.Nested, includeInParent = true)
	private List<Author> authors;

	// standard getters and setters
}
