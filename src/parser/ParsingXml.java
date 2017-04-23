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
    NodeList nodeList;
    TreeMap<BigDecimal, Deposit> depositMap= new TreeMap();
    TreeMap<BigDecimal, String> customerNumberMap= new TreeMap();
    Deposit deposit = new Deposit();
    BigDecimal tempPayedInterest;
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
                deposit.setCustomerNumber(docElement.getElementsByTagName("customerNumber").item(i).getTextContent());
                String depositTypeStr = "depositType."+ docElement.getElementsByTagName("depositType").item(i).getTextContent();
                try {
                    Class depositTypeClass = Class.forName(depositTypeStr);
                   // Object f = depositTypeClass.newInstance();
                    DepositType depositType = (depositType.DepositType)depositTypeClass.newInstance();
                    //tempInterestRate = depositType.getInterestRate();
                    //deposit.setTempInterestRate(tempInterestRate);
                    deposit.setDepositType(depositType);
                    /*
                    Class<?> clazz = Class.forName(depositTypeStr);
                    Method method = clazz.getDeclaredMethod("getInterestRate");
                    Object obj = clazz.newInstance();
                   int a = (int) method.invoke(obj);
                    deposit.setDepositType(Object obj);
                    System.out.println("1111111111111111111" +a);
*/
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
                deposit.setDepositBalance(depositBalance);
                int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(i).getTextContent());
                if (durationInDays <= 0)
                    try {
                        throw new WrongDurationInDayValue("durationInDays can`t be zero or negative at 	"+j+"th deposit");
                    } catch (WrongDurationInDayValue wrongDurationInDayValue) {
                        wrongDurationInDayValue.printStackTrace();
                    }
                deposit.setDurationInDays(durationInDays);
                tempPayedInterest= deposit.calculatePayedInterest(deposit);
                customerNumberMap.put (tempPayedInterest,deposit.getCustomerNumber());
                depositMap.put( tempPayedInterest,deposit);
            }
            depositMap.descendingMap();
        }return customerNumberMap;
    }

    public void writeOnFile(TreeMap<BigDecimal ,String> customerNumberMap) throws IOException {
        RandomAccessFile outputFile = new RandomAccessFile("Output.txt" , "rw");
        for (Map.Entry<BigDecimal ,String > entry : customerNumberMap.descendingMap().entrySet()) {
            System.out.println( entry.getValue()+ "# " + entry.getKey() + "\n" );
            outputFile.writeUTF(String.valueOf(entry.getValue()) +"# " +String.valueOf( entry.getKey()) + "\n" );
        }
        outputFile.seek( outputFile.getFilePointer());
        outputFile.close();
    }
    public static  void main (String[] aegs) throws IOException, SAXException, ParserConfigurationException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        ParsingXml p = new ParsingXml();
        p.parseDocument(p.parseXmlFile());
    }
}
