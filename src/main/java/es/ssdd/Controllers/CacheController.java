package es.ssdd.Controllers;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
	
	private static Log log = LogFactory.getLog(CacheController.class);
	
	@Autowired
	private CacheManager cacheManager;
	
	@RequestMapping(value="/cache", method = RequestMethod.GET)
	public Map<Object, Object> getCacheContent() {
		log.info("GET /cache");
		ConcurrentMapCacheManager cacheMgr = (ConcurrentMapCacheManager) cacheManager;
		ConcurrentMapCache cache = (ConcurrentMapCache) cacheMgr.getCache("NoMeOlvides");
		return cache.getNativeCache();
	}

}
