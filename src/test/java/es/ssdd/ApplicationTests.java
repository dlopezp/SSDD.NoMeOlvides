package es.ssdd;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ssdd.Models.Item;
import es.ssdd.Models.ItemList;
import es.ssdd.Repositories.ItemListRepository;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	@Autowired
	private ItemListRepository bd;
	
	private MockMvc mockMvc;
	
	private MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
	);
	
	private static final String rootPath = "/lists";
	private static final String listUser = "userTest";
	private static final String listName = "nameTest";
	private static final List<Item> listItems = new ArrayList<>();
	
	@Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        bd.deleteAllInBatch();
    }

	@Test
	public void getEmptyCollection() throws Exception {
		int expectedSize = 0;
		MockHttpServletRequestBuilder request = 
				buildGetRequest(buildPath("unexistentUser"));
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkSize(response, expectedSize);
		checkStatus(response, status().isOk());
	}
	
	@Test
	public void getNonEmptyCollection() throws Exception {
		int expectedSize = 2;
		insertTestList();
		insertTestList();
		MockHttpServletRequestBuilder request = 
				buildGetRequest(buildPath(listUser));
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkSize(response, expectedSize);
		checkStatus(response, status().isOk());
	}
	
	@Test
	public void getEmptyCollectionWithName() throws Exception {
		int expectedSize = 0;
		MockHttpServletRequestBuilder request = 
				buildGetRequest(buildPath(listUser, "unexistenName"));
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkSize(response, expectedSize);
		checkStatus(response, status().isOk());
	}
	
	@Test
	public void getNonEmptyCollectionWithName() throws Exception {
		int expectedSize = 2;
		insertTestList();
		insertTestList();
		MockHttpServletRequestBuilder request = 
				buildGetRequest(buildPath(listUser, listName));
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkSize(response, expectedSize);
		checkStatus(response, status().isOk());
	}
	
	public void twoUsers() throws Exception {
		ItemList list1 = insertTestList();
		ItemList list2 = getTestItemList();
		list2.setUser("newUserTest");
		bd.save(list2);
		int expectedSize1 = 1;
		int expectedSize2 = 1;
		
		MockHttpServletRequestBuilder request1 = 
				buildGetRequest(buildPath(listUser));
		MockHttpServletRequestBuilder request2 = 
				buildGetRequest(buildPath(list2.getUser()));
		
		ResultActions response1 = mockMvc.perform(request1);
		ResultActions response2 = mockMvc.perform(request2);
		
		checkContentType(response1);
		checkSize(response1, expectedSize1);
		checkStatus(response1, status().isOk());
		
		checkContentType(response2);
		checkSize(response2, expectedSize2);
		checkStatus(response2, status().isOk());
	}
	
	@Test
	public void postNewList() throws Exception {
		ItemList list = getTestItemList();
		MockHttpServletRequestBuilder request = buildPostRequest(rootPath, list);
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkStatus(response, status().isCreated());
	}
	
	@Test
	public void postBadList() throws Exception {
		String badList = "{\"userBad\":\"aaa\",\"nameBad\":\"bbb\",\"itemsBad\":[]}";
		MockHttpServletRequestBuilder request = buildPostRequest(rootPath, badList);
		
		ResultActions response = mockMvc.perform(request);
		
		checkStatus(response, status().isBadRequest());
	}
	
	@Test
	public void putList() throws Exception {
		ItemList list = insertTestList();
		list.setName("newName");
		MockHttpServletRequestBuilder request = buildPutrequest(
			buildPath(list.getId()),
			list
		);
		
		ResultActions response = mockMvc.perform(request);
		
		checkContentType(response);
		checkFields(response, list);
		checkStatus(response, status().isOk());
	}
	
	@Test
	public void invalidPut() throws Exception {
		ItemList list = insertTestList();
		list.setName("newName");
		MockHttpServletRequestBuilder request = buildPutrequest(
			buildPath(99999),
			list
		);
		
		ResultActions response = mockMvc.perform(request);
		
		checkStatus(response, status().isNotFound());
	}
	
	@Test
	public void validDelete() throws Exception {
		int expectedSize = 0;
		ItemList list = insertTestList();
		MockHttpServletRequestBuilder deleteRequest = 
				buildDeleteRequest(buildPath(list.getId()));
		
		ResultActions response = mockMvc.perform(deleteRequest);
		
		checkContentType(response);
		checkStatus(response, status().isOk());
		assertEquals(expectedSize, bd.findAll().size());
	}
	
	@Test
	public void invalidDelete() throws Exception {
		int expectedSize = 1;
		ItemList list = insertTestList();
		MockHttpServletRequestBuilder deleteRequest = 
				buildDeleteRequest(buildPath(9999));
		
		ResultActions response = mockMvc.perform(deleteRequest);
		
		checkStatus(response, status().isNotFound());
		assertEquals(expectedSize, bd.findAll().size());
	}
	
	private void checkFields(ResultActions result, ItemList list) throws Exception {
		result
			.andExpect(jsonPath("$.user", is(list.getUser())))
	        .andExpect(jsonPath("$.name", is(list.getName())))
	        .andExpect(jsonPath("$.items", is(list.getItems())));
	}
	
	private MockHttpServletRequestBuilder buildGetRequest(String path) {
		return get(path).contentType(contentType);
	}
	
	private MockHttpServletRequestBuilder buildGetRequest(String path, long id) {
		return get(path, id).contentType(contentType);
	}
	
	private MockHttpServletRequestBuilder buildPostRequest(String path, Object o) throws JsonProcessingException {
		return post(path)
			.contentType(contentType)
			.content(json(o));
	}
	
	private MockHttpServletRequestBuilder buildDeleteRequest(String path, long id) {
		return delete(path, id).contentType(contentType);
	}
	
	private MockHttpServletRequestBuilder buildDeleteRequest(String path) {
		return delete(path).contentType(contentType);
	}
	
	private MockHttpServletRequestBuilder buildPutrequest(String path, Object o) throws JsonProcessingException {
		return put(path)
			.contentType(contentType)
			.content(json(o));
	}
	
	private String buildPath (String user) {
		return rootPath + "/" + user;
	}
	
	private String buildPath (String user, String name) {
		return rootPath + "/" + user + "/" + name;
	}
	
	private String buildPath (long id) {
		return rootPath + "/" + id;
	}

	private void checkStatus(ResultActions response, ResultMatcher rm) throws Exception {
		response.andExpect(rm);
	}

	private void checkSize(ResultActions response, int sizeExpected) throws Exception {
		response.andExpect(jsonPath("$", hasSize(sizeExpected)));
	}

	private void checkContentType(ResultActions response) throws Exception {
		response.andExpect(content().contentType(contentType));
	}
	
	private ItemList insertTestList() {
		ItemList listTest = getTestItemList();
		return bd.save(listTest);
	}
	
	private ItemList getTestItemList() {
		return new ItemList(listUser, listName, listItems);
	}
	
	private byte[] json(final Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        byte[] result = mapper.writeValueAsBytes(object);
        return result;
	}

}
