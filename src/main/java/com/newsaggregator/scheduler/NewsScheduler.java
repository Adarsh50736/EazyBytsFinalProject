package com.newsaggregator.scheduler;

import com.newsaggregator.service.ExternalNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewsScheduler {

	@Autowired
	private ExternalNewsService externalNewsService;

	@Scheduled(fixedRateString = "${news.fetch-rate-ms}")
	public void fetchLatestNews() {
		try {
			externalNewsService.fetchAndSaveNews();
		} catch (Exception e) {

			System.err.println("Failed to fetch news: " + e.getMessage());
		}
	}
}
