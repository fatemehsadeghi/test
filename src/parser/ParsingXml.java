package parser;

import depositType.DepositType;
import deposits.Deposit;
import exception.DepositTypeNotFound;
import exception.WrongDepositBalance;
import exception.WrongDurationInDayValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class ParsingXml{
    public ParsingXml() {
    }
    private int durationInDays;
    private int tempInterestRate;
    NodeList nodeList;
    TreeMap<BigDecimal, Deposit> depositMap= new TreeMap();
    TreeMap<BigDecimal, String> customerNumberMap= new TreeMap();
    Deposit deposit = new Deposit();
    BigDecimal tempPayedInterest;
    String customerNumber;
    public Document parseXmlFile() throws ParserConfigurationException, IOException, SAXException {
        File inputXml = new File("Deposit.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputXml);
        //System.out.println("doc is " +document);
        return document;
    }
    public TreeMap<BigDecimal, String> parseDocument(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Element docElement = document.getDocumentElement();
        nodeList = docElement.getElementsByTagName("deposit");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                customerNumber = docElement.getElementsByTagName("customerNumber").item(i).getTextContent();
                String depositTypeStr = docElement.getElementsByTagName("depositType").item(i).getTextContent();
                String depositTypeStrConcat = "depositType."+depositTypeStr;
                try {
                    Class<?> c = Class.forName(depositTypeStrConcat);
                    DepositType depositType = (depositType.DepositType)c.newInstance();
                    tempInterestRate = depositType.getInterestRate();
                    deposit.setTempInterestRate(tempInterestRate);
                    // System.out.println("interest rate is" + depositType.getInterestRate() + deposit.getTempInterestRate());
                } catch (ClassNotFoundException e) {
                    try {
                        throw new DepositTypeNotFound("");
                    } catch (DepositTypeNotFound depositTypeNotFound) {
                        depositTypeNotFound.printStackTrace();
                    }
                }
                BigDecimal depositBalance = new BigDecimal(docElement.getElementsByTagName("depositBalance").item(i).getTextContent());
                if (depositBalance.signum() < 0)
                    try {
                        throw new WrongDepositBalance("depositBalance can`t be negative");
                    } catch (WrongDepositBalance wrongDepositBalance) {
                        wrongDepositBalance.printStackTrace();
                    }
                // deposit.setDepositBalance(depositBalance);
                int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(i).getTextContent());
                if (durationInDays <= 0)
                    try {
                        throw new WrongDurationInDayValue("durationInDays can`t be zero or negative");
                    } catch (WrongDurationInDayValue wrongDurationInDayValue) {
                        wrongDurationInDayValue.printStackTrace();
                    }
                Deposit deposit = new Deposit(customerNumber,tempInterestRate,depositBalance , durationInDays);
                tempPayedInterest= deposit.calculatePayedInterest(deposit);
                //System.out.println(tempPayedInterest);
                //Deposit deposit = new Deposit(customerNumber,rate,depositBalance , durationInDays);
                //depositList.add(deposit);
                // System.out.println("size is " + depositList.size());
                customerNumberMap.put (tempPayedInterest,customerNumber);
                depositMap.put( tempPayedInterest,deposit);
               // System.out.println("my key" + "/t "+depositMap.descendingMap());
            }
            customerNumberMap.descendingMap();
            depositMap.descendingMap();
            for (Map.Entry<BigDecimal ,String > entry : customerNumberMap.descendingMap().entrySet()) {
                System.out.println( entry.getValue()+ "# " + entry.getKey() + "\n" );
            }
        }return customerNumberMap;
    }

    public void writeOnFile(TreeMap<BigDecimal ,String> customerNumberMap) throws IOException {

        FileOutputStream outputFile = new FileOutputStream("Output.xml");
        ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
        for (Map.Entry<BigDecimal ,String > entry : customerNumberMap.descendingMap().entrySet()) {
            outputStream.writeUnshared(entry.getValue() + "# " + entry.getKey() + "\n");
        }

/*
        FileWriter fw = new FileWriter("Output.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        for (Map.Entry<BigDecimal ,String > entry : customerNumberMap.descendingMap().entrySet()) {
            bw.write(entry.getValue() + "# " + entry.getKey() + "\n");
        }
        System.out.println("File written Successfully");
*/
        outputStream.close();

    }
}
