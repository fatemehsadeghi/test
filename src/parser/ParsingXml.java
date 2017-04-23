package parser;

import depositType.DepositType;
import deposits.Deposit;
import exception.DepositTypeNotFoundException;
import exception.WrongDepositBalanceException;
import exception.WrongDurationInDayValueException;
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
    private List<Deposit> depositList = new ArrayList<>();
    private Deposit deposit = new Deposit();
    public NodeList nodeList;
    public int depositNum;

    public Document parseXmlFile() throws ParserConfigurationException, IOException, SAXException {
        File inputXml = new File("Deposit.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(inputXml);
    }

    public List<Deposit> parseDocument(Document document) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, DepositTypeNotFoundException, WrongDepositBalanceException, WrongDurationInDayValueException {
        Element docElement = document.getDocumentElement();
        nodeList = docElement.getElementsByTagName("deposit");
        if (nodeList != null && nodeList.getLength() > 0) {
            for ( depositNum = 0; depositNum < nodeList.getLength(); depositNum++) {
                String customerNumber = docElement.getElementsByTagName("customerNumber").item(depositNum).getTextContent();
                String depositTypeStr = "depositType." + docElement.getElementsByTagName("depositType").item(depositNum).getTextContent();
                BigDecimal depositBalance = new BigDecimal(docElement.getElementsByTagName("depositBalance").item(depositNum).getTextContent());
                int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(depositNum).getTextContent());
               // try {
                    checkDepositType(depositTypeStr);
                   checkDepositBalance(depositBalance);
                    checkDurationInDays(durationInDays);
                    deposit.setCustomerNumber(customerNumber);
                    deposit.setDepositBalance(depositBalance);
                    deposit.setDurationInDays(durationInDays);
                    deposit.calculatePayedInterest();
                    depositList.add(deposit);
                //}
                /*
                catch (DepositTypeNotFoundException e) {
                    System.out.println("class not found////");
                } catch (WrongDepositBalanceException e) {
                    System.out.println("class not neg////");
                } catch (WrongDurationInDayValueException e) {
                    System.out.println("not neg and zero////");
                } finally {
                    continue;
                }
                */
            }
        } return depositList;
    }
    public void checkDepositBalance (BigDecimal depositBalance) throws WrongDepositBalanceException { if (depositBalance.compareTo(BigDecimal.ZERO) < 0)
        throw new WrongDepositBalanceException("depositBalance can`t be negative ");}

    public void checkDurationInDays (int durationInDays) throws WrongDurationInDayValueException {
        if (durationInDays <= 0)
            throw new WrongDurationInDayValueException("durationInDays can`t be zero or negative ");
    }

    public void checkDepositType(String depositTypeStr) throws IllegalAccessException, InstantiationException, ClassNotFoundException, DepositTypeNotFoundException {
        try {
            Class depositTypeClass = Class.forName(depositTypeStr);
            DepositType depositType = (depositType.DepositType) depositTypeClass.newInstance();
            deposit.setDepositType(depositType);
        }catch(ClassNotFoundException e)
        {
            throw new  DepositTypeNotFoundException("");
        }
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
}
