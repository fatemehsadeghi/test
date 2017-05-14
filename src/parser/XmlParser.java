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
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class XmlParser {
    public Document parseXmlFile() throws ParserConfigurationException, IOException, SAXException {
        File inputXml = new File("Deposit.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        return documentBuilder.parse(inputXml);
    }

    public List<Deposit> parseDocument(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException, DepositTypeNotFoundException, WrongDepositBalanceException, WrongDurationInDayValueException {
        List<Deposit> depositList = new ArrayList<>();
        Element docElement = document.getDocumentElement();
        NodeList nodeList = docElement.getElementsByTagName("deposit");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int depositNum = 0; depositNum < nodeList.getLength(); depositNum++) {
                try {
                    Deposit deposit = new Deposit();
                    String customerNumber = docElement.getElementsByTagName("customerNumber").item(depositNum).getTextContent();
                    String depositTypeStr = "depositType." + docElement.getElementsByTagName("depositType").item(depositNum).getTextContent();
                    BigDecimal depositBalance = new BigDecimal(docElement.getElementsByTagName("depositBalance").item(depositNum).getTextContent());
                    int durationInDays = Integer.parseInt(docElement.getElementsByTagName("durationInDays").item(depositNum).getTextContent());
                    Class depositTypeClass = Class.forName(depositTypeStr);
                    DepositType depositType = (depositType.DepositType) depositTypeClass.newInstance();
                    deposit.setDepositType(depositType);
                    checkDepositBalance(depositBalance);
                    checkDurationInDays(durationInDays);
                    deposit.setCustomerNumber(customerNumber);
                    deposit.setDepositBalance(depositBalance);
                    deposit.setDurationInDays(durationInDays);
                    deposit.calculatePayedInterest();
                    depositList.add(deposit);
                    Collections.sort(depositList, new Comparator<Deposit>() {
                        public int compare(Deposit deposit1, Deposit deposit2) {
                            return deposit2.getPayedInterest().compareTo(deposit1.getPayedInterest());
                        }
                    });
                } catch (ClassNotFoundException e) {
                    System.out.println("DepositType not found");
                } catch (WrongDepositBalanceException e) {
                    System.out.println("Deposit Balance can`t be negative");
                } catch (WrongDurationInDayValueException e) {
                    System.out.println("DurationIn Days cant be negative and zero");
                }
            }
        }
        return depositList;
    }

    private void checkDepositBalance(BigDecimal depositBalance) throws WrongDepositBalanceException {
        if (depositBalance.compareTo(BigDecimal.ZERO) < 0)
            throw new WrongDepositBalanceException("");
    }

    private void checkDurationInDays(int durationInDays) throws WrongDurationInDayValueException {
        if (durationInDays <= 0)
            throw new WrongDurationInDayValueException("");
    }

    public void writeOnFile(List<Deposit> depositList) throws IOException {
        RandomAccessFile outputFile = new RandomAccessFile("Output.txt", "rw");
        for (Deposit deposit : depositList) {
            System.out.println(deposit.getCustomerNumber() + "# " + deposit.getPayedInterest() + "\n");
            outputFile.writeUTF(String.valueOf(deposit.getCustomerNumber()) + "# " + String.valueOf(deposit.getPayedInterest()) + "\n");
        }
        outputFile.close();
    }
}
