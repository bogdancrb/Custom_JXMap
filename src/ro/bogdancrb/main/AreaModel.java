package ro.bogdancrb.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class AreaModel extends WaypointModel
{
	private Object lastWaypoint;
	private int nrPointsInsideRadius;
	
	public AreaModel(JXMapKit map) 
	{
		super();
		
		nrPointsInsideRadius = 0;
	}
	
	public void drawWaypoint(double latitude, double longitude, WaypointPainter waypointPainter, JXMapKit map) 
	{
		lastWaypoint = new Waypoint(this.latitude, this.longitude);
		
		this.latitude = latitude;
		this.longitude = longitude;
		
		this.removeOldWaypoint(waypointPainter);
		
		super.drawWaypoint(latitude, longitude, waypointPainter, map);
	}
	
	public void removeOldWaypoint(WaypointPainter waypointPainter, double latitude, double longitude)
	{
		lastWaypoint = new Waypoint(latitude, longitude);
		
		this.removeOldWaypoint(waypointPainter);
	}
	
	public void removeOldWaypoint(WaypointPainter waypointPainter)
	{
		Object wp = new Object(); 
		Iterator it = waypointPainter.getWaypoints().iterator();
		
		while (it.hasNext())
		{
			wp = it.next();
			
			if (((Waypoint)wp).getPosition().getLatitude() == ((Waypoint)lastWaypoint).getPosition().getLatitude() &&
				((Waypoint)wp).getPosition().getLongitude() == ((Waypoint)lastWaypoint).getPosition().getLongitude())
			{
				waypointPainter.getWaypoints().remove(wp);
				break;
			}
		}
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setNrPointsInsideRadius(int nrPointsInsideRadius)
	{
		this.nrPointsInsideRadius = nrPointsInsideRadius;
	}
}
