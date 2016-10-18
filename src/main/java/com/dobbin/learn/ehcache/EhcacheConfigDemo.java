package com.dobbin.learn.ehcache;

import java.io.File;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.UserManagedCache;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.CacheManagerConfiguration;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.junit.Assert;
import org.junit.Test;

/**
 * Ehcache配置demo
 * 
 * @author 833901
 *
 */
public class EhcacheConfigDemo {

	@Test
	public void testConfigWithJava() {

		String cacheAlias = "preConfig";

		// 1. build a cache manager builder;
		CacheManagerBuilder<CacheManager> cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder();

		// 2. build a resource pools builder;
		// 缓存映射关系的最大数量为5
		ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.heap(5);

		// 3. build a cache configuration builder;
		CacheConfigurationBuilder<Long, String> cacheConfigurationBuilder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePoolsBuilder);

		// 4. build a cache manager
		CacheManager cacheManager = cacheManagerBuilder.withCache(cacheAlias, cacheConfigurationBuilder).build();

		// 5. init cacheManager.同上一步中用build(true)
		cacheManager.init();

		// 6. get cache
		Cache<Long, String> preConfigCache = cacheManager.getCache(cacheAlias, Long.class, String.class);

		// 7. put some value to cache
		String value = "hello sfer";
		preConfigCache.put(1L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(1L)));
		
		preConfigCache.put(2L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(2L)));
		
		preConfigCache.put(3L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(3L)));
		
		preConfigCache.put(4L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(4L)));
		
		preConfigCache.put(5L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(5L)));
		
		preConfigCache.put(6L, value);
		Assert.assertTrue(value.equals(preConfigCache.get(6L)));

		cacheManager.removeCache(cacheAlias);

		cacheManager.close();
	}

	@Test
	public void testUserManagedCache() {
		// 1. build a user managed cache
		UserManagedCache<Long, String> userManagedCache = UserManagedCacheBuilder
				.newUserManagedCacheBuilder(Long.class, String.class).build(true);

		// 2. cache a value
		String value = "do it!";
		userManagedCache.put(1l, value);

		Assert.assertTrue(value.equals(userManagedCache.get(1L)));

		userManagedCache.close();
	}

	@Test
	public void testOffHeapCache() {
		// 使用堆外缓存，数据存储在堆外需要被序列化和反序列化，而且会比存储在堆内要满一点
		// 1. build a cache manager builder;
		CacheManagerBuilder<CacheManager> cacheManagerBuilder = CacheManagerBuilder.newCacheManagerBuilder();

		// 2. build a resource pools;
		// 缓存映射关系的最大数量为10，且设置堆外存储容量为10M
		ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES)
				.offheap(10, MemoryUnit.MB).build();

		// 3. build a cache configuration builder;
		CacheConfigurationBuilder<Long, String> cacheConfigurationBuilder = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePools);

		CacheConfiguration<Long, String> cacheConfig = cacheConfigurationBuilder.build();

		// 4. build and init a cache manager
		String tieredCacheAlias = "tieredCache";
		CacheManager tieredCacheManager = cacheManagerBuilder.withCache(tieredCacheAlias, cacheConfig).build(true);

		// 5. get cache
		Cache<Long, String> tieredCache = tieredCacheManager.getCache(tieredCacheAlias, Long.class, String.class);

		// 6. put a value
		String value = "go home";

		tieredCache.put(1L, value);

		Assert.assertTrue(value.equals(tieredCache.get(1L)));

		tieredCache.clear();

		Assert.assertNull(tieredCache.get(1L));

		tieredCacheManager.close();
	}

	@Test
	public void testDiskPersistence() {

		// 持久缓存
		// disk persistence path
		String location = "D:" + File.pathSeparator + "temp" + File.pathSeparator + "ehcache";

		String persistenceCacheAlias = "persistenceCache";

		// 2. initialize cache manager configuration
		CacheManagerConfiguration<PersistentCacheManager> cacheManagerConfiguration = CacheManagerBuilder
				.persistence(location);

		// 3. build resource pools
		ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES)
				.disk(10, MemoryUnit.MB, true).build();

		// 4.
		CacheConfiguration<Long, String> configuration = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePools).build();

		PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(cacheManagerConfiguration).withCache(persistenceCacheAlias, configuration).build(true);

		Cache<Long, String> cache = persistentCacheManager.getCache(persistenceCacheAlias, Long.class, String.class);

		String value = "good afternoon";
		cache.put(1L, value);

		Assert.assertTrue(value.equals(cache.get(1L)));

		cache.clear();

		Assert.assertTrue(!value.equals(cache.get(1L)));

		persistentCacheManager.close();

	}

	@Test
	public void testThreeTiers() {
		// 1. 创建三级缓存的资源池
		ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, EntryUnit.ENTRIES)
				.offheap(10, MemoryUnit.MB).disk(20, MemoryUnit.MB).build();

		// 2. 创建缓存配置
		CacheConfiguration<Long, String> cacheConfiguration = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePools).build();

		String location = "D:" + File.pathSeparator + "temp" + File.pathSeparator + "ehcache";
		CacheManagerConfiguration<PersistentCacheManager> cacheManagerConfig = CacheManagerBuilder
				.persistence(location);

		String alias = "threeTiers";
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().with(cacheManagerConfig)
				.withCache(alias, cacheConfiguration).build(true);

		Cache<Long, String> cache = cacheManager.getCache(alias, Long.class, String.class);

		String value = "hello cat";
		cache.put(1L, value);

		Assert.assertTrue(value.equals(cache.get(1L)));

		cacheManager.close();

	}

	@Test
	public void testByteBasedHeap() {
		// 用内存单元定义堆层的大小，替代定义条目数
		ResourcePools resourcePools1 = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, MemoryUnit.KB)
				.offheap(10, MemoryUnit.MB).build();
		CacheConfiguration<Long, String> usesConfiguredInCacheConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePools1).withSizeOfMaxObjectGraph(1000)
				.withSizeOfMaxObjectSize(1000, MemoryUnit.B).build();

		ResourcePools resourcePools2 = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, MemoryUnit.KB)
				.offheap(10, MemoryUnit.MB).build();
		CacheConfiguration<Long, String> usesDefaultSizeOfEngineConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Long.class, String.class, resourcePools2).build();
		
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withDefaultSizeOfMaxObjectSize(500, MemoryUnit.B)
				.withDefaultSizeOfMaxObjectGraph(2000)
				.withCache("usesConfiguredInCache", usesConfiguredInCacheConfig)
				.withCache("usesDefaultSizeOfEngine", usesDefaultSizeOfEngineConfig)
				.build(true);
		
		Cache<Long, String> usesConfiguredInCache = cacheManager.getCache("usesConfiguredInCache", Long.class, String.class);
		
		String temp = "hellocat";
		usesConfiguredInCache.put(1L, temp);
		Assert.assertTrue(temp.equals(usesConfiguredInCache.get(1L)));
		
		Cache<Long, String> usesDefaultSizeOfEngine = cacheManager.getCache("usesDefaultSizeOfEngine", Long.class, String.class);
		usesDefaultSizeOfEngine.put(1L, temp);
		Assert.assertTrue(temp.equals(usesDefaultSizeOfEngine.get(1L)));
		
		cacheManager.close();
		
	}

}
