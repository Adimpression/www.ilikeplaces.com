package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.entities.Human;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CHumanLocal {
    public void doNTxCHuman(final Human newUser);
}
