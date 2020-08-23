package application.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.function.Consumer;
import application.MapApp;
import application.MarkerManager;
import application.controllers.RouteController;
import geography.GeographicPoint;
import geography.RoadSegment;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.javascript.JavascriptObject;
import netscape.javascript.JSObject;

public class RouteService extends JavascriptObject{
	private GoogleMap map;
    private MarkerManager markerManager;
    private Polyline routeLine;

	public RouteService(GoogleMapView mapComponent, MarkerManager manager) {
		this.map = mapComponent.getMap();
        this.markerManager = manager;
	}

	/**
	 * Displays route on Google Map
	 * @return returns false if route fails to display
	 */
	private boolean displayRoute(List<LatLong> route) {

        if(routeLine != null) {
        	removeRouteLine();
        }
		routeLine = new Polyline();
		MVCArray path = new MVCArray();
		LatLongBounds bounds = new LatLongBounds();
		for(LatLong point : route)  {
			path.push(point);
			Object obj = invokeJavascript("extend", point);
			bounds = new LatLongBounds((JSObject)obj);
		}
		routeLine.setPath(path);
		map.addMapShape(routeLine);
		markerManager.hideIntermediateMarkers();
		map.fitBounds(bounds);
    	markerManager.disableVisButton(false);
		return true;
	}

    public void hideRoute() {
    	if(routeLine != null) {
        	map.removeMapShape(routeLine);
        	if(markerManager.getVisualization() != null) {
        		markerManager.clearVisualization();
        	}
            markerManager.restoreMarkers();
        	markerManager.disableVisButton(true);
            routeLine = null;
    	}
    }

    public boolean isRouteDisplayed() {
    	return routeLine != null;
    }
    public boolean displayRoute(GeographicPoint start, GeographicPoint end, int toggle) {
        if(routeLine == null) {
        	if(markerManager.getVisualization() != null) {
        		markerManager.clearVisualization();
        	}

        	if(toggle == RouteController.DIJ || toggle == RouteController.A_STAR ||
        			toggle == RouteController.BFS) {
        		markerManager.initVisualization();
            	Consumer<GeographicPoint> nodeAccepter = markerManager.getVisualization()::acceptPoint;
            	List<GeographicPoint> path = null;
            	if (toggle == RouteController.BFS) {
            		path = markerManager.getDataSet().getGraph().bfs(start, end, nodeAccepter);
            	}
            	else if (toggle == RouteController.DIJ) {
            		path = markerManager.getDataSet().getGraph().dijkstra(start, end, nodeAccepter);
            	}
            	else if (toggle == RouteController.A_STAR) {
            		path = markerManager.getDataSet().getGraph().aStarSearch(start, end, nodeAccepter);
            	}

            	if(path == null) {
                    MapApp.showInfoAlert("Routing Error : ", "No path found");
                	return false;
                }
               List<LatLong> mapPath = constructMapPath(path);
               markerManager.setSelectMode(false);
               return displayRoute(mapPath);
    		}

    		return false;
        }
        return false;
    }

    /**
     * Construct path including road regments
     * @param path - path with only intersections
     * @return list of LatLongs corresponding the path of route
     */
    private List<LatLong> constructMapPath(List<GeographicPoint> path) {
    	List<LatLong> retVal = new ArrayList<LatLong>();
        List<GeographicPoint> segmentList = null;
    	GeographicPoint curr;
    	GeographicPoint next;

    	RoadSegment chosenSegment = null;;

        for(int i = 0; i < path.size() - 1; i++) {
            double minLength = Double.MAX_VALUE;
        	curr = path.get(i);
        	next = path.get(i+1);

        	if(markerManager.getDataSet().getRoads().containsKey(curr)) {
        		HashSet<RoadSegment> segments = markerManager.getDataSet().getRoads().get(curr);
        		Iterator<RoadSegment> it = segments.iterator();

        		// get segments which are
            	RoadSegment currSegment;
                while(it.hasNext()) {
                    //System.out.println("new segment");
                	currSegment = it.next();
                	if(currSegment.getOtherPoint(curr).equals(next)) {
                        //System.out.println("1st check passed : other point correct");
                		if(currSegment.getLength() < minLength) {
                            //System.out.println("2nd check passed : length less");
                			chosenSegment = currSegment;
                		}
                	}
                }

                if(chosenSegment != null) {
                    segmentList = chosenSegment.getPoints(curr, next);
                    for(GeographicPoint point : segmentList) {
                        retVal.add(new LatLong(point.getX(), point.getY()));
                    }
                }
                else {
                	System.err.println("ERROR in constructMapPath : chosenSegment was null");
                }
        	}
        }
   	return retVal;
    }

	private void removeRouteLine() {
        if(routeLine != null) {
    		map.removeMapShape(routeLine);
        }
	}
}


