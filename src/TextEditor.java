import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/** Editor Class
 * Contains methods for building the Text Editor Window
 * Uses Swing framework for building the window and the buttons
 * Contains methods for functionality
 */
public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JTextField searchField;
    private JFileChooser fileChooser;
    private JCheckBox checkBox;
    private int currentIndex;
    private java.util.List<MatchedGroup> matchedGroups = new ArrayList<>();

    //Method for building the editor window
    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setTitle("Text Editor");
        initComponent();
        setVisible(true);
    }
    
    
    //Adding components
    private void initComponent() {
        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        add(fileChooser);
        addTextArea();
        buildTop();
        buildMenuBar();
    }
    
    
    //Method to build File Menu on Menu Bar
    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        
        //adding operations
        JMenuItem loadMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        menu.setName("MenuFile");
        loadMenuItem.setName("MenuOpen");
        saveMenuItem.setName("MenuSave");
        exitMenuItem.setName("MenuExit");
        
        //executing operations
        loadMenuItem.addActionListener(l -> loadContent());
        saveMenuItem.addActionListener(l -> saveFile(textArea.getText()));
        exitMenuItem.addActionListener(l -> System.exit(0));
        
        //displaying options
        menu.add(loadMenuItem);
        menu.add(saveMenuItem);
        menu.addSeparator();
        menu.add(exitMenuItem);
        
        menuBar.add(menu);
        menuBar.add(createSearchMenu());
        setJMenuBar(menuBar);
    }
    
    
    //Method to build Search Menu for Menu Bar
    private JMenu createSearchMenu() {
        JMenu menu = new JMenu("Search");
        menu.setName("MenuSearch");
        
        //adding options
        JMenuItem startMenuItem = new JMenuItem("Start search");
        JMenuItem prevMenuItem = new JMenuItem("Previous search");
        JMenuItem nextMenuItem = new JMenuItem("Next match");
        JMenuItem regExMenuItem = new JMenuItem("Use regular expressions");

        startMenuItem.setName("MenuStartSearch");
        nextMenuItem.setName("MenuNextMatch");
        prevMenuItem.setName("MenuPreviousMatch");
        regExMenuItem.setName("MenuUseRegExp");
        
        //executing operations
        prevMenuItem.addActionListener(l -> onPrev());
        nextMenuItem.addActionListener(l -> onNext());
        startMenuItem.addActionListener(l -> onSearch());
        regExMenuItem.addActionListener(l -> toggleCheckBox());
        
        //displaying options
        menu.add(startMenuItem);
        menu.add(prevMenuItem);
        menu.add(nextMenuItem);
        menu.add(regExMenuItem);

        return menu;
    }
    
    
    //Method for building buttons above text area
    private void buildTop() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        /* Removed Load and Save buttons temporarily.
         * Files can be loaded or saved from the File Menu on the Menu Bar
        ImageIcon saveButtonImage = new ImageIcon("images/save.png");
        ImageIcon loadImageIcon = new ImageIcon("images/load.png");

        saveButtonImage = changeSize(saveButtonImage);
        loadImageIcon = changeSize(loadImageIcon);
        
        //Initializing Load and Save Buttons
        JButton saveButton = new JButton(saveButtonImage);
        JLabel saveButtonLabel = new JLabel("Save");
        JButton loadButton = new JButton(loadImageIcon);
        JLabel loadButtonLabel = new JLabel("Open");

        loadButton.setName("OpenButton");
        saveButton.setName("SaveButton");
        
        //Adding functionality
        loadButton.addActionListener(l -> loadContent());
        saveButton.addActionListener(l -> saveFile(textArea.getText()));


        Displaying Buttons
        panel.add(loadButton);
        panel.add(loadButtonLabel);
        panel.add(saveButton);
        panel.add(saveButtonLabel);
        */


        JPanel container = new JPanel();
        //container.add(panel);
        container.add(createSearchPanel());
        FlowLayout layout = (FlowLayout) container.getLayout();
        layout.setHgap(20);
        layout.setAlignment(FlowLayout.LEFT);
        add(container, BorderLayout.NORTH);
    }

    
    //Method for building Text Area where the editing takes place
    private void addTextArea() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setEnabled(true);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addBorder(scrollPane);
        scrollPane.setName("ScrollPane");
        setLocationRelativeTo(null);
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /** Method for creating Search panel and regex options
    * - Creates a search bar for finding specific string in loaded file
    * - Next and Previous Buttons are used to find corresponding occurrences of the string
    */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();

        //Getting images as button icons
        ImageIcon nextIcon = new ImageIcon("images/next.png");
        ImageIcon prevIcon = new ImageIcon("images/previous.png");
        ImageIcon searchIcon = new ImageIcon("images/search.png");

        //Updating sizes
        nextIcon = changeSize(nextIcon);
        prevIcon = changeSize(prevIcon);
        searchIcon = changeSize(searchIcon);
        
        //Initializing buttons
        JButton searchButton = new JButton(searchIcon);
        JLabel searchButtonLabel = new JLabel("Search");
        JButton nextButton = new JButton(nextIcon);
        JLabel nextButtonLabel = new JLabel("Next");
        JButton prevButton = new JButton(prevIcon);
        JLabel prevButtonLabel = new JLabel("Prev");
        
        searchButton.setName("StartSearchButton");
        prevButton.setName("PreviousMatchButton");
        nextButton.setName("NextMatchButton");
        
        //Adding functionality
        searchButton.addActionListener(l -> onSearch());
        nextButton.addActionListener(l -> onNext());
        prevButton.addActionListener(l -> onPrev());
        
        //Making search bar
        searchField = new JTextField(18);
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(18, 35));
        
        //Making Use Regex checkbox
        checkBox = new JCheckBox();
        checkBox.setName("UseRegExCheckbox");
        JLabel checkBoxLabel = new JLabel("Use regex");

        //Displaying buttons
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(searchButtonLabel);
        panel.add(prevButton);
        panel.add(prevButtonLabel);
        panel.add(nextButton);
        panel.add(nextButtonLabel);
        panel.add(checkBox);
        panel.add(checkBoxLabel);

        return panel;
    }
    
    private void toggleCheckBox() {
        boolean currentChecked = !checkBox.isSelected();
        checkBox.setSelected(currentChecked);
    }
    
    //Adding borders for components
    private void addBorder(JComponent component) {
        Border border = component.getBorder();
        Border empty = new EmptyBorder(0, 8, 8, 8);
        if (border == null) {
            component.setBorder(empty);
            return;
        }
        component.setBorder(new CompoundBorder(empty, border));
    }

    private ImageIcon changeSize(ImageIcon icon) {
        Image image = icon.getImage();
        Image newImg = image.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        return new ImageIcon(newImg);
    }
    
    
    //Functionality for Search Button
    private void onSearch() {
        currentIndex = 0;
        matchedGroups = java.util.List.of();
        TextSearcher textSearcher = new TextSearcher(this::handleSearch);
        textSearcher.setTextToSearch(searchField.getText());                    //Specifying string to find
        textSearcher.setContent(textArea.getText());                            //Specifying data to look through
        textSearcher.execute();                                                 //Runs doInBackground() and done()
    }
    
    
    //Functionality for Load button
    private void loadContent() {
        int returnedValue = fileChooser.showOpenDialog(null);
        if(returnedValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = Files.readString(selectedFile.toPath());
                textArea.setText(content);
            } catch (IOException e) {
                textArea.setText("");
            }
        }
    }
    
    
    //Functionality for Next button
    private void onNext() {
        if(matchedGroups.isEmpty())
            return;
        currentIndex++;
        if(currentIndex == matchedGroups.size()) {
            currentIndex = 0;
        }
        setCaretPosition(matchedGroups.get(currentIndex));
    }
    
    
    //Functionality for Previous button
    private void onPrev() {
        if(matchedGroups.isEmpty())
            return;
        currentIndex--;
        if(currentIndex == -1) {
            currentIndex = matchedGroups.size() - 1;
        }
        setCaretPosition(matchedGroups.get(currentIndex));
    }
    
    
    //Functionality for Save button
    private void saveFile(String content) {
        fileChooser.setDialogTitle("Save File");
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();
        if(file == null)
            return;
        
        try(FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        } catch (IOException e) {
            //do nothing
        }
    }
    
    
    /** HandleSearch Method
     * Stores given MatchedGroup List to instance variable
     * Creates new MatchedCroup with current index
    */
    private void handleSearch(java.util.List<MatchedGroup> matchedGroupList) {
        if(!matchedGroupList.isEmpty()) {
            this.matchedGroups = matchedGroupList;
            MatchedGroup matchedGroup = matchedGroupList.get(currentIndex);
            setCaretPosition(matchedGroup);
        } else {
            this.matchedGroups = java.util.List.of();
        }
    }
    
    
    //Method for highlighting String found using Search Bar
    private void setCaretPosition(MatchedGroup matchedGroup) {
        textArea.setCaretPosition(matchedGroup.getEndIndex());
        textArea.select(matchedGroup.getStartIndex(), matchedGroup.getEndIndex());
        textArea.grabFocus();
    }
}
