package es.ssdd.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.ssdd.Exceptions.BadRequestException;
import es.ssdd.Exceptions.NotFoundException;
import es.ssdd.Models.ItemList;
import es.ssdd.Repositories.ItemListRepository;

@Component
public class ItemListService {

	@Autowired
	private ItemListRepository bd;
	
	public List<ItemList> getByUser (String user) {
		List<ItemList> lists = bd.findByUser(user);
		return lists;
	}

	public ItemList save(ItemList list) {
		return bd.save(list);
	}

	public ItemList edit(long id, ItemList list) {
		if (!bd.exists(id)) {
			throw new NotFoundException();
		}
		if (list.getId() != 0 && id != list.getId()) {
			throw new BadRequestException();
		}
		ItemList bdList = bd.findOne(id);

		bdList.setItems(list.getItems());
		bdList.setName(list.getName());
		bdList.setUser(list.getUser());
		return bd.save(bdList);
	}

	public ItemList delete(long id) {
		if (!bd.exists(id)) {
			throw new NotFoundException();
		}
		ItemList list = bd.findOne(id);
		bd.delete(list);
		return list;
	}

	public List<ItemList> getByUserAndName(String user, String name) {
		return bd.findByUserAndName(user, name);
	}
}
