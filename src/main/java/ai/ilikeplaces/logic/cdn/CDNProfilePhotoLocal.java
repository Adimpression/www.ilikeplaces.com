package ai.ilikeplaces.logic.cdn;

import ai.ilikeplaces.util.FileUploadListenerFace;
import ai.scribble.License;

import javax.ejb.Local;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:08:49 PM
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Local
public interface CDNProfilePhotoLocal extends FileUploadListenerFace<File> {

    public static final String NAME = CDNProfilePhotoLocal.class.getSimpleName();
}
