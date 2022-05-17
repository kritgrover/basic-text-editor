import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Searcher class to find strings
 * 
 *
 *
*/
public class TextSearcher extends SwingWorker<List<MatchedGroup>,Void> {
    private String textToSearch = "";
    private String content;
    private final SearchCompleteHandler searchCompleteHandler;

    public TextSearcher (SearchCompleteHandler searchCompleteHandler) {
        this.searchCompleteHandler = searchCompleteHandler;
    }

    
    //Method to return an ArrayList of found occurences of String
    @Override
    protected List<MatchedGroup> doInBackground() {
        Pattern pattern = Pattern.compile(textToSearch);
        Matcher matcher = pattern.matcher(content);
        List<MatchedGroup> positions = new ArrayList<>();
        while(matcher.find()) {
            positions.add(new MatchedGroup(matcher.start(), matcher.end()));
        }
        return positions;
    }
    
    //Method to confirm if String exists
    @Override
    protected void done() {
        try {
            searchCompleteHandler.handle(get());
        } catch (InterruptedException | ExecutionException e) {
            //do Nothing
        }
    }
    
    //Setter for input to be searched for
    public void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }
    
    //Setter for defining content to search from
    public void setContent(String content) {
        this.content = content;
    }
}
