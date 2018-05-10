package spittr.web;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeastOnce;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;


import org.springframework.test.web.servlet.MockMvc;

import spittr.controller.SpitterController;
import spittr.data.SpitterRepository;
import spittr.entity.Spitter;

public class SpitterControllerTest {

	@Test
	public void testShowRegistrationForm() throws Exception {
		SpitterController controller = new SpitterController();
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		mockMvc.perform(get("/spitter/register"))
		   .andExpect(view().name("registerForm"));
	}

	@Test
	public void shouldProcessRegistration() throws Exception {
		SpitterRepository mockRepository = mock(SpitterRepository.class);
		Spitter unsaved = new Spitter("jbauer","24hours","Jack","Bauer");
		Spitter saved = new Spitter(24L,"jbauer","24hours","Jack","Bauer");
		when(mockRepository.save(unsaved)).thenReturn(saved);
		
		SpitterController controller = new SpitterController(mockRepository);
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		mockMvc.perform(post("/spitter/register")
				.param("firstName", "Jack")
				.param("lastName", "Bauer")
				.param("username", "jbauer")
				.param("password", "24hours"))
		   .andExpect(redirectedUrl("/spitter/jbauer"));
		
		verify(mockRepository,atLeastOnce()).save(unsaved);
	}
	
	@Test
	public void testShowSpitterProfile() throws Exception{
		SpitterRepository mockRepository = mock(SpitterRepository.class);
		Spitter spitter = new Spitter("jbauer","24hours","Jack","Bauer");
		when(mockRepository.findByUsername("jbauer")).thenReturn(spitter);
		
		SpitterController controller = new SpitterController(mockRepository);
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		mockMvc.perform(get("/spitter/jbauer"))
		    .andExpect(view().name("profile"))
		    .andExpect(model().attributeExists("spitter"))
		    .andExpect(model().attribute("spitter", spitter));
		
	}
}
