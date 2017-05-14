import deposits.Deposit;
import exception.DepositTypeNotFoundException;
import exception.WrongDepositBalanceException;
import exception.WrongDurationInDayValueException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import parser.XmlParser;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, WrongDepositBalanceException, WrongDurationInDayValueException, DepositTypeNotFoundException, IOException, SAXException {

                XmlParser xmlParser = new XmlParser();
                Document document = xmlParser.parseXmlFile();
                List<Deposit>depositList = xmlParser.parseDocument(document);
                xmlParser.writeOnFile(depositList);
    }
}
