package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.entities.HumansPrivateLocation;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface RHumansPrivateLocationLocal {

    public HumansPrivateLocation doDirtyRHumansPrivateLocation(String humanId);
}