package com.crossover.techtrial.controller;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;

/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 * 
 * @author Crossover
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PanelControllerTest {

	MockMvc mockMvc;

	@Mock
	private PanelController panelController;

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(panelController).build();
	}

	@Test
	public void testPanelShouldBeRegistered() throws Exception {
		HttpEntity<Object> panel = getHttpEntity("{\"serial\": \"232323\", \"longitude\": \"54.123232\","
				+ " \"latitude\": \"54.123232\",\"brand\":\"tesla\" }");
		ResponseEntity<Panel> response = template.postForEntity("/api/register", panel, Panel.class);
		Assert.assertEquals(202, response.getStatusCode().value());
	}

	@Test
	public void testHourlyElectricityShouldBeSaved() {

		HttpEntity<Object> hourlyElectricity = getHttpEntity(
				"{\"panel\" : {\"serial\": \"232323\", \"longitude\": \"54.123232\","
						+ " \"latitude\": \"54.123232\",\"brand\":\"tesla\" },\"generatedElectricity\":" + 123l
						+ ",\"readingAt\":\"2018-08-05T04:30\"}");
		ResponseEntity<HourlyElectricity> response = template.postForEntity("/api/panels/232323/hourly",
				hourlyElectricity, HourlyElectricity.class);
		Assert.assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void testgetHourlyElectricityWhenPanelisNotNull() {
		ResponseEntity response = template.getForEntity("/api/panels/1234567890123456/hourly", Object.class);
		Assert.assertEquals(200, response.getStatusCode().value());
	}
	
	@Test
	public void testgetHourlyElectricityWhenPanelisNull() {
		ResponseEntity response = template.getForEntity("/api/panels/2323/hourly", Object.class);
		System.out.println(response);
		Assert.assertEquals(404, response.getStatusCode().value());
	}

	private HttpEntity<Object> getHttpEntity(Object body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<Object>(body, headers);
	}
}
