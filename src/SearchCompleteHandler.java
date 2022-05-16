import java.util.List;

public interface SearchCompleteHandler {
    void handle(List<MatchedGroup> result);
}
