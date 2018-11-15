package com.boojongmin.nctestserver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
// @Slf4j
public class EventServiceTests {
	@Autowired ResourceLoader loader;
	EventService service; 

	// class A {

	// 	@JsonSerialize(using=MyDoubleDesirializer.class)
	// 	double a = 0.0000002341;
	// }

	// public static class MyDoubleDesirializer extends JsonSerializer<Double> {
	// 	@Override
	// 	public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
	// 		BigDecimal d = new BigDecimal(value);
	// 		gen.writeNumber(d.toPlainString());
	// 	}
	// }
	

	// @Test
  // public void testDoubleNumber() throws JsonProcessingException {
	// 	ObjectMapper mapper = new ObjectMapper();
	// 	// mapper.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
  //   String json = mapper.writeValueAsString(new A());
	// 	System.out.println(json);
	// 	log.error(json);

  // }
	
	@Test
	public void testEvents() throws IOException {
		this.service = new EventService(new RestTemplate());
		Path path = loader.getResource("classpath:test_data.csv").getFile().toPath();
		byte[] bytes = Files.readAllBytes(path);
		List<Event> events = service.getEvents(bytes);
		assertThat(events.size()).isEqualTo(24);
	}

	@Test
	public void testEventsByMock() {
		RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), eq(new ParameterizedTypeReference<List<Event>>() {})))
			.thenReturn(new ResponseEntity(new ArrayList<Event>(), HttpStatus.OK));
		this.service = new EventService(restTemplate);
		List<Event> events = service.getEvents(new byte[]{});
		assertThat(events.size()).isEqualTo(0);

	}
}


