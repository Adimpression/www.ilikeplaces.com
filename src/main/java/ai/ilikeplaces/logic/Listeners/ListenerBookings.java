package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.doc.License;
import ai.ilikeplaces.logic.Listeners.widgets.SignInOn;
import ai.ilikeplaces.logic.validators.unit.HumanId;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.util.AbstractListener;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.MarkupTag;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import java.util.ResourceBundle;

import static ai.ilikeplaces.servlets.Controller.Page.*;

/**
 * @author Ravindranath Akila
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
public class ListenerBookings implements ItsNatServletRequestListener {

    final private Logger logger = LoggerFactory.getLogger(ListenerBookings.class.getName());

    /**
     * @param request__
     * @param response__
     */
    @Override
    public void processRequest(final ItsNatServletRequest request__, final ItsNatServletResponse response__) {

        new AbstractListener(request__) {

            /**
             * Intialize your document here by appending fragments
             */
            @Override
            @SuppressWarnings("unchecked")
            protected final void init(final ItsNatHTMLDocument itsNatHTMLDocument__, final HTMLDocument hTMLDocument__, final ItsNatDocument itsNatDocument__) {
                itsNatDocument.addCodeToSend(JSCodeToSend.FnEventMonitor);

                final ResourceBundle gUI = ResourceBundle.getBundle("ai.ilikeplaces.rbs.GUI");

                layoutNeededForAllPages:
                {
                    setLoginWidget:
                    {
                        try {
                            new SignInOn(itsNatDocument__, $(Skeleton_login_widget), new HumanId(getUsername()), request__.getServletRequest()) {
                            };
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("{}", t);
                        }
                    }

                    signOnDisplayLink:
                    {
                        try {
                            if (getUsername() != null) {
                                final Element usersName = $(MarkupTag.P);
                                usersName.setTextContent(gUI.getString("ai.ilikeplaces.logic.Listeners.ListenerMain.0004") + getUsername());
                                $(Skeleton_othersidebar_identity).appendChild(usersName);
                            } else {
                                final Element locationElem = $(MarkupTag.P);
                                locationElem.setTextContent(gUI.getString("NoLogin"));
                                $(Skeleton_othersidebar_identity).appendChild(locationElem);
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("{}", t);
                        }

                    }
                    setProfileLink:
                    {
                        try {
                            if (getUsername() != null) {
                                $(Skeleton_othersidebar_profile_link).setAttribute("href", Controller.Page.PhotoCRUD.getURL());
                            } else {
                                $(Skeleton_othersidebar_profile_link).setAttribute("href", Controller.Page.signup.getURL());
                            }
                        } catch (final Throwable t) {
                            Loggers.EXCEPTION.error("{}", t);

                        }
                    }
                }
                if (getUsername() != null) {
                    try {
                        //Page content

                    } catch (final Throwable t) {
                        Loggers.EXCEPTION.error("{}", t);
                    }
                }
            }

            /**
             * Use ItsNatHTMLDocument variable stored in the AbstractListener class
             */
            @Override
            protected void registerEventListeners(
                    final ItsNatHTMLDocument itsNatHTMLDocument__,
                    final HTMLDocument hTMLDocument__,
                    final ItsNatDocument itsNatDocument__) {
            }
        };
    }
}