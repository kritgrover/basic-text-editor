/* MatchedGroup Class
 * Methods to return Start and End Indexes for given String
*/
public class MatchedGroup {
    private final int startIndex;
    private final int endIndex;
    
    //Constructor to set indexes
    public MatchedGroup(int start, int end) {
        this.startIndex = start;
        this.endIndex = end;
    }
    
    //Getter for StartIndex
    public int getStartIndex() {
        return startIndex;
    }
    
    //Getter for EndIndex
    public int getEndIndex() {
        return endIndex;
    }
    
}
