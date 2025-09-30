package com.newsaggregator.service;

import com.newsaggregator.model.Article;
import com.newsaggregator.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
	@Autowired
	private ArticleRepository articleRepository;

	public List<Article> getAllArticles() {
		return articleRepository.findAll();
	}

	public List<Article> searchArticles(String keyword) {
		return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword);
	}

	public List<Article> getByCategory(String category) {
		return articleRepository.findByCategory(category);
	}

	public Article getById(Long id) {
		return articleRepository.findById(id).orElse(null);
	}
}
