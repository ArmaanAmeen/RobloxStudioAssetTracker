package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private final AssetManager assetManager;
    private final AssetTableModel tableModel;

    private JTable assetTable;

    private JTextField nameField;
    private JComboBox<String> typeComboBox;
    private JTextField robloxAssetIdField;
    private JTextField studioPathField;
    private JComboBox<String> statusComboBox;
    private JTextArea notesArea;

    private JTextField searchField;
    private JComboBox<String> typeFilterComboBox;
    private JComboBox<String> statusFilterComboBox;

    private JLabel dashboardLabel;

    private final String[] assetTypes = {
            "Weapon",
            "Skin",
            "Wrap",
            "Animation",
            "Sound",
            "Map",
            "VFX",
            "UI",
            "Script",
            "Other"
    };

    private final String[] statuses = {
            "Finished",
            "In Progress",
            "Needs Fixing",
            "Not Started"
    };

    public MainFrame() {
        assetManager = new AssetManager();
        tableModel = new AssetTableModel();

        setTitle("Roblox Studio Project Asset Tracker");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createLayout();
        refreshTable();
        updateDashboard();
    }

    private void createLayout() {
        setLayout(new BorderLayout());

        JPanel titlePanel = createTitlePanel();
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();
        JPanel filterPanel = createFilterPanel();

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titleLabel = new JLabel("Roblox Studio Project Asset Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        dashboardLabel = new JLabel();
        dashboardLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(dashboardLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Asset Information"));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        nameField = new JTextField();
        typeComboBox = new JComboBox<>(assetTypes);
        robloxAssetIdField = new JTextField();
        studioPathField = new JTextField();
        statusComboBox = new JComboBox<>(statuses);
        notesArea = new JTextArea(6, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        addFormRow(panel, gbc, 0, "Asset Name:", nameField);
        addFormRow(panel, gbc, 1, "Asset Type:", typeComboBox);
        addFormRow(panel, gbc, 2, "Roblox Asset ID:", robloxAssetIdField);
        addFormRow(panel, gbc, 3, "Studio Path:", studioPathField);
        addFormRow(panel, gbc, 4, "Status:", statusComboBox);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        panel.add(new JLabel("Notes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;
        panel.add(new JScrollPane(notesArea), gbc);

        JButton addButton = new JButton("Add Asset");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear Form");
        JButton exportButton = new JButton("Export CSV");

        addButton.addActionListener(event -> addAsset());
        editButton.addActionListener(event -> editSelectedAsset());
        deleteButton.addActionListener(event -> deleteSelectedAsset());
        clearButton.addActionListener(event -> clearForm());
        exportButton.addActionListener(event -> exportAssets());

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 6, 6));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(buttonPanel, gbc);

        gbc.gridy = 7;
        gbc.weighty = 1;
        panel.add(new JLabel(""), gbc);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        panel.add(input, gbc);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Search and Filters"));

        searchField = new JTextField(20);

        typeFilterComboBox = new JComboBox<>(createFilterOptions(assetTypes));
        statusFilterComboBox = new JComboBox<>(createFilterOptions(statuses));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent event) {
                refreshTable();
            }

            public void removeUpdate(DocumentEvent event) {
                refreshTable();
            }

            public void changedUpdate(DocumentEvent event) {
                refreshTable();
            }
        });

        typeFilterComboBox.addActionListener(event -> refreshTable());
        statusFilterComboBox.addActionListener(event -> refreshTable());

        panel.add(new JLabel("Search Name:"));
        panel.add(searchField);

        panel.add(new JLabel("Type:"));
        panel.add(typeFilterComboBox);

        panel.add(new JLabel("Status:"));
        panel.add(statusFilterComboBox);

        return panel;
    }

    private String[] createFilterOptions(String[] values) {
        String[] options = new String[values.length + 1];
        options[0] = "All";

        for (int i = 0; i < values.length; i++) {
            options[i + 1] = values[i];
        }

        return options;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        assetTable = new JTable(tableModel);
        assetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assetTable.setAutoCreateRowSorter(true);
        assetTable.setRowHeight(24);

        assetTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                loadSelectedAssetIntoForm();
            }
        });

        JScrollPane scrollPane = new JScrollPane(assetTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addAsset() {
        Asset asset = getAssetFromForm();

        if (asset == null) {
            return;
        }

        assetManager.addAsset(asset);
        refreshTable();
        updateDashboard();
        clearForm();

        JOptionPane.showMessageDialog(this, "Asset added successfully.");
    }

    private void editSelectedAsset() {
        int selectedRow = assetTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an asset to edit.");
            return;
        }

        Asset updatedAsset = getAssetFromForm();

        if (updatedAsset == null) {
            return;
        }

        int modelRow = assetTable.convertRowIndexToModel(selectedRow);
        Asset selectedAsset = tableModel.getAssetAt(modelRow);
        int realIndex = assetManager.getAssets().indexOf(selectedAsset);

        assetManager.updateAsset(realIndex, updatedAsset);

        refreshTable();
        updateDashboard();
        clearForm();

        JOptionPane.showMessageDialog(this, "Asset updated successfully.");
    }

    private void deleteSelectedAsset() {
        int selectedRow = assetTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an asset to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this asset?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int modelRow = assetTable.convertRowIndexToModel(selectedRow);
        Asset selectedAsset = tableModel.getAssetAt(modelRow);
        int realIndex = assetManager.getAssets().indexOf(selectedAsset);

        assetManager.deleteAsset(realIndex);

        refreshTable();
        updateDashboard();
        clearForm();

        JOptionPane.showMessageDialog(this, "Asset deleted successfully.");
    }

    private Asset getAssetFromForm() {
        String name = nameField.getText().trim();
        String type = (String) typeComboBox.getSelectedItem();
        String robloxAssetId = robloxAssetIdField.getText().trim();
        String studioPath = studioPathField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        String notes = notesArea.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Asset name is required.");
            return null;
        }

        if (robloxAssetId.isEmpty()) {
            robloxAssetId = "N/A";
        }

        return new Asset(name, type, robloxAssetId, studioPath, status, notes);
    }

    private void loadSelectedAssetIntoForm() {
        int selectedRow = assetTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow = assetTable.convertRowIndexToModel(selectedRow);
        Asset asset = tableModel.getAssetAt(modelRow);

        if (asset == null) {
            return;
        }

        nameField.setText(asset.getName());
        typeComboBox.setSelectedItem(asset.getType());
        robloxAssetIdField.setText(asset.getRobloxAssetId());
        studioPathField.setText(asset.getStudioPath());
        statusComboBox.setSelectedItem(asset.getStatus());
        notesArea.setText(asset.getNotes());
    }

    private void clearForm() {
        nameField.setText("");
        typeComboBox.setSelectedIndex(0);
        robloxAssetIdField.setText("");
        studioPathField.setText("");
        statusComboBox.setSelectedIndex(0);
        notesArea.setText("");
        assetTable.clearSelection();
    }

    private void refreshTable() {
        String searchText = searchField == null ? "" : searchField.getText();
        String typeFilter = typeFilterComboBox == null ? "All" : (String) typeFilterComboBox.getSelectedItem();
        String statusFilter = statusFilterComboBox == null ? "All" : (String) statusFilterComboBox.getSelectedItem();

        List<Asset> filteredAssets = assetManager.filterAssets(searchText, typeFilter, statusFilter);
        tableModel.setAssets(filteredAssets);
    }

    private void updateDashboard() {
        int total = assetManager.getTotalCount();
        int finished = assetManager.getStatusCount("Finished");
        int inProgress = assetManager.getStatusCount("In Progress");
        int needsFixing = assetManager.getStatusCount("Needs Fixing");
        int notStarted = assetManager.getStatusCount("Not Started");

        dashboardLabel.setText(
                "Total: " + total +
                        " | Finished: " + finished +
                        " | In Progress: " + inProgress +
                        " | Needs Fixing: " + needsFixing +
                        " | Not Started: " + notStarted
        );
    }

    private void exportAssets() {
        try {
            File exportFile = assetManager.exportAssets();
            JOptionPane.showMessageDialog(this, "Exported to: " + exportFile.getPath());
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(this, "Could not export assets: " + exception.getMessage());
        }
    }
}
