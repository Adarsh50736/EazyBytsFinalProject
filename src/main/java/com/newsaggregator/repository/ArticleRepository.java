package com.newsaggregator.repository;

import com.newsaggregator.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	List<Article> findByCategory(String category);

	List<Article> findByTitleContainingIgnoreCase(String keyword);

	List<Article> findByTitleContainingOrContentContaining(String keyword, String keywordAgain);

	Optional<Article> findByUrl(String url);
}
