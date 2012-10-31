package com.ascf.jwt.appstore.dirparser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.ascf.jwt.appstore.Utils;

public class DirXMLParse {
	private static final String TAG = "DirXMLParse";
    private static final String TAG_DIRECTORY = "derictory";
    private static final String TAG_ITEM = "item";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_LEVEL = "level";
    private static final String ATTR_ID = "ext_id";
    private static final String ATTR_DISPLAYNAME = "display_name";
    private static final String ATTR_SRC = "src";
    private static final String ATTR_ENABLE = "enable";

    public DirXMLParse (){ 
        
    }

    public static List<Directory> getDirectfromXML(String url) {

        List<Directory> directories = new ArrayList<Directory>();
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
            NodeList nodes = root.getElementsByTagName(TAG_DIRECTORY);

            // 遍历根节点所有子节点,Directories 下所有Directory,已经directory下所有Item
            int dirCount = 0;
            int itemCount = 0;
            if (null != nodes){
                dirCount = nodes.getLength();
            }
            for (int i = 0; i < dirCount; i++){
                Directory dir = new Directory();
                Element dirElement=(Element)(nodes.item(i));
                
                dir.setmName(dirElement.getAttribute(ATTR_NAME));
                dir.setLevel(Integer.parseInt(dirElement.getAttribute(ATTR_LEVEL)));
                NodeList items = dirElement.getElementsByTagName(TAG_ITEM);
                if (null != items){
                    itemCount = items.getLength();
                }
                List<Item> itemList = new ArrayList<Item>();
                for (int j = 0; j < itemCount; j++) {
                    Element oneItem = (Element) items.item(j);
                    Item n = new Item(Integer.parseInt(oneItem.getAttribute(ATTR_ID)),
                                       oneItem.getAttribute(ATTR_DISPLAYNAME),
                                       oneItem.getAttribute(ATTR_SRC),
                                       Boolean.parseBoolean(oneItem.getAttribute(ATTR_ENABLE)));
                    itemList.add(n);
                }
                dir.setElements(itemList);
                directories.add(dir);
            }

        } catch (Exception e) {
        	Log.e(TAG, "parsing XML", e);
        }
        return directories;
    }
}
