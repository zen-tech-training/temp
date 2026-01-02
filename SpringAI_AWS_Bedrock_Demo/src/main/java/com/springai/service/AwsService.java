package com.springai.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springai.dto.Prompt;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelSummary;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsRequest;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsResponse;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

@Service
public class AwsService {
    //private static final String CLAUDE = "amazon.nova-lite-v1:0"; //"deepseek.v3-v1:0"; //"anthropic.claude-3-7-sonnet-20250219-v1:0"; //"anthropic.claude-v2";
	private static final String CLAUDE = "anthropic.claude-3-5-sonnet-20240620-v1:0"; //"arn:aws:bedrock:eu-north-1:211125644466:inference-profile/eu.anthropic.claude-3-7-sonnet-20250219-v1:0"; //"anthropic.claude-sonnet-4-5-20250929-v1:0"; //"anthropic.claude-opus-4-5-20251101-v1:0";
	
    @Autowired
    private BedrockRuntimeClient bedrockClient;

    public String askAssistant(Prompt prompt) {
        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + prompt.getQuestion() + "\n\nAssistant:";

        return syncResponse2(enclosedPrompt);
    }

    private String syncResponse(String enclosedPrompt) {

        String payload = new JSONObject().put("prompt", enclosedPrompt)
//                .put("max_tokens_to_sample", 200)
//                .put("temperature", 0.5)
//                .put("stop_sequences", List.of("\n\nHuman:"))
                .toString();
        InvokeModelRequest request = InvokeModelRequest.builder().body(SdkBytes.fromUtf8String(enclosedPrompt))
                .modelId(CLAUDE)
                //.contentType("text/plain")
                .accept("application/json").build();

        InvokeModelResponse response = bedrockClient.invokeModel(request);

        JSONObject responseBody = new JSONObject(response.body().asUtf8String());

        String generatedText = responseBody.getString("completion");

        System.out.println("Generated text: " + generatedText);

        return generatedText;
    }
    
    /*
     * * Synchronous call to AI for text response
     */
    private String syncResponse2(String enclosedPrompt) {

    	listFoundationModels();
    	
        String payload = new JSONObject().put("prompt", enclosedPrompt)
                .put("max_tokens_to_sample", 200)
                .put("temperature", 0.5)
                .put("stop_sequences", List.of("\n\nHuman:")).toString();
        InvokeModelRequest request = InvokeModelRequest.builder().body(SdkBytes.fromUtf8String(payload))
                .modelId(CLAUDE)
                .contentType("application/json")
                .accept("application/json").build();

        InvokeModelResponse response = bedrockClient.invokeModel(request);

        JSONObject responseBody = new JSONObject(response.body().asUtf8String());

        String generatedText = responseBody.getString("completion");

        System.out.println("Generated text: " + generatedText);

        return generatedText;
    }
    
    
    public List<FoundationModelSummary> listFoundationModels() {

        try {

        	Region region = Region.EU_NORTH_1;
        	BedrockClient bedrockClient = BedrockClient.builder()
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .region(region)
                    .build();
        	
            ListFoundationModelsResponse response = bedrockClient.listFoundationModels(r -> {});
            List<FoundationModelSummary> models = response.modelSummaries();

            if (models.isEmpty()) {
                System.out.println("No available foundation models in " + region.toString());
            } else {
                for (FoundationModelSummary model : models) {
                    System.out.println("Model ID: " + model.modelId());
                    System.out.println("Provider: " + model.providerName());
                    System.out.println("Name:     " + model.modelName());
                    System.out.println();
                }
            }

            return models;

        } catch (SdkClientException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }    
}

