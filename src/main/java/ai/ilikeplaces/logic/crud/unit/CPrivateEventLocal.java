package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.PrivateEvent;
import ai.ilikeplaces.entities.etc.DBRefreshDataException;
import ai.ilikeplaces.exception.DBDishonourCheckedException;
import ai.ilikeplaces.exception.DBFetchDataException;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Jan 13, 2010
 * Time: 12:07:04 AM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CPrivateEventLocal {

    public PrivateEvent doNTxCPrivateEvent(final String humanId,
                                           final long privateLocationId,
                                           final String eventName,
                                           final String eventInfo,
                                           final String startDate,
                                           final String endDate) throws DBDishonourCheckedException, DBFetchDataException, DBRefreshDataException;

}
