import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.*;

import javax.lang.model.element.Name;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        //CSV to JSON (Задача 1)
        //String [] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        //String fileName = "data.csv";
        //List<Employee> list = parseCSV(columnMapping, fileName);

        //writeString(listToJson(list));

        //XML to JSON (Задача 2)
        //List<Employee> list2 = parseXML("data.xml");
        //writeString(listToJson(list2));

        System.out.println(readString("new_data.json"));
        List<Employee> list3 = jsonToList(readString("new_data.json"));

        for (Employee employee : list3) {
            System.out.println(employee.employeeToString());
        }
    }

    public static List<Employee> parseCSV (String [] columnMapping, String filename) {
        List<Employee> list = null;
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            ColumnPositionMappingStrategy <Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken <List<Employee>>() {}.getType();
                GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        System.out.println(json);
        return json;

    }

    public static void writeString(String json) {
        try (FileWriter file = new FileWriter("new_data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List <Employee> list = new ArrayList<>();
        String p = "";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node1 = nodeList.item(i); // Для раскрытия узлов корневого каталога
                if (Node.ELEMENT_NODE == node1.getNodeType()) {
                    NodeList nodeList1 = node1.getChildNodes();

                    for (int t = 0; t < nodeList1.getLength(); t++) {
                        Node node2 = nodeList1.item(t); // Для раскрытия узлов каждого из Employee
                        if (Node.ELEMENT_NODE == node2.getNodeType()) {
                            NodeList nodeList2 = node2.getChildNodes();
                            p = p + nodeList2.item(0).getTextContent() + " ";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String [] y = p.trim().split(" ");
        Employee employee1 = new Employee(Long.parseLong(y[0]), y[1], y[2], y[3], Integer.parseInt(y[4]));
        Employee employee2 = new Employee(Long.parseLong(y[5]), y[6], y[7], y[8], Integer.parseInt(y[9]));

        list.add(employee1);
        list.add(employee2);

        return list;
    }

    public static String readString(String filename) {
        String jsonToString = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            jsonToString = reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonToString;
    }

    public static List<Employee> jsonToList (String json) {
        List <Employee> list = null;
        try {
            Employee [] employees = new Gson().fromJson(json, Employee[].class);
            list = Arrays.stream(employees).toList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}


