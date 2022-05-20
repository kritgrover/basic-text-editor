import java.util.List;

//Handler interface for TextSearcher implementation
public interface SearchCompleteHandler {
    void handle(List<MatchedGroup> result);
}
