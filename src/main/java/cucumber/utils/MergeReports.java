package cucumber.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;

public class MergeReports {
    private final static String pathToCucumberReport = System.getenv("CUCUMBER_REPORT");
    private final static String pathToCucumberRerunReport = System.getenv("CUCUMBER_RERUN_REPORT");

    public static void main (String[] args) {

        if (!rerunFeatures().isEmpty()) {
            runMerge();
        } else {
            System.out.println("There are no failed tests to be re-run!");
        }
    }

    public static void runMerge() {
        JSONArray original;
        JSONObject feature;
        JSONArray scenarios;

        File file1 = new File(pathToCucumberReport);
        String path1 = file1.getAbsolutePath();

        original = originalFeatures();

        for (Object o : original) {
            feature = (JSONObject) o;
            scenarios = (JSONArray) feature.get("elements");

            Iterator<Object> iterator = scenarios.iterator();
            while (iterator.hasNext()) {
                Object it = iterator.next();
                JSONObject originalScenario = (JSONObject) it;

                stepsToUpdate(originalScenario);
            }
        }

        System.out.println("MERGED_CUCUMBER_REPORT: " + original);

        try {
            FileWriter file = new FileWriter(path1);
            file.write(original.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray rerunFeatures() {
        JSONParser parser = new JSONParser();
        JSONArray rerun = new JSONArray();

        File file2 = new File(pathToCucumberRerunReport);
        String path2 = file2.getAbsolutePath();

        try {
            rerun = (JSONArray) parser.parse(new FileReader(path2));

        } catch (FileNotFoundException e) {
            System.out.println("Please configure CUCUMBER_RERUN_REPORT as part of your env variables!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rerun;
    }

    private static JSONArray originalFeatures() {
        JSONParser parser = new JSONParser();
        JSONArray original = new JSONArray();

        File file1 = new File(pathToCucumberReport);
        String path1 = file1.getAbsolutePath();

        try {
            original = (JSONArray) parser.parse(new FileReader(path1));

        } catch (FileNotFoundException e) {
            System.out.println("Please configure CUCUMBER_REPORT as part of your env variables!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return original;
    }

    private static JSONObject stepsToUpdate(JSONObject originalScenario) {
        JSONArray scenarios;
        JSONArray rerun;

        rerun = rerunFeatures();

        for (Object o : rerun) {
            JSONObject feature = (JSONObject) o;
            scenarios = (JSONArray) feature.get("elements");

            Iterator<Object> iterator = scenarios.iterator();
            while (iterator.hasNext()) {
                Object it = iterator.next();
                JSONObject rerunScenario = (JSONObject) it;

                if(rerunScenario.get("name").equals(originalScenario.get("name"))) {
                    originalScenario.put("steps", rerunScenario.get("steps"));
                    return originalScenario;
                }
            }
        }
        return originalScenario;
    }
}
