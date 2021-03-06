package ai.ilikeplaces.servlets;

import ai.ilikeplaces.entities.Human;
import ai.ilikeplaces.entities.etc.HumanId;
import ai.ilikeplaces.exception.ConstructorInvokationException;
import ai.ilikeplaces.logic.Listeners.widgets.Bate;
import ai.ilikeplaces.logic.contactimports.google.GoogleContactImporter;
import ai.ilikeplaces.logic.crud.DB;
import ai.ilikeplaces.logic.role.HumanUser;
import ai.ilikeplaces.logic.role.HumanUserLocal;
import ai.ilikeplaces.logic.validators.unit.Email;
import ai.ilikeplaces.logic.validators.unit.Password;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.security.face.SingletonHashingRemote;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.Parameter;
import ai.ilikeplaces.util.SessionBoundBadRefWrapper;
import ai.reaver.Return;
import ai.scribble.WARNING;
import com.google.gdata.data.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 1/7/12
 * Time: 12:22 PM
 */
public class ServletOAuthGoogle extends AbstractOAuth {
    private static final RuntimeException JSONERROR = new RuntimeException("ERROR IN GETTING EMAIL FROM JSON OBJECT.");
    private static final String JSON_ERROR = "JSON ERROR";
    private static final String NAMING_ERROR = "NAMING ERROR";
    private static final String IO_ERROR = "IO ERROR";
    private static final String DB_ERROR = "DB ERROR";
    // ------------------------ CANONICAL METHODS ------------------------

    @Override
    OAuthProvider oAuthProvider() {
        return new OAuthProvider(
                "https://accounts.google.com/o/oauth2/auth",
                "https://accounts.google.com/o/oauth2/auth",
                new OAuthAuthorizationRequest(
                        "code",
                        "796688826799.apps.googleusercontent.com",
                        "http://www.ilikeplaces.com/oauth2gg",
                        "https://www.google.com/m8/feeds",
                        null
                ));
    }


    final Logger logger = LoggerFactory.getLogger(ServletLogin.class.getName());
    final private Properties p_ = new Properties();
    private Context context = null;
    private SingletonHashingRemote singletonHashingRemote = null;
    private static final String HEADER_REFERER = "Referer";

    final PageFace home = Controller.Page.home;

    @Override
    @SuppressWarnings("unchecked")
    public void init() {
        boolean initializeFailed = true;
        final StringBuilder log = new StringBuilder();
        init:
        {
            try {
                p_.put(Context.INITIAL_CONTEXT_FACTORY, RBGet.globalConfig.getString("oejb.RICF"));
                p_.put(Context.PROVIDER_URL, RBGet.globalConfig.getString("RICF_LOCATION"));

                context = new InitialContext(p_);

                singletonHashingRemote = (SingletonHashingRemote) context.lookup(SingletonHashingRemote.NAME);

                if (singletonHashingRemote == null) {
                    log.append("\nVARIABLE singletonHashingRemote IS NULL! ");
                    log.append(singletonHashingRemote);
                    break init;
                }
            } catch (NamingException ex) {
                log.append("\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!");
                logger.info("\nCOULD NOT INITIALIZE SIGNUP SERVLET DUE TO A NAMING EXCEPTION!", ex);
                break init;
            }

            /**
             *
             * break. Do not let this statement be reachable if initialization
             * failed. Instead, break immediately where initialization failed.
             * At this point, we set the initializeFailed to false and thereby,
             * allow initialization of an instance
             */
            initializeFailed = false;
        }
        if (initializeFailed) {
            throw new ConstructorInvokationException(log.toString());
        }
    }

    /**
     * <b>code</b>
     * <p/>
     * REQUIRED.  The authorization code generated by the
     * authorization server.  The authorization code MUST expire
     * shortly after it is issued to mitigate the risk of leaks.  A
     * maximum authorization code lifetime of 10 minutes is
     * RECOMMENDED.  The client MUST NOT use the authorization code
     * more than once.  If an authorization code is used more than
     * once, the authorization server MUST deny the request and SHOULD
     * attempt to revoke all tokens previously issued based on that
     * authorization code.  The authorization code is bound to the
     * client identifier and redirection URI.
     * <p/>
     * <b>state</b>
     * <p/>
     * REQUIRED if the "state" parameter was present in the client
     * authorization request.  The exact value received from the
     *
     * @param request
     * @param response
     */
    @Override
    void processRequest(final HttpServletRequest request, final HttpServletResponse response) {
        final OAuthAuthorizationResponse oAuthAuthorizationResponse = this.getOAuthAuthorizationResponse(request, response);
        if (oAuthAuthorizationResponse != null) {
            Loggers.debug(oAuthAuthorizationResponse.toString());

            final OAuthAccessTokenResponse oAuthAccessTokenResponse = this.getOAuthAccessTokenResponse(request, response, oAuthAuthorizationResponse,
                    new ClientAuthentication("796688826799.apps.googleusercontent.com", "lHiQ5yEkEBVhfXEHZirmgY3i", "http://www.ilikeplaces.com/oauth2gg"));

            Loggers.debug(oAuthAccessTokenResponse != null ? oAuthAccessTokenResponse.toString() : "");

            if (oAuthAccessTokenResponse != null) {

                final Person person = GoogleContactImporter.fetchAuthor(Bate.DEFAULT, oAuthAccessTokenResponse.access_token);

                Loggers.debug(person.toString());

                Human existingUser = null;
                try {
                    final String email = person.getEmail();

                    Loggers.debug(email);

                    existingUser = DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(email);

                    if (existingUser != null) {

                        ActivateAccountAsFBWouldHaveVerifiedUser:
                        {
                            if (!existingUser.getHumanAlive()) {
                                Loggers.debug("Activating User");
                                DB.getHumanCRUDHumanLocal(true).doUActivateHuman(new HumanId(existingUser.getHumanId()).getSelfAsValid());
                            }
                        }

                        loginUser(request, response, existingUser);

                    } else {
                        Loggers.debug("Creating New User");

                        final Return<Boolean> humanCreateReturn = DB.getHumanCRUDHumanLocal(true).doCHuman(
                                new HumanId().setObjAsValid(email),
                                new Password(Bate.getRandomPassword()),
                                new Email(email));

                        if (humanCreateReturn.valid()) {
                            loginUser(request, response, DB.getHumanCRUDHumanLocal(true).doDirtyRHuman(email));
                        } else {
                            //Hmmm. Backend would've logged this. We need to notify the user however.
                        }
                    }
                } catch (NamingException e) {
                    Loggers.error(NAMING_ERROR, e);
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Loggers.error(IO_ERROR, e);
                    throw new RuntimeException(e);
                }
            }
        } else {
            // we ignore since a redirect will happen
        }
    }

    /**
     * @param request
     * @param response
     * @return OAuthAuthorizationResponse or redirects user to endpoint and returns null
     */
    OAuthAuthorizationResponse getOAuthAuthorizationResponse(final HttpServletRequest request, final HttpServletResponse response) {
        final String code = request.getParameter(AbstractOAuth.code);
        final String state = request.getParameter(AbstractOAuth.state);

        if ((state != null && state.equals("login")) && (code == null || code.isEmpty())) {
            try {
                response.sendRedirect(
                        new Parameter(this.oAuthEndpoint)
                                .append(client_id, this.oAuthAuthorizationRequest.client_id, true)
                                .append(redirect_uri, this.oAuthAuthorizationRequest.redirect_uri)
                                .append(response_type, this.oAuthAuthorizationRequest.response_type)
                                .append(scope, this.oAuthAuthorizationRequest.scope)
                                .append(AbstractOAuth.state, this.oAuthAuthorizationRequest.state)
                                .get()

                );
            } catch (final IOException e) {
                //hmmm!
                throw RedirectToOAuthEndpointFailed;
            }
            return null;
        } else {
            return new OAuthAuthorizationResponse(code, state);
        }
    }

    @WARNING("DEVIATING FROM OAUTH IMPLEMENTATION JUST FOR GOOGLE. WE DON'T NEED code WE JUST NEED TO USE A CLIENT PAGE REFRESH TO GET THE access_token")
    OAuthAccessTokenResponse getOAuthAccessTokenResponse(final HttpServletRequest request, final HttpServletResponse response, final OAuthAuthorizationResponse oAuthAuthorizationResponse, final ClientAuthentication clientAuthentication) {
        final String access_token__ = request.getParameter(access_token);
        final String code__ = request.getParameter(code);

        Loggers.debug("################################ code: " + code__);
        Loggers.debug("################################ token: " + access_token__);

        if (code__ != null && !code__.isEmpty()) {
            try {
                response.sendRedirect(
                        new Parameter(this.oAuthEndpoint)
                                .append(client_id, this.oAuthAuthorizationRequest.client_id, true)
                                .append(redirect_uri, this.oAuthAuthorizationRequest.redirect_uri)
                                .append(response_type, "token")
                                .append(scope, this.oAuthAuthorizationRequest.scope)
                                .append(AbstractOAuth.state, "done")
                                .get()

                );
                return null;
            } catch (final IOException e) {
                //hmmm!
                throw RedirectToOAuthEndpointFailed;
            }
        } else {
            if (access_token__ != null && !access_token__.isEmpty()) {
                return new OAuthAccessTokenResponse(access_token__, "", "", "", "");
            } else {
                try {
                    final PrintWriter out = response.getWriter();
                    out.println("<html><head>");
                    out.println("        <script type=\"text/javascript\">\n" +
                            "            if(window.location.hash){\n" +
                            "                window.location.href = '?'+(window.location.hash + '').substring(1);\n" +
                            "            }\n" +
                            "        </script>");
                    out.println("</title>");
                    out.println("<body>");
                    out.println("</body></html>");
                    out.close();
                    return null;
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response, Human existingUser) throws NamingException, IOException {
        LoginTheUser:
        {

            Loggers.debug("Logging in User");

            final String ilp_destination = (String) request.getSession(true).getAttribute(ILP_DESTINATION);

            final HttpSession userSession_;
            handleHttpSession:
            {
                /**
                 * Remove any redundant session
                 */
                if (request.getSession(false) != null) {
                    request.getSession(false).invalidate();
                }

                /**
                 * Make user session anyway as he came to log in
                 */
                userSession_ = request.getSession();

                /**
                 * Set a timeout compatible with the stateful session bean handling user
                 */
                userSession_.setMaxInactiveInterval(Integer.parseInt(RBGet.globalConfig.getString("UserSessionIdleInterval")));
            }

            final HumanUserLocal humanUserLocal = HumanUser.getHumanUserLocal(true);
            humanUserLocal.setHumanUserId(existingUser.getHumanId());
            userSession_.setAttribute(ServletLogin.HumanUser, (new SessionBoundBadRefWrapper<HumanUserLocal>(humanUserLocal, userSession_)));

            response.sendRedirect(ilp_destination);
        }
    }
}

/*

https://www.facebook.com/dialog/oauth?client_id=139373316127498&redirect_uri=http://WWW.ilikeplaces.com/oauth2&scope=email

http://www.ilikeplaces.com/oauth2?code=AQAGQa3sr_AolE2ocDLLnq0NYDgJ8TYcATJpwHnC6XKQfDPbxBoxr0lfE9TO4V2l7YXXy35bY3pVmV69F5vM3bWROEyIPD6F5QNPptb2tLTPdQHZFiAEiUOwsrs3hgJDxStOhLRwFRslgRfhj8NZdIVYvZVGYs7w9yT06w6KoNaNIDwS0e2an9tp86QV5eh1mV8#_=_


https://graph.facebook.com/oauth/access_token?client_id=139373316127498&redirect_uri=http://WWW.ilikeplaces.com/oauth2&client_secret=56a2340af5eb11db36258f9f7a07b2b9&code=AQAGQa3sr_AolE2ocDLLnq0NYDgJ8TYcATJpwHnC6XKQfDPbxBoxr0lfE9TO4V2l7YXXy35bY3pVmV69F5vM3bWROEyIPD6F5QNPptb2tLTPdQHZFiAEiUOwsrs3hgJDxStOhLRwFRslgRfhj8NZdIVYvZVGYs7w9yT06w6KoNaNIDwS0e2an9tp86QV5eh1mV8#_=_


Without logging in, calling the above url gives

access_token=AAABZBwmDZCuwoBAEtNOfOITUpi0oJFCZAHD0KHOPNiyBErPMhu1k3eXa7RGrrPy789r4OgoZCqST53T9cJKIZBPDhb4yJj2NrSpbb8VbeTwZDZD&expires=4051

Without logging in means calling it from a cookieless session, like the server



Running this :
https://graph.facebook.com/me?access_token=AAABZBwmDZCuwoBAEtNOfOITUpi0oJFCZAHD0KHOPNiyBErPMhu1k3eXa7RGrrPy789r4OgoZCqST53T9cJKIZBPDhb4yJj2NrSpbb8VbeTwZDZD


Returns:


{
   "id": "726280855",
   "name": "Ravindranath Akila",
   "first_name": "Ravindranath",
   "last_name": "Akila",
   "link": "http://www.facebook.com/ravindranathakila",
   "username": "ravindranathakila",
   "location": {
      "id": "",
      "name": null
   },
   "gender": "male",
   "email": "ravindranathakila\u0040gmail.com",
   "timezone": 5.5,
   "locale": "en_US",
   "verified": true,
   "updated_time": "2011-12-24T13:54:01+0000"
}



https://graph.facebook.com/ravindranathakila

{
   "id": "726280855",
   "name": "Ravindranath Akila",
   "first_name": "Ravindranath",
   "last_name": "Akila",
   "username": "ravindranathakila",
   "gender": "male",
   "locale": "en_US"
}




*/


/*

final String parameter = request.getParameter(access_token);

if (parameter == null || parameter.isEmpty()) {
    try {
        final PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("        <script type=\"text/javascript\">\n" +
                "            if(window.location.hash){\n" +
                "                window.location.href = '?'+(window.location.hash + '').substring(1);\n" +
                "            }\n" +
                "        </script>");
        out.println("</title>");
        out.println("<body>");
        out.println("<body>");
        out.println("</body></html>");
    } catch (final IOException e) {
        throw new RuntimeException(e);
    }
}else{
    Loggers.info("######################### " + parameter);
}

*/
