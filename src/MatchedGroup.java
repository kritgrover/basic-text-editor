public class MatchedGroup {
    private final int startIndex;
    private final int endIndex;

    public MatchedGroup(int start, int end) {
        this.startIndex = start;
        this.endIndex = end;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }
}
