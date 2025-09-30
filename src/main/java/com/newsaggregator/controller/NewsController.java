package com.newsaggregator.controller;

import com.newsaggregator.model.Article;
import com.newsaggregator.service.ExternalNewsService;
import com.newsaggregator.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private ExternalNewsService externalNewsService;

    
    @GetMapping("/all")
    public List<Article> getAllNews() {
        return newsService.getAllArticles();
    }

    
    @GetMapping("/search")
    public List<Article> search(@RequestParam String keyword) {
        return newsService.searchArticles(keyword);
    }

    
    @GetMapping("/fetch")
    public List<Article> fetchExternalNews() throws IOException {
        return externalNewsService.fetchAndSaveNews();
    }
    
    
    @GetMapping
    public List<Article> getNews() throws IOException {
        return externalNewsService.fetchAndSaveNews();
    }
}
