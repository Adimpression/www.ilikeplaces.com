package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.doc.NOTE;
import ai.ilikeplaces.entities.HumansPrivateEvent;
import ai.ilikeplaces.entities.HumansPrivateLocation;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@NOTE(note = "SEE CRUDSERVICE WHERE TO SUPPORT READ AND DIRTY READ, THE TX TYPE IS SUPPORTS.")
@Stateless
public class RHumansPrivateEvent extends AbstractSLBCallbacks implements RHumansPrivateEventLocal {


    @EJB
    private CrudServiceLocal<HumansPrivateEvent> humansPrivateEventCrudServiceLocal_;

    public RHumansPrivateEvent() {
        logger.debug(RBGet.logMsgs.getString("common.Constructor.Init"), RHumansPrivateEvent.class, this.hashCode());
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public HumansPrivateEvent doDirtyRHumansPrivateEvent(String humanId) {
        return humansPrivateEventCrudServiceLocal_.find(HumansPrivateEvent.class, humanId);
    }

    final static Logger logger = LoggerFactory.getLogger(RHumansPrivateEvent.class);
}