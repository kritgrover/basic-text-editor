import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Searcher class to find strings
 * Sets input
 * Sets content to search from
 * Returns ArrayList of found positions
 * Confirms execution
*/
public class TextSearcher extends SwingWorker<List<MatchedGroup>,Void> {
    private String textToSearch = "";
    private String content;
    private final SearchCompleteHandler searchCompleteHandler;
    
    //Constructor to implement search handling
    public TextSearcher (SearchCompleteHandler searchCompleteHandler) {
        this.searchCompleteHandler = searchCompleteHandler;
    }
    
    
    //Setter for input to be searched for
    public void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }
    
    
    //Setter for defining content to search from
    public void setContent(String content) {
        this.content = content;
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
    
    
    //Method to confirm execution
    @Override
    protected void done() {
        try {
            searchCompleteHandler.handle(get());
        } catch (InterruptedException | ExecutionException e) {
            //do Nothing
        }
    }
}
