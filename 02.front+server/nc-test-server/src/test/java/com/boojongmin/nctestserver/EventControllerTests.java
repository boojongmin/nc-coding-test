package com.boojongmin.nctestserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EventControllerTests {
	@Autowired private ResourceLoader loader;
	@Autowired private TestRestTemplate restTemplate;
	@Autowired private ObjectMapper mapper;

	@Test
	public void testEvents() throws IOException {
		Path path = loader.getResource("classpath:test_data.csv").getFile().toPath();
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("name", "test_data_file");
		ByteArrayResource res = new ByteArrayResource(Files.readAllBytes(path)) {
			@Override
			public String getFilename() {
				return "test_data.csv";
			}
		};
		body.add("test_data_file", res);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("/upload", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<String>() {});
		String json = responseEntity.getBody();
		System.out.println(json);
		List<Event> list = mapper.readValue(json, new TypeReference<List<Event>>() {});
		Assertions.assertThat(list.size()).isEqualTo(24);
	}
}
