package ai.ilikeplaces.logic.crud;

import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.scribble.License;

import javax.ejb.Local;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface DBLocal {

    final static public String NAME = DBLocal.class.getSimpleName();

    public HumanCRUDPublicPhotoLocal getHumanCRUDPublicPhotoLocal();

    public HumanCRUDPrivatePhotoLocal getHumanCRUDPrivatePhotoLocal();

    public HumanCRUDLocationLocal getHumanCRUDLocationLocal();

    public HumanCRUDHumanLocal getHumanCRUDHumanLocal();

    public HumanCRUDMapLocal getHumanCRUDMapLocal();

    public HumanCRUDPrivateEventLocal getHumanCrudPrivateEventLocal();

    public HumanCRUDPrivateLocationLocal getHumanCrudPrivateLocationLocal();

    public HumanCRUDWallLocal getHumanCrudWallLocal();

    public SingletonHashingRemote getSingletonHashingFaceLocal();

    public HumanUserLocal getHumanUserLocal();

    public HumanCRUDHumansUnseenLocal getHumanCRUDHumansUnseenLocal();

    public HumanCRUDTribeLocal getHumanCRUDTribeLocal();

}
