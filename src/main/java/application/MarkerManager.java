/** Class to manage Markers on the Map
 */

package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/*import gmapsfx.javascript.event.UIEventType;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.Marker;
import gmapsfx.javascript.object.MarkerOptions;*/
import com.lynden.gmapsfx.javascript.JavascriptObject;
import javafx.scene.control.Button;
//import gmapsfx.javascript.object.LatLongBounds;
import netscape.javascript.JSObject;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.javascript.event.UIEventType;

public class MarkerManager extends JavascriptObject {

    private HashMap<geography.GeographicPoint, Marker> markerMap;
    private ArrayList<geography.GeographicPoint> markerPositions;
    private GoogleMap map;
    protected static String startURL = "http://maps.google.com/mapfiles/kml/pal3/icon40.png";
    protected static String destinationURL = "http://maps.google.com/mapfiles/kml/pal2/icon5.png";
    protected static String SELECTED_URL = "http://maps.google.com/mapfiles/kml/paddle/ltblu-circle.png";
    protected static String markerURL = "http://maps.google.com/mapfiles/kml/paddle/blu-diamond-lv.png";
	protected static String visURL = "http://maps.google.com/mapfiles/kml/paddle/red-diamond-lv.png";
    private Marker startMarker;
    private Marker destinationMarker;
    private Marker selectedMarker;
    private DataSet dataSet;
    private LatLongBounds bounds;
    private SelectManager selectManager;
    private RouteVisualization rv;
    private Button vButton;
    private boolean selectMode = true;

    public MarkerManager() {
    	markerMap = new HashMap<geography.GeographicPoint, Marker>();
    	this.map = null;
    	this.selectManager = null;
        this.rv = null;
        markerPositions = null;
    }

    /**
     * Used to set reference to visualization button. Manager will be responsible
     * for disabling button
     *
     * @param vButton
     */
    public void setVisButton(Button vButton) {
    	this.vButton = vButton;
    }

    public void setSelect(boolean value) {
    	selectMode = value;
    }
    public RouteVisualization getVisualization() { return rv; }

    public GoogleMap getMap() { return this.map; }
    public void setMap(GoogleMap map) { this.map = map; }
    public void setSelectManager(SelectManager selectManager) { this.selectManager = selectManager; }

    public void putMarker(geography.GeographicPoint key, Marker value) {
    	markerMap.put(key, value);

    }

    /** Used to initialize new RouteVisualization object
     *
     */
    public void initVisualization() {
    	rv = new RouteVisualization(this);
    }

    public void clearVisualization() {
        rv.clearMarkers();
    	rv = null;
    }

    public void startVisualization() {
    	if(rv != null) {
	    	rv.startVisualization();
    	}
    }

    public void setStart(geography.GeographicPoint point) {
    	if(startMarker!= null) {
            changeIcon(startMarker, markerURL);
    	}
        startMarker = markerMap.get(point);
        changeIcon(startMarker, startURL);
    }
    public void setDestination(geography.GeographicPoint point) {
    	if(destinationMarker != null) {
    		//destinationMarker.setIcon(markerURL);
            setIcon(destinationMarker, markerURL);
    	}
        destinationMarker = markerMap.get(point);
        changeIcon(destinationMarker, destinationURL);
    }

    public void changeIcon(Marker marker, String url) {
        marker.setVisible(false);
        setIcon(marker, url);
        marker.setVisible(true);
    }
    public void setIcon(Marker marker, String markerURL) {

        invokeJavascript("setIcon", markerURL);
        this.setProperty("icon", markerURL);
    }

    public void restoreMarkers() {
    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            // destination marker needs to be added because it is added in javascript
            if(marker != startMarker) {
                marker.setVisible(false);
                marker.setVisible(true);
            }
        }
        selectManager.resetSelect();
    }

    public void refreshMarkers() {

    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
        	Marker marker = markerMap.get(it.next());
        	marker.setVisible(true);
        }
    }
    public void clearMarkers() {
        if(rv != null) {
        	rv.clearMarkers();
        	rv = null;
        }
    	Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
    	while(it.hasNext()) {
    		markerMap.get(it.next()).setVisible(false);
    	}
    }

    public void setSelectMode(boolean value) {
        if(!value) {
        	selectManager.clearSelected();
        }
    	selectMode = value;
    }

    public boolean getSelectMode() {
    	return selectMode;
    }
    public static MarkerOptions createDefaultOptions(LatLong coord) {
        	MarkerOptions markerOptions = new MarkerOptions();
        	markerOptions.animation(null)
        				 .icon(markerURL)
        				 .position(coord)
                         .title(null)
                         .visible(true);
        	return markerOptions;
    }

    public void hideIntermediateMarkers() {
        Iterator<geography.GeographicPoint> it = markerMap.keySet().iterator();
        while(it.hasNext()) {
            Marker marker = markerMap.get(it.next());
            if(marker != startMarker && marker != destinationMarker) {
                marker.setVisible(false);
            }
        }
    }

    public void hideDestinationMarker() {
    	destinationMarker.setVisible(false);
    }

    public void displayDataSet() {
        markerPositions = new ArrayList<geography.GeographicPoint>();
        dataSet.initializeGraph();
    	Iterator<geography.GeographicPoint>it = dataSet.getIntersections().iterator();
        bounds = new LatLongBounds();
        while(it.hasNext()) {
        	geography.GeographicPoint point = it.next();
            LatLong ll = new LatLong(point.getX(), point.getY());
        	MarkerOptions markerOptions = createDefaultOptions(ll);

            //todo maria
			/*public LatLongBounds extend(LatLong point) {
				Object obj = invokeJavascript("extend", point);
				return new LatLongBounds((JSObject)obj);
			}*/
            System.out.println("EXTEND");
            Object obj = invokeJavascript("extend", ll);
            bounds = new LatLongBounds((JSObject)obj);

            //bounds.extend(ll);
        	Marker marker = new Marker(markerOptions);
            registerEvents(marker, point);
        	map.addMarker(marker);
        	putMarker(point, marker);
        	markerPositions.add(point);
        }
        map.fitBounds(bounds);
    }

    private void registerEvents(Marker marker, geography.GeographicPoint point) {

        map.addUIEventHandler(marker, UIEventType.click, (JSObject o) -> {
            if(selectMode) {
                	if(selectedMarker != null && selectedMarker != startMarker
                	   && selectedMarker != destinationMarker) {
                		//selectedMarker.setIcon(markerURL);
                        setIcon(selectedMarker, markerURL);
                	}
            	selectManager.setPoint(point, marker);
                selectedMarker = marker;
                setIcon(selectedMarker, SELECTED_URL);
                //selectedMarker.setIcon(SELECTED_URL);
            }
        });
    }

    public void disableVisButton(boolean value) {
    	if(vButton != null) {
	    	vButton.setDisable(value);
    	}
    }
	public void setDataSet(DataSet dataSet) {
		this.dataSet= dataSet;
	}


    public DataSet getDataSet() { return this.dataSet; }
}
