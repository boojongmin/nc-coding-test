package com.boojongmin.nctestserver;

import java.io.IOException;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@SpringBootApplication
public class NcTestServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(NcTestServerApplication.class, args);
	}
}

@Configuration
class BeanConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
class EventController {
	private EventService service;

	public EventController(EventService service) {
		this.service = service;
	}

	@GetMapping("/hello")
	public String hello() {
		return "world";
	}

	@PostMapping("/upload")
	public List<Event> upload(@RequestParam("test_data_file") MultipartFile file) throws IOException {
		List<Event> events = service.getEvents(file.getBytes());
//		events.sort(new Comparator<Event>() {
//			@Override
//			public int compare(Event o1, Event o2) {
//				return o1.getTime().compareTo(o2.getTime());
//			}
//		});
		return events;
	}
}

@Service
class EventService {
	private RestTemplate restTemplate;

	public EventService(RestTemplate restTemplate){
		this.restTemplate = restTemplate;
	}

	public List<Event> getEvents(byte[] bytes) {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		ByteArrayResource res = new ByteArrayResource(bytes) {
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

		ResponseEntity<List<Event>> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<Event>>() {});
		return response.getBody();	
	}
}

@Data
class Event {
	private AnormalFlag anormal_flag;
	private long cumul_lwr;
	private long cumul_upr;
	private long cumulative_sum_of_residual;
	private long fit;
	private long lwr;
	private long residual;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date time;
	private long upr;
	private long value;
}

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum AnormalFlag {
	NORMAL("normal"), CUM_RESID_ANORMAL("cum_resid_anormal");

	private final String type;

	AnormalFlag(String type) {
		this.type = type;
	}

	@JsonValue
	public String getType() {
		return this.type;
	}

	@JsonCreator
	public static AnormalFlag create(String value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		for (AnormalFlag v : values()) {
			if (value.equals(v.getType())) {
				return v;
			}
		}
		throw new IllegalArgumentException();
	}
}
