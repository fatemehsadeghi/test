import deposits.Deposit;
import exception.DepositTypeNotFoundException;
import exception.WrongDepositBalanceException;
import exception.WrongDurationInDayValueException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import parser.ParsingXml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, WrongDepositBalanceException, WrongDurationInDayValueException, DepositTypeNotFoundException, IOException, SAXException {

        ParsingXml parseXml = new ParsingXml();
      //  int j = parseXml.nodeList.getLength();
      //  int i = parseXml.depositNum;
        //System.out.println(i);
        Deposit deposit = new Deposit();
        Document document = parseXml.parseXmlFile();
        parseXml.parseDocument(document);


        /*
       parseXml.writeOnFile(
               */
    }
}
