package lk.mazarin.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

/**
 * Created by Kasun Perera on 6/12/2016.
 */
@Component
public class SecurityConfigResource {

    public static final String AUTHENTICATING_USER      = "AuthenticatingUser";
    public static final String AUTHENTICATING_USER_TYPE = "AuthenticatingUserType";

    private Document propertiesDoc;
    private NodeList nodeList;
    private InputStream inputStream;

    /**
     * This will contain all the high level property names and values. Children
     * not considered.
     */
    private Map<String, String> properties;

    /**
     * This will contain all the node names who has children and their child nodes.
     */
    private Map<String, NodeList> childProperties;

    public SecurityConfigResource() throws IOException, ParserConfigurationException, SAXException {
        this("microservice-oauth2-config.xml");
    }

    public SecurityConfigResource(String resourcePath) throws IOException, ParserConfigurationException, SAXException {

        inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        propertiesDoc = dBuilder.parse(inputStream);
        if (inputStream != null)
            inputStream.close();

        propertiesDoc.getDocumentElement().normalize();
        nodeList = propertiesDoc.getDocumentElement().getChildNodes();

        properties = new HashMap<String, String>();
        childProperties = new HashMap<String, NodeList>();

        for (int index = 0; index < nodeList.getLength(); ++index){
            Node node = nodeList.item(index);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                properties.put(element.getNodeName(), element.getNodeValue());
                if (element.hasChildNodes()){
                    childProperties.put(element.getNodeName(), element.getChildNodes());
                }
            }
        }
    }

    public String getProperty(String key){
        return properties.get(key);
    }

    public List<AuthenticatingUser> getAuthenticatingUsers(){
        NodeList nodeList = childProperties.get(AUTHENTICATING_USER_TYPE);
        if (nodeList != null){
            List<AuthenticatingUser> users = new ArrayList<AuthenticatingUser>();
            for (int index = 0; index < nodeList.getLength(); ++index){
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) node;
                    AuthenticatingUser user = new AuthenticatingUser(userElement.getAttribute("username"), userElement.getAttribute("password"), userElement.getAttribute("role"));
                    users.add(user);
                }
            }

            return users;
        }

        return null;
    }

}
