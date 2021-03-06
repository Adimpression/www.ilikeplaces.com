package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.util.AbstractWidgetListener;
import ai.ilikeplaces.util.Loggers;
import ai.scribble.License;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class SearchBox extends AbstractWidgetListener {

    /**
     * @param itsNatDocument__
     * @param appendToElement__
     */
    public SearchBox(final ItsNatServletRequest request__, final Element appendToElement__) {
        super(request__, Page.Photo$Description, appendToElement__);
    }

    @Override
    protected void init(final Object... initArgs) {
    }

    @Override
    protected void registerEventListeners(ItsNatHTMLDocument itsNatHTMLDocument_, HTMLDocument hTMLDocument_) {
    }

    @Override
    public void finalize() throws Throwable {
        Loggers.finalized(this.getClass().getName());
        super.finalize();
    }
}
