package ai.ilikeplaces.logic.Listeners.widgets;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.HumansAuthentication;
import ai.ilikeplaces.entities.HumansIdentity;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.logic.Listeners.JSCodeToSend;
import ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControls;
import ai.ilikeplaces.logic.Listeners.widgets.autoplay.AutoplayControlsCriteria;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.mail.SendMail;
import ai.ilikeplaces.logic.role.HumanUser;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.logic.validators.unit.SimpleString;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.servlets.Controller;
import ai.ilikeplaces.servlets.Controller.Page;
import ai.ilikeplaces.servlets.ServletLogin;
import ai.ilikeplaces.servlets.filters.ProfileRedirect;
import ai.ilikeplaces.util.*;
import ai.reaver.Return;
import ai.scribble.License;
import ai.scribble.WARNING;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.event.NodePropertyTransport;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpSession;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
abstract public class SignInOn extends AbstractWidgetListener<SignInOnCriteria> {
// ------------------------------ FIELDS ------------------------------

    private static final String TRUE = "true";

    private static final String ISSIGNUP = "issignup";

    private HumanId username = null;

    private Password password = null;

    private SimpleString dbHash = null;

    private SimpleString dbSalt = null;

    private Obj<Boolean> userOk = null;

    private Obj<Boolean> existButNotActive = null;

// -------------------------- ENUMERATIONS --------------------------

    public static enum SignInOnIds implements WidgetIds {
        signinon_login,
        signinon_logon,
        signinonUsername,
        signinonPassword,
        signinonSubmit,
        signinonNotice,
        signinon_autoplay,
        Global_Profile_Link,
        signinonBG,

        signinonFacebook,
        signinon_power_off, signinon_power_on, signinonGoogle
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * @param request__
     * @param signInOnCriteria__
     * @param appendToElement__
     */
    public SignInOn(final ItsNatServletRequest request__, final Element appendToElement__, final SignInOnCriteria signInOnCriteria__) {
        super(request__, Page.SignInOn, signInOnCriteria__, appendToElement__);
    }

// ------------------------ CANONICAL METHODS ------------------------

    // ------------------------ OVERRIDING METHODS ------------------------
    @WARNING(warning = "During work ont this class, from time to time you'll want to make it a WEB 2.0 login." +
            "However, remember that having a login on a separate servlet is better for security.  HTTPS support." +
            "Also, note that we have a policy of redirecting the user to the refer. " +
            "You will also have to make sure both online and offline modes are available.")
    @Override
    protected void init(final SignInOnCriteria signInOnCriteria) {
        registerUserNotifier($$(SignInOnIds.signinonNotice));

        criteria.username = signInOnCriteria.getHumanId();
        criteria.password = new Password();
        criteria.dbHash = new SimpleString("");
        criteria.dbSalt = new SimpleString("");
        criteria.userOk = new Obj<Boolean>(false);
        criteria.existButNotActive = new Obj<Boolean>(false);

        UCSetCSSBasedOnURL:
        {
            $$setClass($$(SignInOnIds.signinonBG), criteria.getSignInOnDisplayComponent().toString(), false);
            $(Controller.Page.CPageType).setAttribute(MarkupTag.INPUT.value(), criteria.getSignInOnDisplayComponent().toString());
        }

        if (criteria.username.validate() == 0) {
            UCSetGlobalProfileLink:
            {
                final HumansIdentity hi = ai.ilikeplaces.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE.get(criteria.username.getObjectAsValid(), "");

                $$(SignInOnIds.Global_Profile_Link).setAttribute(MarkupTag.A.href(), ProfileRedirect.PROFILE_URL + hi.getUrl().getUrl());
            }
            UCShowHideWidgets:
            {
                $$displayBlock(SignInOnIds.signinon_logon);
                $$displayNone(SignInOnIds.signinon_login);
                $$displayBlock(SignInOnIds.signinon_power_on);
                $$displayNone(SignInOnIds.signinon_power_off);
            }

            UCAutoplay:
            {
                new AutoplayControls(request, new AutoplayControlsCriteria()
                        .setHumanId(criteria.username)
                        .setHumanUserLocal($$getHumanUserFromRequest(request))
                        , $$(SignInOnIds.signinon_autoplay));
            }
        } else {
            UCShowHIdeWIdgets:
            {
                $$displayBlock(SignInOnIds.signinon_login);
                $$displayNone(SignInOnIds.signinon_logon);
                $$displayBlock(SignInOnIds.signinon_power_off);
                $$displayNone(SignInOnIds.signinon_power_on);
            }
            UCShowPleaseLoginMessage:
            {
                $$(SignInOnIds.signinonNotice).setTextContent(RBGet.gui().getString("not.logged.in"));
            }
        }
    }

    @Override
    protected void registerEventListeners(final ItsNatHTMLDocument itsNatHTMLDocument_, final HTMLDocument hTMLDocument_) {
        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonUsername), EventType.CHANGE.toString(), new EventListener() {

            @Override
            public void handleEvent(final Event evt_) {
                synchronized (criteria) {
                    //because the user might change this value once hash and everything is set. This is a critical bug fix. Remove only if you know what you are doing!
                    if (!criteria.userOk.getObj()) {
                        criteria.username.setObj($$(evt_).getAttribute(MarkupTag.INPUT.value()));
                        if (criteria.username.validate() == 0) {
                            Human existingUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(criteria.username.getObjectAsValid());
                            if (existingUser != null
                                    && existingUser.getHumanAlive()
                                    ) {/*Ok user name valid but now we check for criteria.password*/
                                final HumansAuthentication humansAuthentication = DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(criteria.username).returnValue();
                                criteria.dbHash.setObj(humansAuthentication.getHumanAuthenticationHash());
                                criteria.dbSalt.setObj(humansAuthentication.getHumanAuthenticationSalt());
                                criteria.userOk.setObj(true);
                            } else if (existingUser != null && !existingUser.getHumanAlive()) {/*Ok criteria.password wrong or not activated. What do we do with this guy? First lets make his session object null*/
                                criteria.userOk.setObj(false);
                                criteria.existButNotActive.setObj(true);
                            }
                        }
                    }
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));

        itsNatHTMLDocument_.addEventListener((EventTarget) $$(SignInOnIds.signinonPassword), EventType.CHANGE.toString(), new EventListener() {

            @Override
            public void handleEvent(final Event evt_) {
                synchronized (criteria) {
                    criteria.password.setObj($$(evt_).getAttribute(MarkupTag.INPUT.value()));
                }
            }
        }, false, new NodePropertyTransport(MarkupTag.TEXTAREA.value()));


        super.registerForClick(SignInOnIds.signinonSubmit, new AIEventListener<SignInOnCriteria>(criteria) {
            @Override
            public void onFire(Event evt) {
                notifyUser(RBGet.gui().getString("checking.credentials"));
            }
        });


        super.registerForClick(SignInOnIds.signinonSubmit,
                new AIEventListener<SignInOnCriteria>(criteria) {

                    @Override
                    public void onFire(final Event evt_) {

                        synchronized (criteria) {
                            final HttpSession userSession_ = ((ItsNatHttpSession) request.getItsNatSession()).getHttpSession();

                            if (criteria.username.validate() == 0 && criteria.password.validate() == 0) {
                                if (userSession_.getAttribute(HumanUserLocal.NAME) == null) {
                                    /*Ok the session does not have the bean, initialize it with the user with email id and criteria.password*/
                                    if (criteria.userOk.getObj()) {/*Ok user name valid but now we check for criteria.password*/
                                        if (criteria.dbHash.getObj().equals(DB.getSingletonHashingFaceLocal(true).getHash(criteria.password.getObjectAsValid(), criteria.dbSalt.getObj()))) {
                                            final HumanUserLocal humanUserLocal = HumanUser.getHumanUserLocal(true);

                                            humanUserLocal.setHumanUserId(criteria.username.getObjectAsValid());

                                            userSession_.setAttribute(HumanUserLocal.NAME, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, userSession_)));

                                            userSession_.setAttribute(ServletLogin.Username, criteria.username.getHumanId());

                                            notifyUser(RBGet.gui().getString("logging.you.in"));

                                            switch (criteria.getSignInOnDisplayComponent()) {
                                                case HOME: {
                                                    $$sendJS(JSCodeToSend.redirectPageWithURL(ProfileRedirect.PROFILE_URL
                                                            + ai.ilikeplaces.logic.Listeners.widgets.UserProperty.HUMANS_IDENTITY_CACHE.get(criteria.username.getObjectAsValid(), "").getUrl().getUrl()));
                                                    break;
                                                }
                                                default: {
                                                    $$sendJS(JSCodeToSend.refreshPageIn(0));
                                                }
                                            }
                                        } else {/*Ok criteria.password wrong or not activated. What do we do with this guy? First lets make his session object null*/
                                            Loggers.log(Loggers.LEVEL.FAILED_LOGINS, criteria.username.getHumanId() + ":" + criteria.password.getObj());
                                            notifyUser(RBGet.gui().getString("password.is.wrong"));
                                        }
                                    } else {/*There is no such user. Ask if he forgot criteria.username or whether to create a new account :)*/
                                        if (criteria.existButNotActive.getObj()) {
                                            $$sendJS(JSCodeToSend.redirectPageWithURL(Page.Profile.getURL()));
                                            notifyUser(RBGet.gui().getString("activate.your.account"));
                                        } else {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final Return<Boolean> returnVal = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                                            criteria.username,
                                                            criteria.password,
                                                            new Email().setObjAsValid(criteria.username.getSelfAsValid().getHumanId()));

                                                    final String activationURL = new Parameter("http://www.ilikeplaces.com/" + "activate")
                                                            .append(ServletLogin.Username, criteria.username, true)
                                                            .append(ServletLogin.Password,
                                                                    DB.getHumanCRUDHumanLocal(true).doDirtyRHumansAuthentication(criteria.username)
                                                                            .returnValue()
                                                                            .getHumanAuthenticationHash())
                                                            .get();
                                                    final String mail = MessageFormat.format(RBGet.gui().getString("SIGNUP_BODY"), RBGet.globalConfig.getString("noti_mail"))
                                                            .replace("activationURL", "<a href='" +
                                                                    activationURL + "' >" + "here" + "</a>.");

                                                    try {
                                                        final String frame = HTMLDocParser.getDocumentAsString(Controller.REAL_PATH + Controller.WEB_INF_PAGES + "ai/ilikeplaces/EmailFrame.xhtml");

                                                        final String fullEmail = frame.replace("_FrameContent_", mail);


                                                        SendMail.getSendMailLocal().sendAsHTMLAsynchronously(
                                                                criteria.username.getSelfAsValid().getHumanId(),
                                                                RBGet.gui().getString("SIGNUP_HEADER"),
                                                                fullEmail);
                                                    } catch (IOException e) {

                                                        throw new RuntimeException(e);
                                                    } catch (SAXException e) {
                                                        throw new RuntimeException(e);
                                                    } catch (TransformerException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            }).start();

                                            $$sendJSStmt(JSCodeToSend.redirectPageWithURL(Controller.Page.Activate.getURL()));
                                        }
                                    }
                                } else {
                                    //We just ignore since the form is not visible and this cannot happen, or a hacker is trying something ;)
                                }
                            } else {
                                notifyUser(RBGet.gui().getString("login.failed.retry.or.recover"));
                            }
                        }
                    }
                });

        super.registerForClick(SignInOnIds.signinonFacebook, new AIEventListener<SignInOnCriteria>(criteria) {//Google Analytics
            /**
             * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
             *
             * @param evt fired from client
             */
            @Override
            protected void onFire(Event evt) {
                $$sendJS(JSCodeToSend.redirectPageWithURL("/oauth2fb"));
            }
        });

        super.registerForClick(SignInOnIds.signinonGoogle, new AIEventListener<SignInOnCriteria>(criteria) {//Google Analytics
            /**
             * Override this method and avoid {@link #handleEvent(org.w3c.dom.events.Event)} to make debug logging transparent
             *
             * @param evt fired from client
             */
            @Override
            protected void onFire(Event evt) {
                $$sendJS(JSCodeToSend.redirectPageWithURL("/oauth2gg?state=login"));
            }
        });
    }
}
