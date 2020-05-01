package deserialization;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

public class SchemaValidation {




    public boolean validateXmlFile(File xmlFile,File schema){


        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA,schema);
            Schema schema1 = schemaFactory.newSchema(schema);
            Validator validator = schema1.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
