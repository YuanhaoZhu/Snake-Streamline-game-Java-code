
public interface Settable
{
    
    /**
     * Getter for the center x-coordinate of this Settable.
     * 
     * @return the center x-coordinate
     */
    public double getCenterX();

    /**
     * Getter for the center y-coordinate of this Settable.
     * 
     * @return the center y-coordinate
     */
    public double getCenterY();

    /**
     * Getter for the appropriate size metric of this Settable.
     * 
     * @return the size
     */
    public double getSize();

    
    /**
     * Setter for the center x-coordinate of this Settable.
     * 
     * @param centerX the new center x-coordinate
     */
    public void setCenterX(double centerX);

    /**
     * Setter for the center y-coordinate of this Settabl.
     * 
     * @param centerY the new center y-coordinate
     */
    public void setCenterY(double centerY);

    /**
     * Setter for the appropriate size metric of this Settable.
     * 
     * @param size the new size
     */
    public void setSize(double size); 
}