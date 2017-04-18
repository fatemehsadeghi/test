import deposits.Deposit;
import exception.DepositTypeNotFound;
import exception.WrongDepositBalance;
import exception.WrongDurationInDayValue;
import interest.DepositInterest;
import org.xml.sax.SAXException;
import parser.ParsingXml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, WrongDepositBalance, WrongDurationInDayValue, DepositTypeNotFound, IOException, SAXException {
        ParsingXml parseXml = new ParsingXml();
       parseXml.writeOnFile(parseXml.parseDocument(parseXml.parseXmlFile()));
    }
}
