package support;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;
import domain.Person;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:32
 */
public class PersonFactory {

    public static Person getPerson(Long id) {
        HareAssertUtils.notNull(id);

        Person person = new Person();
        person.setId(id);
        person.setName("name_" + id);

        return person;
    }

}
