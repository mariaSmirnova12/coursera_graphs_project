package util;

import geography.GeographicPoint;

// A class to store information about the lines in the road files.
public class RoadLineInfo {
    GeographicPoint point1;
    GeographicPoint point2;

    String roadName;
    String roadType;

    /** Create a new RoadLineInfo object to store information about the line
     * read from the file
     * @param p1 One of the points
     * @param p2 The other point
     * @param roadName The name of the road
     * @param roadType The type of the road
     */
    RoadLineInfo(GeographicPoint p1, GeographicPoint p2, String roadName, String roadType)
    {
        point1 = p1;
        point2 = p2;
        this.roadName = roadName;
        this.roadType = roadType;
    }


    /** Get the other point from this roadLineInfo */
    public GeographicPoint getOtherPoint(GeographicPoint pt)
    {
        if (pt == null) throw new IllegalArgumentException();
        if (pt.equals(point1)) {
            return point2;
        }
        else if (pt.equals(point2)) {
            return point1;
        }
        else throw new IllegalArgumentException();
    }

    /** Two RoadLineInfo objects are considered equal if they have the same
     * two points and the same roadName and roadType.
     */
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof RoadLineInfo))
        {
            return false;
        }
        RoadLineInfo info = (RoadLineInfo)o;
        return info.point1.equals(this.point1) && info.point2.equals(this.point2)  &&
                info.roadType.equals(this.roadType) && info.roadName.equals(this.roadName);

    }

    /** Calculate the hashCode based on the hashCodes of the two points
     * @return The hashcode for this object.
     */
    public int hashCode()
    {
        return point1.hashCode() + point2.hashCode();

    }

    /** Returns whether these segments are part of the same road in terms of
     * road name and road type.
     * @param info The RoadLineInfo to compare against.
     * @return true if these represent the same road, false otherwise.
     */
    public boolean sameRoad(RoadLineInfo info)
    {
        return info.roadName.equals(this.roadName) && info.roadType.equals(this.roadType);
    }

    /** Return a copy of this LineInfo in the other direction */
    public RoadLineInfo getReverseCopy()
    {
        return new RoadLineInfo(this.point2, this.point1, this.roadName, this.roadType);
    }

    /** Return true if this road is the same segment as other, but in reverse
     *   Otherwise return false.
     */
    public boolean isReverse(RoadLineInfo other)
    {
        return this.point1.equals(other.point2) && this.point2.equals(other.point1) &&
                this.roadName.equals(other.roadName) && this.roadType.equals(other.roadType);
    }

    /** Return the string representation of this LineInfo. */
    public String toString()
    {
        return this.point1 + " " + this.point2 + " " + this.roadName + " " + this.roadType;

    }
}
