package support;

import cache.*;
import data.impl.*;
import domain.Person;
import lombok.Getter;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 12:08
 */
public class BeanContainer {

    private static final BeanContainer INSTANCE = new BeanContainer();
    @Getter
    private final PersonDao simplePersonDao = new PersonDao();
    @Getter
    private final StatusablePersonDao statusablePersonDao = new StatusablePersonDao(simplePersonDao);
    @Getter
    private final PersonDao personDao = statusablePersonDao;
    @Getter
    private final PersonService simplePersonService = new SimplePersonService();
    @Getter
    private final PersonService cachePersonService = new CachePersonService(simplePersonService);
    @Getter
    private final PersonService personService = cachePersonService;
    @Getter
    private final NetCacheColony<Person, Long> cacheColony = new NetCacheColony<>(new NetLongIdMappingStrategy());
    @Getter
    private final NetCacheManager<Person, Long> cacheManager = new NetCacheManager<>(cacheColony);
    @Getter
    private final Cache<Person, Long> cacheService = new NetCacheDelegate<>(cacheColony);

    private BeanContainer() {
        build();
    }

    public static BeanContainer getInstance() {
        return INSTANCE;
    }

    private void build() {
        ((SimplePersonService) simplePersonService).setPersonDao(personDao);
        ((CachePersonService) cachePersonService).setCacheService(cacheService);
    }
}
