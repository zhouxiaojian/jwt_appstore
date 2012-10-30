package com.ascf.jwt.appstore.dirparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.ascf.jwt.appstore.Utils;

public class DirXMLParse {

    private static final String DIRECTORY_TAG = "derictory";
    private static final String ITEM_TAG = "item";
    private static final String NAME_ATTR = "name";
    private static final String LEVEL_ATTR = "level";
    private static final String ID_ATTR = "ext_id";
    private static final String DISPLAYNAME_ATTR = "display_name";
    private static final String SRC_ATTR = "src";
    private static final String ENABLE_ATTR = "enable";

    public DirXMLParse (){ 
        
    }

    public static List<Directory> getDirectfromXML(String url) {

        List<Item> item = new ArrayList();
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        InputStream inputStream = Utils.getInputStreamFromServer(url);

        factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
            // find root Element
            Element root = document.getDocumentElement();
            NodeList nodes = root.getElementsByTagName(DIRECTORY_TAG);
            // 遍历根节点所有子节点,rivers 下所有river

            int dirCount = nodes.getLength();
            int itemCount = 0;
            for (int i = 0; i < dirCount; i++){
                Directory dir = new Directory();
                Element dirElement=(Element)(nodes.item(i));
                
                dir.setmName(dirElement.getAttribute(NAME_ATTR));
                dir.setLevel(Integer.parseInt(dirElement.getAttribute(LEVEL_ATTR)));
                
            }
            
            
            Directory dir = new Directory();
            
            
            
            
            
        } catch (Exception e) {

        }

        return null;
    }
}
