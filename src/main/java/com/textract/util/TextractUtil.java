package com.textract.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextRequest;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.Document;
import com.amazonaws.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;


public class TextractUtil {

	public static void processDocument(InputStream inputStream) throws IOException {
		
		//Create a new Amazon Textract client with access key and secret key
		BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAQ7SUKFBYUZS7EOKT",
				"RiS8+nGvYjJtNreAHAVQ3iJJRJ+tRclVbuUTbS7L");
		AmazonTextract client = AmazonTextractClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-east-1").build();
		
		ByteBuffer imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		DetectDocumentTextRequest request = new DetectDocumentTextRequest().withDocument(new Document()
						.withBytes(imageBytes));
		
		DetectDocumentTextResult result = client.detectDocumentText(request);
		List<Block> docInfo = result.getBlocks();
		
		int index = 0;
		String possibleAmount = null;
		for (Block block : docInfo) {
			index++;
			if (block.getBlockType().equals("LINE") && block.getText() != null && block.getText().contains("$")) {
				possibleAmount = block.getText();
				System.out.println("Amount found: " + possibleAmount + "at :" + index);
			}
		}
		
		System.out.println("Possible amount: " + possibleAmount);
		if(possibleAmount != null) {
			BigDecimal amount = extractNumber(possibleAmount);
			//Recheck docInfo if it contains the amount at exact index or not
			Block amountBlock = docInfo.get(index);
			boolean amountCheck = false;
			if(amountBlock.getBlockType().equals("LINE") && amountBlock.getText() != null
					&& amountBlock.getText().contains(possibleAmount)) {
				
				amountCheck = true;
			}
			if(amountCheck) {
				System.out.println("Possible check amount : " + possibleAmount);
			}
		}
	}
	
	private static BigDecimal extractNumber(String possibleAmount) {
		//Get number with decimal point from possibleAmount, It may or may not contain decimal point
		String number = possibleAmount.replaceAll("[^\\d.]", "");
		System.out.println("Extracted number: " + number);
		return new BigDecimal(number);
		
	}
	
}
