package dev.blackhig.zhebushigudu.lover.manager;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.JsonParser;
import java.util.Collection;
import com.google.gson.Gson;
import java.nio.file.Path;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.file.OpenOption;
import com.google.gson.GsonBuilder;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Objects;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map;
import com.google.gson.JsonObject;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.features.setting.EnumConverter;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import com.google.gson.JsonElement;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import java.util.ArrayList;
import dev.blackhig.zhebushigudu.lover.util.Util;

public class ConfigManager implements Util {
    public ArrayList<Feature> features = new ArrayList<>();

    public String config = "Lover/config/";

    public static void setValueFromJson(Feature feature, Setting setting, JsonElement element) {
        String str;
        switch (setting.getType()) {
            case "Boolean":
                setting.setValue(Boolean.valueOf(element.getAsBoolean()));
                return;
            case "Double":
                setting.setValue(Double.valueOf(element.getAsDouble()));
                return;
            case "Float":
                setting.setValue(Float.valueOf(element.getAsFloat()));
                return;
            case "Integer":
                setting.setValue(Integer.valueOf(element.getAsInt()));
                return;
            case "String":
                str = element.getAsString();
                setting.setValue(str.replace("_", " "));
                return;
            case "Bind":
                setting.setValue((new Bind.BindConverter()).doBackward(element));
                return;
            case "Enum":
                try {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    Enum value = converter.doBackward(element);
                    setting.setValue((value == null) ? setting.getDefaultValue() : value);
                } catch (Exception exception) {
                }
                return;
        }
        lover.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
    }

    private static void loadFile(JsonObject input, Feature feature) {
        for (Map.Entry<String, JsonElement> entry : input.entrySet()) {
            String settingName = entry.getKey();
            JsonElement element = entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    lover.friendManager.addFriend(new FriendManager.Friend(element.getAsString(), UUID.fromString(settingName)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }
            boolean settingFound = false;
            for (Setting setting : feature.getSettings()) {
                if (settingName.equals(setting.getName())) {
                    try {
                        setValueFromJson(feature, setting, element);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    settingFound = true;
                }
            }
            if (settingFound) ;
        }
    }

    public void loadConfig(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("Lover").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        if (files.contains(new File("Lover/" + name + "/"))) {
            this.config = "Lover/" + name + "/";
        } else {
            this.config = "Lover/config/";
        }
        lover.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                loadSettings(feature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public boolean configExists(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("Lover").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        return files.contains(new File("Lover/" + name + "/"));
    }

    public void saveConfig(String name) {
        this.config = "Lover/" + name + "/";
        File path = new File(this.config);
        if (!path.exists())
            path.mkdir();
        lover.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                saveSettings(feature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public void saveCurrentConfig() {
        File currentConfig = new File("Lover/currentconfig.txt");
        try {
            if (currentConfig.exists()) {
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("Lover", ""));
                writer.close();
            } else {
                currentConfig.createNewFile();
                FileWriter writer = new FileWriter(currentConfig);
                String tempConfig = this.config.replaceAll("/", "");
                writer.write(tempConfig.replaceAll("Lover", ""));
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadCurrentConfig() {
        File currentConfig = new File("Lover/currentconfig.txt");
        String name = "config";
        try {
            if (currentConfig.exists()) {
                Scanner reader = new Scanner(currentConfig);
                while (reader.hasNextLine())
                    name = reader.nextLine();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public void resetConfig(boolean saveConfig, String name) {
        for (Feature feature : this.features)
            feature.reset();
        if (saveConfig)
            saveConfig(name);
    }

    public void saveSettings(Feature feature) throws IOException {
        JsonObject object = new JsonObject();
        File directory = new File(this.config + getDirectory(feature));
        if (!directory.exists())
            directory.mkdir();
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path outputFile = Paths.get(featureName);
        if (!Files.exists(outputFile))
            Files.createFile(outputFile);
        Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
        String json = gson.toJson(writeSettings(feature));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
        writer.write(json);
        writer.close();
    }

    public void init() {
        this.features.addAll(lover.moduleManager.modules);
        this.features.add(lover.friendManager);
        String name = loadCurrentConfig();
        loadConfig(name);
        lover.LOGGER.info("Config loaded.");
    }

    private void loadSettings(Feature feature) throws IOException {
        String featureName = this.config + getDirectory(feature) + feature.getName() + ".json";
        Path featurePath = Paths.get(featureName);
        if (!Files.exists(featurePath))
            return;
        loadPath(featurePath, feature);
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        InputStream stream = Files.newInputStream(path);
        try {
            loadFile((new JsonParser()).parse(new InputStreamReader(stream)).getAsJsonObject(), feature);
        } catch (IllegalStateException e) {
            lover.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            loadFile(new JsonObject(), feature);
        }
        stream.close();
    }

    public JsonObject writeSettings(Feature feature) {
        JsonObject object = new JsonObject();
        JsonParser jp = new JsonParser();
        for (Setting setting : feature.getSettings()) {
            if (setting.isEnumSetting()) {
                EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                object.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
                continue;
            }
            if (setting.isStringSetting()) {
                String str = (String) setting.getValue();
                setting.setValue(str.replace(" ", "_"));
            }
            try {
                object.add(setting.getName(), jp.parse(setting.getValueAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public String getDirectory(Feature feature) {
        String directory = "";
        if (feature instanceof Module)
            directory = directory + ((Module) feature).getCategory().getName() + "/";
        return directory;
    }
}