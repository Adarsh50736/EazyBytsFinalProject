package com.newsaggregator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsaggregator.model.Article;
import com.newsaggregator.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalNewsService {

	private final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=6e550a7525574fa6822af8f94ec196a7";

	@Autowired
	private ArticleRepository articleRepository;

	public List<Article> fetchAndSaveNews() throws IOException {

		URL url = new URL(API_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String response = in.lines().collect(Collectors.joining());
		in.close();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response);
		JsonNode articlesNode = root.path("articles");

		List<Article> articles = new ArrayList<>();

		for (JsonNode node : articlesNode) {
			Article article = new Article();
			article.setTitle(node.path("title").asText());
			article.setSource(node.path("source").path("name").asText());
			article.setUrl(node.path("url").asText());
			article.setCategory("general"); // default category
			article.setPublishedAt(node.path("publishedAt").asText());
			article.setContent(node.path("description").asText());

			// Step 4: Save in DB
			articles.add(articleRepository.save(article));
		}

		return articles;
	}
}
