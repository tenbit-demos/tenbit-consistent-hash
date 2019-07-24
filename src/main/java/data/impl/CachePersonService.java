package data.impl;

import cache.Cache;
import domain.ClientRequest;
import domain.ClientResponse;
import domain.Person;
import lombok.Setter;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:40
 */
public class CachePersonService implements PersonService {

    @Setter
    private Cache<Person, Long> cacheService;

    private PersonService personService;

    public CachePersonService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public ClientResponse query(ClientRequest request) {
        ServiceValidate.validate(request);
        Person person = cacheService.get(request.getId());
        if (person != null) {
            return ServiceConvert.convert2ClientResponse(person);
        }

        ClientResponse response = personService.query(request);
        person = response == null ? null : response.getPerson();
        if (person != null) {
            cacheService.put(person.getId(), person);
        }
        return ServiceConvert.convert2ClientResponse(person);
    }
}
