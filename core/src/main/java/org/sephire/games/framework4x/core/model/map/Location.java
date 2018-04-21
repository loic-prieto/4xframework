package org.sephire.games.framework4x.core.model.map;

import lombok.Value;

@Value
public class Location {
    private int x;
    private int y;

    /**
     * Returns a new location which is the result of substracting the given
     * location coordinates to the current location coordinates.
     * Does not modify the current location.
     *
     * @param location
     * @return
     */
    public Location substract(Location location) {
        return new Location(this.x - location.x,this.y - location.y);
    }
    public Location substract(int x,int y) {
        return this.substract(new Location(x,y));
    }

    /**
     * Returns a new location which is the result of adding the given
     * location coordinates to the current location coordinates.
     * Does not modify the current location.
     *
     * @param location
     * @return
     */
    public Location add(Location location) {
        return new Location(this.x + location.x,this.y + location.y);
    }
    public Location add(int x,int y) { return this.add(new Location(x,y));}

    /**
     * Checks whether one of the two coordinates has a positive value.
     * @return
     */
    public boolean hasPositiveValue() {
        return (x >= 0 || y >= 0);
    }
}
