package es.ssdd.Models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ItemList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String user;
	private String name;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Item> items;
	
	protected ItemList () {}
	
	public ItemList (String userName, String listName, List<Item> items) {
		this.user = userName;
		this.name = listName;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String userName) {
		this.user = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String listName) {
		this.name = listName;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
