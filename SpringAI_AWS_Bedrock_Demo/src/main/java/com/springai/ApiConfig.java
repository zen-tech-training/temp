package com.springai;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
		info = @Info(
				title = "Spring AI AWS Bedrock API Documentation",
				description = "Spring AI AWS Bedrock application",
				version = "1.0",
				license = @License(
						name = "LGPL",
						url = "http://lgpl.com"
				),
				contact = @Contact(
						name = "Anand",
						email = "anand.kulkarni1@zensar.com"
)))
public class ApiConfig {

}
