package main;

public class Asset {
    private String name;
    private String type;
    private String robloxAssetId;
    private String studioPath;
    private String status;
    private String notes;

    public Asset(String name, String type, String robloxAssetId, String studioPath, String status, String notes) {
        this.name = name;
        this.type = type;
        this.robloxAssetId = robloxAssetId;
        this.studioPath = studioPath;
        this.status = status;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRobloxAssetId() {
        return robloxAssetId;
    }

    public String getStudioPath() {
        return studioPath;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRobloxAssetId(String robloxAssetId) {
        this.robloxAssetId = robloxAssetId;
    }

    public void setStudioPath(String studioPath) {
        this.studioPath = studioPath;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toCsvLine() {
        return escapeCsv(name) + "," +
                escapeCsv(type) + "," +
                escapeCsv(robloxAssetId) + "," +
                escapeCsv(studioPath) + "," +
                escapeCsv(status) + "," +
                escapeCsv(notes);
    }

    public static Asset fromCsvLine(String line) {
        String[] parts = parseCsvLine(line);

        String name = getPart(parts, 0);
        String type = getPart(parts, 1);
        String robloxAssetId = getPart(parts, 2);
        String studioPath = getPart(parts, 3);
        String status = getPart(parts, 4);
        String notes = getPart(parts, 5);

        return new Asset(name, type, robloxAssetId, studioPath, status, notes);
    }

    private static String getPart(String[] parts, int index) {
        if (index >= parts.length) {
            return "";
        }
        return parts[index];
    }

    private static String escapeCsv(String value) {
        if (value == null) {
            value = "";
        }

        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n");

        value = value.replace("\"", "\"\"");

        if (needsQuotes) {
            value = "\"" + value + "\"";
        }

        return value;
    }

    private static String[] parseCsvLine(String line) {
        java.util.ArrayList<String> values = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);

            if (character == '"') {
                if (insideQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (character == ',' && !insideQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }

        values.add(current.toString());

        return values.toArray(new String[0]);
    }
}
