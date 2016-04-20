package ro.ucv.main.maps;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXMapKit;

import org.jdesktop.swingx.mapviewer.DefaultTileFactory;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.TileFactory;
import org.jdesktop.swingx.mapviewer.TileFactoryInfo;
/**
 * This class connects to the server tile.openstreetmap.org and requests maps with actual 
 * Longitude and Latitude coordinates
 *
 * The resulting picture is wrapped in a JComponent and returned to be incorporated in a JFrame 
 *
 *http://today.java.net/pub/a/today/2007/10/30/building-maps-into-swing-app-with-jxmapviewer.html
 *Note: contrary to what this site says Google Maps do not allow the use of their maps any longer.
 *
 */
public class SetupMap {
     
    public JComponent createOpenMap(GeoPosition userLocation) {
        JXMapKit map = new JXMapKit();
        final int max = 17;
        TileFactoryInfo info = new TileFactoryInfo(1,max-2,max,
                256, true, true, // tile size is 256 and x/y orientation is normal
                "http://tile.openstreetmap.org",
                "x","y","z") 
        	{
	            public String getTileUrl(int x, int y, int zoom) {
	                zoom = max-zoom;
	                String url = this.baseURL +"/"+zoom+"/"+x+"/"+y+".png";
	                return url;
            }
        };
        
        TileFactory tf = new DefaultTileFactory(info);
        
        map.setTileFactory(tf);
        map.setZoom(5);
        map.setAddressLocationShown(false);
        //map.setAddressLocation(new GeoPosition(44.33,23.81));
        map.setAddressLocation(new GeoPosition(userLocation.getLatitude(),userLocation.getLongitude()));
        
        return map;
    }
    
    
 
}
