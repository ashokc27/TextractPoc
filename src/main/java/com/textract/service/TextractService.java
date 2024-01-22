package com.textract.service;

import com.textract.util.TextractUtil;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

@Singleton
public class TextractService {
	
	public String getAmount(Integer fileId) {
		ClassPathResourceLoader loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).get();
		String resourcePath = null;
		switch (fileId) {
			case 1 -> resourcePath = "images/image1.jpg";
			case 2 -> resourcePath = "images/image2.jpg";
			default -> {
			}
		}
		
		Optional<InputStream> inputStream = loader.getResourceAsStream(resourcePath);
		try {
			TextractUtil.processDocument(inputStream.get());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
