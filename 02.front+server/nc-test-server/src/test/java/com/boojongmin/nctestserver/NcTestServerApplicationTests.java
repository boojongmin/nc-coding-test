package com.boojongmin.nctestserver;

import static org.assertj.core.api.Assertions.assertThat;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;
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
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NcTestServerApplicationTests {
	@Autowired	private ResourceLoader loader;
	@Autowired ObjectMapper mapper;

	@Ignore
	@Test
	public void getRealData() throws IOException {
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
		String serverUrl = "http://35.194.117.145:8080/upload_test_data";

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<Event>> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Event>>() {});
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().get(0).getValue()).isEqualTo(1711323084L);
	}

	@Test
	public void deserializeTestFromJsonToEventList() throws IOException {
		Path path = loader.getResource("classpath:data.json").getFile().toPath();
		byte[] bytes = Files.readAllBytes(path);
		List<Event> list = mapper.readValue(bytes, new TypeReference<List<Event>>() {});
		assertThat(list.size()).isEqualTo(24);
	}
}
