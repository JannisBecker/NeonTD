package misc;

import java.io.InputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Ermöglicht es, Texte in der "Lato" Schriftart 
 * mit einer gewünschten Farbe und Größe zu rendern.
 * 
 */
public class LatoFont {
	static TrueTypeFont lato14, lato22, lato32;
	static Color color, colorCyan;
	static {
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("res/Lato.ttf");
	 
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			lato14 = new TrueTypeFont(awtFont2.deriveFont(14f),true);
			lato22 = new TrueTypeFont(awtFont2.deriveFont(22f),true);
			lato32 = new TrueTypeFont(awtFont2.deriveFont(32f), true);
			
			color = Color.decode("#2988ff");
	 
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Zeichnet einen String auf dem Bildschirm in der Farbe #2988ff.
	 * @param x X-Koordinate der linken, oberen Ecke des Textes.
	 * @param y X-Koordinate der linken, oberen Ecke des Textes.
	 * @param str Der zu rendernde Text.
	 * @param size Die Schriftgröße. Unterstützt werden 14, 22 und 32.
	 */
	public static void draw(int x, int y, String str, int size) {
		draw(x,y,str,size,color);
	}
	
	/**
	 * Zeichnet einen String auf dem Bildschirm.
	 * @param x X-Koordinate der linken, oberen Ecke des Textes.
	 * @param y X-Koordinate der linken, oberen Ecke des Textes.
	 * @param str Der zu rendernde Text.
	 * @param size Die Schriftgröße. Unterstützt werden 14, 22 und 32.
	 * @param color Die zu verwendende Farbe.
	 */
	public static void draw(int x, int y, String str, int size, Color color) {
		if(size == 14) {
			lato14.drawString(x, y, str, color);
		} else if(size == 22) {
			lato22.drawString(x, y, str, color);
		} else if(size == 32) {
			lato32.drawString(x, y, str, color);
		}
	}
}
