package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssetManager {
    private final List<Asset> assets;
    private final File dataFile;

    public AssetManager() {
        assets = new ArrayList<>();
        dataFile = new File("data/assets.csv");
        loadAssets();
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void addAsset(Asset asset) {
        assets.add(asset);
        saveAssets();
    }

    public void updateAsset(int index, Asset updatedAsset) {
        if (index >= 0 && index < assets.size()) {
            assets.set(index, updatedAsset);
            saveAssets();
        }
    }

    public void deleteAsset(int index) {
        if (index >= 0 && index < assets.size()) {
            assets.remove(index);
            saveAssets();
        }
    }

    public List<Asset> filterAssets(String searchText, String typeFilter, String statusFilter) {
        List<Asset> filteredAssets = new ArrayList<>();

        String search = searchText.toLowerCase().trim();

        for (Asset asset : assets) {
            boolean matchesSearch = asset.getName().toLowerCase().contains(search);
            boolean matchesType = typeFilter.equals("All") || asset.getType().equals(typeFilter);
            boolean matchesStatus = statusFilter.equals("All") || asset.getStatus().equals(statusFilter);

            if (matchesSearch && matchesType && matchesStatus) {
                filteredAssets.add(asset);
            }
        }

        return filteredAssets;
    }

    public void saveAssets() {
        try {
            File folder = dataFile.getParentFile();

            if (folder != null && !folder.exists()) {
                folder.mkdirs();
            }

            PrintWriter writer = new PrintWriter(new FileWriter(dataFile));

            writer.println("Name,Type,Roblox Asset ID,Studio Path,Status,Notes");

            for (Asset asset : assets) {
                writer.println(asset.toCsvLine());
            }

            writer.close();
        } catch (IOException exception) {
            System.out.println("Could not save assets: " + exception.getMessage());
        }
    }

    public void loadAssets() {
        assets.clear();

        if (!dataFile.exists()) {
            addSampleAssets();
            saveAssets();
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    assets.add(Asset.fromCsvLine(line));
                }
            }

            reader.close();
        } catch (IOException exception) {
            System.out.println("Could not load assets: " + exception.getMessage());
        }
    }

    public File exportAssets() throws IOException {
        File exportFolder = new File("exports");

        if (!exportFolder.exists()) {
            exportFolder.mkdirs();
        }

        String fileName = "roblox_assets_export_" + System.currentTimeMillis() + ".csv";
        File exportFile = new File(exportFolder, fileName);

        PrintWriter writer = new PrintWriter(new FileWriter(exportFile));

        writer.println("Name,Type,Roblox Asset ID,Studio Path,Status,Notes");

        for (Asset asset : assets) {
            writer.println(asset.toCsvLine());
        }

        writer.close();

        return exportFile;
    }

    public int getTotalCount() {
        return assets.size();
    }

    public int getStatusCount(String status) {
        int count = 0;

        for (Asset asset : assets) {
            if (asset.getStatus().equals(status)) {
                count++;
            }
        }

        return count;
    }

    private void addSampleAssets() {
        assets.add(new Asset(
                "Greatsword",
                "Weapon",
                "N/A",
                "ReplicatedStorage.Shared.ToolFolder.Primary.Greatsword",
                "In Progress",
                "First-person visibility needs testing."
        ));

        assets.add(new Asset(
                "Explosion Cloud",
                "VFX",
                "N/A",
                "ReplicatedStorage.Assets.VFX.ExplosionCloudMesh",
                "Finished",
                "Used for bomb explosion effect."
        ));

        assets.add(new Asset(
                "Lobby Map",
                "Map",
                "N/A",
                "ServerStorage.Maps.LobbyMap",
                "Needs Fixing",
                "Check spawn points and lighting."
        ));
    }
}
