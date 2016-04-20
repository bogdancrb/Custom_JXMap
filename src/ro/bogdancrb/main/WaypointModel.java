package ro.bogdancrb.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

public class WaypointModel extends Waypoint
{
	protected double longitude;
	protected double latitude;
	private String description;
	
	public WaypointModel()
	{
		longitude = 0;
		latitude  = 0;
	}
	
	/**
	 * this draws on the map at clicked location . 
	 * @param g
	 */
	public void drawWaypoint(double latitude, double longitude, WaypointPainter waypointPainter, JXMapKit map) 
	{
		this.latitude = latitude;
		this.longitude = longitude;
		
		waypointPainter.getWaypoints().add(new Waypoint(latitude, longitude));

		map.getMainMap().setOverlayPainter(waypointPainter);
	}
	
	public void removeWaypoint(WaypointPainter waypointPainter, double latitude, double longitude)
	{
		Object wp = new Object(); 
		Iterator it = waypointPainter.getWaypoints().iterator();
		
		while (it.hasNext())
		{
			wp = it.next();
			
			if (((Waypoint)wp).getPosition().getLatitude() == latitude &&
				((Waypoint)wp).getPosition().getLongitude() == longitude)
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
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
