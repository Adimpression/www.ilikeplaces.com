package ai.ilikeplaces.logic.crud.unit;

import ai.ilikeplaces.rbs.RBGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.ilikeplaces.doc.*;
import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.Location;
import ai.ilikeplaces.entities.PublicPhoto;
import ai.ilikeplaces.jpa.CrudServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 *
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class DPublicPhoto implements DPublicPhotoLocal {

    @EJB
    private CrudServiceLocal<Human> crudServiceHuman_;
    @EJB
    private CrudServiceLocal<Location> crudServiceLocation_;
    @EJB
    private CrudServiceLocal<PublicPhoto> CrudServicePublicPhoto_;

    public DPublicPhoto() {
        logger.debug(RBGet.logMsgs.getString("common.Constructor.Init"), DPublicPhoto.class, this.hashCode());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean doNTxDPublicPhotoLocal(final long publicPhotoId) {
        final PublicPhoto publicPhoto = CrudServicePublicPhoto_.find(PublicPhoto.class, publicPhotoId);

        if (!publicPhoto.getHumansPublicPhoto().getPublicPhotos().remove(publicPhoto)) {
            throw new IllegalStateException("SORRY, I FIND NO SUCH VALUE.");
        }
        
        if (!publicPhoto.getLocation().getPublicPhotos().remove(publicPhoto)) {
            throw new IllegalStateException("SORRY, I FIND NO SUCH VALUE.");
        }

        CrudServicePublicPhoto_.delete(PublicPhoto.class, publicPhoto.getPublicPhotoId());

        return true;
    }
    final static Logger logger = LoggerFactory.getLogger(DPublicPhoto.class);
}