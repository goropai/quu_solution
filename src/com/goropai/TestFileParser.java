package com.goropai;

import com.goropai.entities.LineType;
import com.goropai.entities.Query;
import com.goropai.entities.ResponseType;
import com.goropai.entities.Wait;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestFileParser {
    private static final int MAX_LINES = 100;
    private static final String ANY_ID = "*";
    private static final String DEFAULT_IN_FILE_PATH = "C:\\input.txt";
    private static final String DEFAULT_OUT_FILE_PATH = "C:\\output.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private String inputFilePath;
    private String outputFilePath;
    private List<Wait> waits = new ArrayList<>(MAX_LINES);
    private List<Query> queries = new ArrayList<>(MAX_LINES);

    public TestFileParser() {
        this.inputFilePath = DEFAULT_IN_FILE_PATH;
        this.outputFilePath = DEFAULT_OUT_FILE_PATH;
    }

    public TestFileParser(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public static void main(String[] args) {
        TestFileParser parser = new TestFileParser();
        parser.getInput();
        parser.getOutput();
    }

    private void getInput() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFilePath), StandardCharsets.UTF_8))) {
            int count = Integer.parseInt(reader.readLine());
            if (count > MAX_LINES) {
                throw new RuntimeException("Maximum lines count was exceeded (>100).");
            }
            for (int i = 0; i < count; i++) {
                String line = reader.readLine().trim();
                if (line.startsWith(LineType.C.toString())) {
                    waits.add(parseWait(line));
                } else if (line.startsWith(LineType.D.toString())) {
                    queries.add(parseQuery(line));
                } else {
                    throw new RuntimeException("Wrong line type.");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void getOutput() {
        String lineSeparator = System.lineSeparator();
        try (FileWriter out = new FileWriter(outputFilePath)){
            for (Query query : queries) {
                Long resultAverageWaitingTimeInMinutes = 0L;
                int count = 0;
                for (Wait wait : waits) {
                    if (query.getService_id().equals(ANY_ID) || wait.getService_id().startsWith(query.getService_id())
                            && query.getQuestion_type_id().equals(ANY_ID) || wait.getQuestion_type_id().startsWith(query.getQuestion_type_id())
                            && wait.getResponseType()==query.getResponseType()) {
                        if (query.getDateFrom().before(wait.getDate()) && (query.getDateTo() == null || query.getDateTo().after(wait.getDate()))) {
                            resultAverageWaitingTimeInMinutes = (resultAverageWaitingTimeInMinutes + wait.getWaitingTimeInMinutes())/++count;
                        }
                    }
                }
                out.write(resultAverageWaitingTimeInMinutes==0L?"-" + lineSeparator:resultAverageWaitingTimeInMinutes + lineSeparator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Wait parseWait(String line) throws ParseException {
        Wait result = new Wait();
        String[] parts = line.split(" ");
        //1
        result.setService_id(parts[1]);
        //2
        result.setQuestion_type_id(parts[2]);
        //3
        result.setResponseType(parts[3].equals(ResponseType.P.toString())?ResponseType.P:ResponseType.N);
        //4
        result.setDate(DATE_FORMAT.parse(parts[4]));
        //5
        result.setWaitingTimeInMinutes(Long.valueOf(parts[5]));
        return result;
    }

    private Query parseQuery(String line) throws ParseException {
        Query result = new Query();
        String[] parts = line.split(" ");
        //1
        result.setService_id(parts[1]);
        //2
        result.setQuestion_type_id(parts[2]);
        //3
        result.setResponseType(parts[3].equals(ResponseType.P.toString())?ResponseType.P:ResponseType.N);
        //4
        if (parts[4].contains("-")) {
            String[] partsOfDate = parts[4].split("-");
            result.setDateFrom(DATE_FORMAT.parse(partsOfDate[0]));
            result.setDateTo(DATE_FORMAT.parse(partsOfDate[1]));
        } else {
            result.setDateFrom(DATE_FORMAT.parse(parts[4]));
        }
        return result;
    }
}
