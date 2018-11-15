package com.boojongmin.nctestserver;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

class TestDoubleNumber {
  @Test
  public void testDoubleNumber() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(0.00000001);
    System.out.println(json);
    assertThat("asdf").isEmpty();;

  }
}