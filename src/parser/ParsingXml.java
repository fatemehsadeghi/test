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
    public Document parseXmlFile() throws ParserConfigurationException, IOException, SAXException {
        File inputXml = new File("Deposit.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(inputXml);
    }
    public List<Deposit> parseDocument(Document document) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, DepositTypeNotFoundException, WrongDepositBalanceException, WrongDurationInDayValueException {
        Element docElement = document.getDocumentElement();
        NodeList nodeList = docElement.getElementsByTagName("deposit");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String customerNumber =docElement.getElementsByTagName("customerNumber").item(i).getTextContent();
                String depositTypeStr = "depositType."+ docElement.getElementsByTagName("depositType").item(i).getTextContent();
                Class depositTypeClass = Class.forName(depositTypeStr);
                DepositType depositType = (depositType.DepositType)depositTypeClass.newInstance();
                BigDecimal depositBalance = new BigDecimal(docElement.getElementsByTagName("depositBalance").item(i).getTextContent());
                if (depositBalance.compareTo(BigDecimal.ZERO) < 0)
                        throw new WrongDepositBalanceException("depositBalance can`t be negative at "+ i + 1 +"th deposit");
                int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(i).getTextContent());
                if (durationInDays <= 0)
                        throw new WrongDurationInDayValueException("durationInDays can`t be zero or negative at 	"+ i + 1 +"th deposit");
                deposit.setCustomerNumber(customerNumber);
                deposit.setDepositType(depositType);
                deposit.setDepositBalance(depositBalance);
                deposit.setDurationInDays(durationInDays);
                deposit.calculatePayedInterest();
                depositList.add(deposit);
            }
        }return depositList;
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
