package controller;

import model.Building;
import model.Graph;
import model.LinkedList;
import model.Place;

import java.io.*;

public class FileController {
    public static LinkedList<Building> readBuildings() {
        LinkedList<Building> buildings = new LinkedList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/files/buildings.csv"));
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                Building building = new Building(splitLine[0], Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));
                if (splitLine.length == 4) {
                    String[] splitPlaces = splitLine[3].split(";");
                    LinkedList<Place> places = new LinkedList<Place>();

                    for (String splitPlace : splitPlaces) {
                        places.add(new Place(splitPlace));
                    }

                    building.setPlaces(places);
                }
                buildings.add(building);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(buildings.toString());
        return buildings;
    }

    public static void writeBuildings(LinkedList<Building> buildings) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/files/buildings.csv", false));
            writer.write("name,x,y,places");

            int index = 0;
            Building building = buildings.getNode(index);

            while (building != null) {
                writer.newLine();
                writer.write(building.getName() + "," + building.getPosition()[0] + "," + building.getPosition()[1] + "," + building.getPlaces().toString());
                index++;
                building = buildings.getNode(index);
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
