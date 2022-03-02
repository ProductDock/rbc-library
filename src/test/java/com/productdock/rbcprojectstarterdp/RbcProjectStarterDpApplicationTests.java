package com.productdock.rbcprojectstarterdp;

import com.productdock.rbcprojectstarterdp.api.RestApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RestApi.class)
@ContextConfiguration(classes = RbcProjectStarterDpApplication.class)
class RbcProjectStarterDpApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() throws Exception {
		mockMvc.perform(get("/api/test")).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().string("<h1>Success</h1>"));
	}

}
