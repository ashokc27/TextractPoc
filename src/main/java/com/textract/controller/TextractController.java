package com.textract.controller;

import com.textract.service.TextractService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/api/textract")
public class TextractController {
	
	@Inject
	TextractService textractService;
	
	@Get("/get-amount/{fileId}")
	public String getAmount(Integer fileId) {
		return textractService.getAmount(fileId);
	}
}
