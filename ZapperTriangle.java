
import javafx.scene.shape.Polygon;
import java.util.ArrayList; 
import java.util.List; 

/**
 * This class is a wrapper for the Polypon class. It creates an equilateral 
 * triangle to represent the zapper in Streamline game.
 */

public class ZapperTriangle extends Polygon implements Settable
{
   private double centerX; 
   private double centerY; 
   private double size; 
   private Direction dir = Direction.UP; 

   private static final double SHORT_VERT_MULT = 0.2887; 
   private static final double LONG_VERT_MULT = 0.5774; 
   private static final int EVEN_DIVISOR = 2; 
   private static final int TOTAL_DIRS = 4; 

   /** Constructor
    * @param dir the direction of the triangle that is pointing to
    * @param centerX the x coordinate of the triangle's center
    * @param centerY the y coordinate of the triangle's center
    * @param size the value of the equilateral triangle
    */   
   public ZapperTriangle(Direction dir, double centerX, double centerY, 
                           double size)
   {
      getPoints().addAll(centerX, 
                           centerY - size * LONG_VERT_MULT, 
                           centerX + size/EVEN_DIVISOR, 
                           centerY + size * SHORT_VERT_MULT, 
                           centerX - size/EVEN_DIVISOR, 
                           centerY + size * SHORT_VERT_MULT); 
      this.centerX = centerX; 
      this.centerY = centerY; 
      this.size = size; 
      setDirection(dir);
   }

   /**
    * Getter for the center x-coordinate of this Settable.
    * 
    * @return the center x-coordinate
    */
   public double getCenterX()
   {
      return centerX; 
   }

   /**
    * Getter for the center y-coordinate of this Settable.
    * 
    * @return the center y-coordinate
    */
   public double getCenterY()
   {
      return centerY; 
   }

   /**
    * Getter for the appropriate size metric of this Settable.
    * 
    * @return the size
    */
   public double getSize()
   {
      return size; 
   }

   /**
    * Getter for the direction that this zapper is facing. 
    * 
    * @return the direction of this zapper
    */
   public Direction getDirection()
   {
      return dir; 
   }

    
   /**
    * Setter for the center x-coordinate of this Settable.
    * 
    * @param centerX the new center x-coordinate
    */
   public void setCenterX(double centerX)
   {
      setNewCenterCoords(0, centerX-this.centerX); 
      this.centerX = centerX; 
   }

   /**
    * Setter for the center y-coordinate of this Settabl.
    * 
    * @param centerY the new center y-coordinate
    */
   public void setCenterY(double centerY)
   {
      setNewCenterCoords(1, centerY-this.centerY); 
      this.centerY = centerY; 
   }

   /**
    * Sets the points of the zapper based on the new center coordinates. 
    */
   private void setNewCenterCoords(int switcher, double offset)
   {
      List<Double> newCoords = new ArrayList<>(); 
      for(int i=0; i<getPoints().size(); i++)
      {
         newCoords.add(getPoints().get(i) + 
                        (i%EVEN_DIVISOR==switcher ? offset : 0)); 
      }
      getPoints().setAll(newCoords); 
   }

   /**
    * Setter for the appropriate size metric of this Settable.
    * 
    * @param size the new size
    */
   public void setSize(double size)
   {
      Direction origDir = dir; 
      setDirection(Direction.UP);

      List<Double> newCoords = new ArrayList<>(); 
      double size_offset = size-this.size; 
      double vert_long = LONG_VERT_MULT*size_offset; 
      double vert_short = SHORT_VERT_MULT*size_offset; 
      double spread = size_offset/EVEN_DIVISOR; 
      newCoords.add(getPoints().remove(0));
      newCoords.add(getPoints().remove(0)-vert_long); 
      newCoords.add(getPoints().remove(0)+spread); 
      newCoords.add(getPoints().remove(0)+vert_short); 
      newCoords.add(getPoints().remove(0)-spread); 
      newCoords.add(getPoints().remove(0)+vert_short); 
      getPoints().setAll(newCoords); 
      this.size = size; 

      setDirection(origDir); 
   } 

   /**
    * Setter for the direction of this zapper. 
    * 
    * @param dir the new direction 
    */
   private void setDirection(Direction dir)
   {
      int origCount = this.dir.getRotationCount(); 
      int numRots = ((dir.getRotationCount()-origCount)+TOTAL_DIRS)%TOTAL_DIRS;
      for(int i=0; i<numRots; i++)
      {
         rotateClockwise(); 
      }
   }

   /**
    * Rotates the zapper clockwise once. Assumes zapper is pointing in a 
    * cardinal direction. 
    */
   private void rotateClockwise()
   {
      int rot = dir.getRotationCount(); 
      dir = Direction.values()[(rot+1)%TOTAL_DIRS]; 
      List<Double> newCoords = new ArrayList<>(); 
      double vert_long = LONG_VERT_MULT*size; 
      double vert_short = SHORT_VERT_MULT*size; 
      double spread = size/EVEN_DIVISOR; 
      boolean parity = ((rot>>1)^(rot&1))==1; 
      boolean off_parity = rot>>1==0;
      double parity_vert_short = parity?-vert_short:vert_short; 
      double off_parity_vert_short = off_parity?-vert_short:vert_short;
      newCoords.add(getPoints().remove(0)+
                     (off_parity?vert_long:-vert_long));
      newCoords.add(getPoints().remove(0)+
                     (parity?vert_long:-vert_long)); 
      newCoords.add(getPoints().remove(0)+
                     (parity?-spread:spread)+off_parity_vert_short); 
      newCoords.add(getPoints().remove(0)+
                     (off_parity?spread:-spread)+parity_vert_short); 
      newCoords.add(getPoints().remove(0)+
                     (parity?spread:-spread)+off_parity_vert_short); 
      newCoords.add(getPoints().remove(0)+
                     (off_parity?-spread:spread)+parity_vert_short); 
      getPoints().setAll(newCoords); 
   }
}
