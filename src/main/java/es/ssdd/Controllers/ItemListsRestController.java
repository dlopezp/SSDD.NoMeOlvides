package es.ssdd.Controllers;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.ssdd.Models.ItemList;
import es.ssdd.Services.ItemListService;

@RestController
@RequestMapping(value = "/lists")
public class ItemListsRestController {
	
	private static Log log = LogFactory.getLog(ItemListsRestController.class);
	
	@Autowired
	private ItemListService service;
	
	@RequestMapping(value = "/{user}", method = RequestMethod.GET)
	public ResponseEntity<List<ItemList>> byUser (
		@PathVariable("user") String user
	) {
		log.info("GET /lists/" + user);
		List<ItemList> lists = service.getByUser(user);
		return new ResponseEntity<>(lists, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{user}/{name}", method = RequestMethod.GET)
	public ResponseEntity<List<ItemList>> byUserAndName (
		@PathVariable("user") String user,
		@PathVariable("name") String name
	) {
		log.info("GET /lists/" + user + "/" + name);
		List<ItemList> lists = service.getByUserAndName(user, name);
		return new ResponseEntity<>(lists, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ItemList> postList (
		@RequestBody ItemList list
	) {
		log.info("POST /lists");
		ItemList newList = service.save(list);
		return new ResponseEntity<ItemList>(newList, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ItemList> edit (
			@PathVariable("id") long id,
			@RequestBody ItemList list	
	) {
		log.info("PUT /lists/" + id);
		ItemList itemListEdited = service.edit(id, list);
		return new ResponseEntity<>(itemListEdited, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ItemList> delete (
			@PathVariable("id") long id
	) {
		log.info("DELETE /lists/" + id);
		ItemList list = service.delete(id);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
