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

public class TextEditor extends JFrame {
    private JTextArea textArea;
    private JTextField searchField;
    private JFileChooser fileChooser;
    private JCheckBox checkBox;
    private int currentIndex;
    private java.util.List<MatchedGroup> matchedGroups = new ArrayList<>();


    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setTitle("Text Editor");
        initComponent();
        setVisible(true);
    }

    private void initComponent() {
        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        add(fileChooser);
        addTextArea();
        buildTop();
        buildMenuBar();
    }

    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");

        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        menu.setName("MenuFile");
        loadMenuItem.setName("MenuOpen");
        saveMenuItem.setName("MenuSave");
        exitMenuItem.setName("MenuExit");

        loadMenuItem.addActionListener(l -> loadContent());
        saveMenuItem.addActionListener(l -> saveFile(textArea.getText()));
        exitMenuItem.addActionListener(l -> System.exit(0));

        menu.add(loadMenuItem);
        menu.add(saveMenuItem);
        menu.addSeparator();
        menu.add(exitMenuItem);

        menuBar.add(menu);
        menuBar.add(createSearchMenu());
        setJMenuBar(menuBar);
    }

    private JMenu createSearchMenu() {
        JMenu menu = new JMenu("Search");
        menu.setName("MenuSearch");

        JMenuItem startMenuItem = new JMenuItem("Start search");
        JMenuItem prevMenuItem = new JMenuItem("Previous search");
        JMenuItem nextMenuItem = new JMenuItem("Next match");
        JMenuItem regExMenuItem = new JMenuItem("Use regular expressions");

        startMenuItem.setName("MenuStartSearch");
        nextMenuItem.setName("MenuNextMatch");
        prevMenuItem.setName("MenuPreviousMatch");
        regExMenuItem.setName("MenuUseRegExp");

        prevMenuItem.addActionListener(l -> onPrev());
        nextMenuItem.addActionListener(l -> onNext());
        startMenuItem.addActionListener(l -> onSearch());
        regExMenuItem.addActionListener(l -> toggleCheckBox());
        menu.add(startMenuItem);
        menu.add(prevMenuItem);
        menu.add(nextMenuItem);
        menu.add(regExMenuItem);

        return menu;
    }

    private void toggleCheckBox() {
        boolean currentChecked = !checkBox.isSelected();
        checkBox.setSelected(currentChecked);
    }

    private void addTextArea() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setEnabled(true);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        addBorder(scrollPane, 0, 8, 8, 8);
        scrollPane.setName("ScrollPane");
        setLocationRelativeTo(null);
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildTop() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ImageIcon saveButtonImage = new ImageIcon("images/diskette.png");
        ImageIcon loadImageIcon = new ImageIcon("images/open.png");

        JButton saveButton = new JButton(saveButtonImage);
        JLabel saveButtonLabel = new JLabel("Save");
        JButton loadButton = new JButton(loadImageIcon);
        JLabel loadButtonLabel = new JLabel("Load");

        loadButton.setName("OpenButton");
        saveButton.setName("SaveButton");

        loadButton.addActionListener(l -> loadContent());
        saveButton.addActionListener(l -> saveFile(textArea.getText()));


        panel.add(loadButton);
        panel.add(loadButtonLabel);
        panel.add(saveButton);
        panel.add(saveButtonLabel);


        JPanel container = new JPanel();
        container.add(panel);
        container.add(createSearchPanel());
        FlowLayout layout = (FlowLayout) container.getLayout();
        layout.setHgap(20);
        layout.setAlignment(FlowLayout.LEFT);
        add(container, BorderLayout.NORTH);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();

        ImageIcon nextIcon = new ImageIcon("images/next.jpg");
        ImageIcon prevIcon = new ImageIcon("images/previous.png");
        ImageIcon searchIcon = new ImageIcon("images/search.png");

        JButton searchButton = new JButton(searchIcon);
        JLabel searchButtonLabel = new JLabel("Search");
        JButton nextButton = new JButton(nextIcon);
        JLabel nextButtonLabel = new JLabel("Next");
        JButton prevButton = new JButton(prevIcon);
        JLabel prevButtonLabel = new JLabel("Prev");

        searchButton.setName("StartSearchButton");
        prevButton.setName("PreviousMatchButton");
        nextButton.setName("NextMatchButton");

        searchButton.addActionListener(l -> onSearch());

        nextButton.addActionListener(l -> onNext());
        prevButton.addActionListener(l -> onPrev());

        searchField = new JTextField(18);
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(18, 35));

        checkBox = new JCheckBox();
        checkBox.setName("UseRegExCheckbox");
        JLabel checkBoxLabel = new JLabel("Use regex");


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

    private void onSearch() {
        currentIndex = 0;
        matchedGroups = java.util.List.of();
        TextSearcher textSearcher = new TextSearcher(this::handleSearch);
        textSearcher.setTextToSearch(searchField.getText());
        textSearcher.setContent(textArea.getText());
        textSearcher.execute();
    }

    private void addBorder(JComponent component, int top, int right, int bottom, int left) {
        Border border = component.getBorder();
        Border empty = new EmptyBorder(top, left, bottom, right);
        if(border == null) {
            component.setBorder(empty);
            return;
        }
        component.setBorder(new CompoundBorder(empty, border));
    }

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

    private void handleSearch(java.util.List<MatchedGroup> matchedGroupList) {
        if(!matchedGroupList.isEmpty()) {
            this.matchedGroups = matchedGroupList;
            MatchedGroup matchedGroup = matchedGroupList.get(currentIndex);
            setCaretPosition(matchedGroup);
        } else {
            this.matchedGroups = java.util.List.of();
        }
    }

    private void onNext() {
        if(matchedGroups.isEmpty())
            return;
        currentIndex++;
        if(currentIndex == matchedGroups.size()) {
            currentIndex = 0;
        }
        setCaretPosition(matchedGroups.get(currentIndex));
    }

    private void onPrev() {
        if(matchedGroups.isEmpty())
            return;
        currentIndex--;
        if(currentIndex == -1) {
            currentIndex = matchedGroups.size() - 1;
        }
        setCaretPosition(matchedGroups.get(currentIndex));
    }

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

    private void setCaretPosition(MatchedGroup matchedGroup) {
        textArea.setCaretPosition(matchedGroup.getEndIndex());
        textArea.select(matchedGroup.getStartIndex(), matchedGroup.getEndIndex());
        textArea.grabFocus();
    }
}
