package es.ssdd.Repositories;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;

import es.ssdd.Models.ItemList;

@CacheConfig(cacheNames = "NoMeOlvides")
public interface ItemListRepository extends JpaRepository<ItemList, Long> {

	@Cacheable(key = "{#p0}")
	List<ItemList> findByUser(String user);

	@Cacheable(key = "{#p0, #p1}")
	List<ItemList> findByUserAndName(String user, String name);
	  
	@CacheEvict(allEntries = true)
	ItemList save(ItemList list);

	@Caching(evict = {
		@CacheEvict(cacheNames = "NoMeOlvides", key = "{#p0.user}"),
		@CacheEvict(cacheNames = "NoMeOlvides", key = "{#p0.user, #p0.name}")
	})
	void delete(ItemList list);
	
}
