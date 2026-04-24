package main;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class AssetTableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "Name",
            "Type",
            "Roblox Asset ID",
            "Studio Path",
            "Status",
            "Notes"
    };

    private List<Asset> assets;

    public AssetTableModel() {
        assets = new ArrayList<>();
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
        fireTableDataChanged();
    }

    public Asset getAssetAt(int row) {
        if (row >= 0 && row < assets.size()) {
            return assets.get(row);
        }

        return null;
    }

    @Override
    public int getRowCount() {
        return assets.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Asset asset = assets.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return asset.getName();
            case 1:
                return asset.getType();
            case 2:
                return asset.getRobloxAssetId();
            case 3:
                return asset.getStudioPath();
            case 4:
                return asset.getStatus();
            case 5:
                return asset.getNotes();
            default:
                return "";
        }
    }
}
