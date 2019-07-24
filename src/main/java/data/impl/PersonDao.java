package data.impl;

import cn.tenbit.hare.core.lite.util.HareAssertUtils;
import data.DataDao;
import domain.Person;
import support.PersonFactory;

/**
 * @Author bangquan.qian
 * @Date 2019-07-23 11:34
 */
public class PersonDao implements DataDao<Person, PersonQuery> {

    @Override
    public Person select(PersonQuery query) {
        HareAssertUtils.notNull(query);
        return PersonFactory.getPerson(query.getId());
    }

}
