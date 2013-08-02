package ai.ilikeplaces.logic.Listeners.widgets.schema.thing;

/**
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 6/3/12
 * Time: 12:07 PM
 */
public class EventCriteria {

    private String eventName;

    private String eventStartDate;

    private String eventPhoto;

    private PlaceCriteria placeCriteria;


    public String getEventName() {
        return "" + eventName;
    }

    public EventCriteria setEventName(final String eventName) {
        this.eventName = eventName;//Returning empty if null
        return this;
    }

    public String eventStartTime() {
        return "" + eventStartDate;//Returning empty if null
    }

    public EventCriteria setEventStartDate(final String eventStartDate) {
        this.eventStartDate = eventStartDate;
        return this;
    }

    public PlaceCriteria getPlaceCriteria() {
        return placeCriteria == null ? new PlaceCriteria() : placeCriteria;
    }

    public EventCriteria setPlaceCriteria(final PlaceCriteria placeCriteria) {
        this.placeCriteria = placeCriteria;
        return this;
    }

    public String getEventPhoto() {
        return eventPhoto == null ? "" : eventPhoto;//Returning empty if null
    }

    public EventCriteria setEventPhoto(final String eventPhoto) {
        this.eventPhoto = eventPhoto;
        return this;
    }
}
