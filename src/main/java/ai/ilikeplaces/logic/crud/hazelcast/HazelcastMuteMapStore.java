package ai.ilikeplaces.logic.crud.hazelcast;

import ai.ilikeplaces.entities.Mute;
import ai.ilikeplaces.util.cache.AbstractHCMapStore;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/3/12
 * Time: 10:40 PM
 */
public class HazelcastMuteMapStore extends AbstractHCMapStore {

    private Class<Mute> type = Mute.class;
    ;

    @Override
    public void store(Object o, Object o1) {

        super.store(o, o1, type);
    }

    @Override
    public void delete(Object o) {
        super.delete(o, type);
    }


    @Override
    public Object load(Object o) {
        return super.load(o, type);
    }


}
