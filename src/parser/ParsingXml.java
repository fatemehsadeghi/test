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
import java.lang.reflect.Field;
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

        RandomAccessFile inputXml = new RandomAccessFile("Deposit.xml" , "r");
        inputXml.length();
        for(int f =0 ; f<inputXml.length();f++)
        {

        }

        File inputXml1 = new File("Deposit.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputXml1);
        return document;
    }
    public TreeMap<BigDecimal, String> parseDocument(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Element docElement = document.getDocumentElement();
        nodeList = docElement.getElementsByTagName("deposit");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                int j = i+1;
                customerNumber = docElement.getElementsByTagName("customerNumber").item(i).getTextContent();
                String depositTypeStr = docElement.getElementsByTagName("depositType").item(i).getTextContent();
                String depositTypeStrConcat = "depositType."+depositTypeStr;
                try {
                    Class depositTypeClass = Class.forName(depositTypeStrConcat);
                    Object f = depositTypeClass.newInstance();
                    DepositType depositType = (depositType.DepositType)depositTypeClass.newInstance();
                    tempInterestRate = depositType.getInterestRate();
                    deposit.setTempInterestRate(tempInterestRate);
                } catch (ClassNotFoundException e) {
                    try {
                        throw new DepositTypeNotFound("depositType Not found at " +j+ "th deposit");
                    } catch (DepositTypeNotFound depositTypeNotFound) {
                        depositTypeNotFound.printStackTrace();
                    }
                }
                BigDecimal depositBalance = new BigDecimal(docElement.getElementsByTagName("depositBalance").item(i).getTextContent());
                if (depositBalance.signum() < 0)
                    try {
                        throw new WrongDepositBalance("depositBalance can`t be negative at "+j+"th deposit");
                    } catch (WrongDepositBalance wrongDepositBalance) {
                        wrongDepositBalance.printStackTrace();
                    }
                int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(i).getTextContent());
                if (durationInDays <= 0)
                    try {
                        throw new WrongDurationInDayValue("durationInDays can`t be zero or negative at 	"+j+"th deposit");
                    } catch (WrongDurationInDayValue wrongDurationInDayValue) {
                        wrongDurationInDayValue.printStackTrace();
                    }
                Deposit deposit = new Deposit(customerNumber,tempInterestRate,depositBalance , durationInDays);
                tempPayedInterest= deposit.calculatePayedInterest(deposit);
                customerNumberMap.put (tempPayedInterest,customerNumber);
                depositMap.put( tempPayedInterest,deposit);
            }
            depositMap.descendingMap();
        }return customerNumberMap;
    }

    public void writeOnFile(TreeMap<BigDecimal ,String> customerNumberMap) throws IOException {
        RandomAccessFile outputFile = new RandomAccessFile("Output.xml" , "rw");
        for (Map.Entry<BigDecimal ,String > entry : customerNumberMap.descendingMap().entrySet()) {
            System.out.println( entry.getValue()+ "# " + entry.getKey() + "\n" );
            outputFile.writeUTF(String.valueOf(entry.getValue()) +"# " +String.valueOf( entry.getKey()) + "\n" );
        }
        outputFile.seek( outputFile.getFilePointer());
        outputFile.close();
    }
}
