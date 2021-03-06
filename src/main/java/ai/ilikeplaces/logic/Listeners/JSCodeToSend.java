package ai.ilikeplaces.logic.Listeners;

import ai.ilikeplaces.util.ExceptionCache;
import ai.scribble.License;

/**
 * @author Ravindranath Akila
 */
@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
final public class JSCodeToSend {
// ------------------------------ FIELDS ------------------------------

    final static public String FnEventMonitor = "\ndocument.monitor = new EventMonitor();document.getItsNatDoc().addEventMonitor(document.monitor);";
    final static public String LocationId = "locationId";
    final static public String FnLocationId = "\ngetLocationId = function(){return document.getElementById('" + LocationId + "').value;}\n";
    final static public String LocationName = "locationName";
    final static public String FnLocationName = "\ngetLocationName = function(){return document.getElementById('" + LocationName + "').value;}\n";
    final static public String FnSetTitle = "\ndocument.title=\"Escape to \"+getLocationName()+\"!\";\n";
    final static public String RefreshPage = "\nwindow.location.reload(true);\n";
    final static public String ClosePage = "\nif(confirm('Done! Close page?')){window.close();}\n";
    final static public String ClosePageOrRefresh = "\nif(confirm('Done! Close page?')){window.close();}else{window.location.href = window.location.href;}\n";
    final static public String UpdateDocument = "\nJUpdateDocument();\n";
    private static final String STRING = "#";

// --------------------------- CONSTRUCTORS ---------------------------

    private JSCodeToSend() throws IllegalAccessException {
        throw ExceptionCache.STATIC_USAGE_ONLY_EXCEPTION;
    }

// -------------------------- OTHER METHODS --------------------------

    public static String clearContent(final String elementId, final int timeout) {
        final String statement = "$(\\'#" + elementId + "\\').empty().text(\\' \\');";
        return embedThisInTimeout(statement, timeout);
    }

    public static String embedThisInTimeout(final String statement, final int timeout) {
        return "setTimeout('" + statement + "'" + "," + timeout + ");\n";
    }

    public static String jqueryHide(final String elementToAnimateHide) {
        return "\n$('#" + elementToAnimateHide + "').hide('slow');\n";
    }

    /**
     * Uses window.location.href = 'urlTobeRedirectedTo';
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param urlTobeRedirectedTo
     * @return
     */
    public static String redirectPageWithURL(final String urlTobeRedirectedTo) {
        return "\nwindow.location.href = '" + urlTobeRedirectedTo + "';\n";
    }

    /**
     * Uses window.location.href = window.location.href + 'stringToBeAppended' after the given timeout.
     * Uses setTimeout to timeout.
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param timeout
     * @return
     */
    public static String refreshPageIn(final int timeout) {
        return "setTimeout('" + "window.location.reload(true)'" + "," + timeout + ");\n";
    }

    /**
     * Uses window.location.href = window.location.href + 'stringToBeAppended'
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param stringToBeAppended
     * @return
     */
    public static String refreshPageWith(final String stringToBeAppended) {
        return "window.location.href=window.location.href" + "+" + "'" + stringToBeAppended + "';\n";
    }

    /**
     * Uses window.location.hash = 'stringToBeAppended'
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param stringToBeAppended
     * @return
     */
    public static String updateHashWith(final String stringToBeAppended) {
        return "window.location.hash=" + "'" + stringToBeAppended + "';\n";
    }

    /**
     * Uses window.location.hash = 'stringToBeAppended'; prior to that, sets hash to #
     * Might turn out to be buggy if there are outer quotes. Verify with firebug for safety.
     *
     * @param stringToBeAppended
     * @return
     */
    public static String resetHashWith(final String stringToBeAppended) {
        return "window.location.hash=" + "'#'" + ";"
                + "window.location.hash=" + "'" + stringToBeAppended + "';\n";
    }

}
