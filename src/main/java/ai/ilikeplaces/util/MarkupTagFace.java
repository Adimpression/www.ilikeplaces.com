package ai.ilikeplaces.util;

/**
 * @author Ravindranath Akila
 */
public interface MarkupTagFace {

    public String id();

    public String style();

    public String value();

    public String type();

    public String typeValueText();

    public String typeValueSelect();

    public String typeValueHidden();

    /**
     * Exact String Representation of Enum.
     * Case sensitive as XML markup could be added here in future
     *
     * @return String representation of Tag
     */
    @Override
    public String toString();


    public String href();

    public String alt();

    public String src();

    public String tag();
}