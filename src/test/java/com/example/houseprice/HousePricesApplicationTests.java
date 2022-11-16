package com.example.houseprice;

import com.example.houseprice.controllers.HousePricesController;
import com.example.houseprice.entity.HousePrices;
import com.example.houseprice.repository.HousePricesRepository;
import com.example.houseprice.utils.Utils;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;



@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class HousePricesApplicationTests {

	private static final String ENDPOINT_URL = "/api/house-prices";

	@InjectMocks
	private HousePricesController housePricesController;

	@MockBean
	private HousePricesRepository housePricesRepository;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.housePricesController).build();

	}

	@Test
	public void createNewHousePricesTest() throws Exception {

		HousePrices housePrices = new HousePrices(1L, "TV", 1, 1.0, null, 1.0, 2.0, null,
				null, 1, "test description", null,1,
				null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null);

		when(housePricesRepository.save(any())).thenReturn(housePrices);

		mockMvc.perform(MockMvcRequestBuilders
				.post(ENDPOINT_URL+"/create")
				.content(Objects.requireNonNull(Utils.jsonAsString(housePrices)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results.id").exists());
	}

	@Test
	public void shouldReturnAllProductsFromDB() throws Exception {
		when(housePricesRepository.findAll()).thenReturn(Arrays.asList(
				new HousePrices(1L, "TV", 1, 1.0, null, 1.0, 2.0, null,
						null, 1, "test description", null,1,
						 null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null),

				new HousePrices(2L, "TV2", 1, 1.0, null, 1.0, 2.0, null,
						null, 1, "test description2", null,1,
						null, null, null, null, null, null, null, null, null, null,
						null, null, null, null, null, null)
		));
		mockMvc.perform(MockMvcRequestBuilders
				.get(ENDPOINT_URL+"/findAll")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").value(1L));
	}


}
