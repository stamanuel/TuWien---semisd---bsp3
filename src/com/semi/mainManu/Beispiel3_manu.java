package com.semi.mainManu;


import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes; 

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.*;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/*
  
 Bambabam des m�sst gehn... glaub das sogar gegen as xsd file validiert ;) 
 
 */


public class Beispiel3_manu {
	
	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    DOMImplementation impl = builder.getDOMImplementation();
	    String resultXmlString = "";
	    String startPrefixes = "";
	
    public static void main(String[] args) throws Exception {
        // Argumentueberpruefung
        if (args.length != 3) {
            System.err.println("Usage: java Beispiel3 <MEMORY XML-FILE> "
                    + "<EMPTY XML-FILE FOR FILTERING> "
                    + "<EMPTY XML-FILE FOR CHECKING>");
            System.exit(1);
        }

        String xmlInput = args[0];
        String filteredXML = args[1];
        String checkedXML = args[2];

        Beispiel3_manu beispiel = new Beispiel3_manu();

        beispiel.sax(xmlInput, filteredXML);
        beispiel.dom(filteredXML, checkedXML);
    }

    private Beispiel3_manu() throws Exception {
    }

    /**
     * Vervollstaendigen Sie die Methode. Der Name des XML-Files, welches
     * verarbeitet werden soll, wird mittels Parameter "memoryXML" uebergeben.
     *
     * Verwenden Sie fuer die Loesung dieser Teilaufgabe einen SAX-Prozessor. 
     * Der gefilterte und bearbeitete InputStream soll mittels eines
     * Transformers auf das File "filteredMemoryXML" geschrieben werden.
     */
    private void sax(String memoryXML, String filteredMemoryXML)
            throws Exception {
    	
    	//read the fucking file
    	FileInputStream fis= new FileInputStream(memoryXML);
    	InputSource is = new InputSource (fis);
    	
    	//create the fucking file and set the handlers
    	XMLReader xr=XMLReaderFactory.createXMLReader();
		
        DataFilter filter = new DataFilter(filteredMemoryXML);
        filter.setParent(xr);
        filter.setContentHandler(new MyContentHandler());

        filter.parse(is);  
        System.out.print(resultXmlString);
        
        String T = prettyFormat(resultXmlString, 2);
        System.out.print(T);
    }
    
    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            StreamResult xmlOutput2 = new StreamResult(new File("test.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer(); 
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            transformer.transform(xmlInput, xmlOutput2);
            //transformer.transform(xmlInput, xmlOutput);
            return null;//xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }
    
	
    /**
     * Vervollstaendigen Sie die Methode. Der Name des XML-Files, welches
     * verarbeitet werden soll, wird mittels Parameter "filteredMemoryXML"
     * uebergeben.
     *
     * Verwenden Sie fuer die Loesung dieser Teilaufgabe einen DOM-Baum. Das 
     * manipulierte Document soll mittels eines Transformers in leserlicher
     * Form in das File "checkedMemoryXML" ausgegeben werden.
     */
    private void dom(String filteredMemoryXML, String checkedMemoryXML)
            throws Exception {
	}
    
    /* ContentHandler mit allen notwendigen Methoden
     */
    
    private class MyContentHandler implements ContentHandler{

    	
    	public void characters(char[] ch, int start, int length)
        throws SAXException {
    	 		
    		for(int i=start;i<start+length;i++) {
    			resultXmlString = resultXmlString + ch[i];
    			System.out.print(ch[i]);
    		}
    }
    	

    // Methode wird aufgerufen wenn der Parser zu einem Start-Tag kommt
    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException {
    
    	
    	     if(qName == "")
    	    	 	resultXmlString+="<"+localName+" ";
    	     else
    	    		resultXmlString+="<"+qName+"";
    	    	 
    	     if(startPrefixes.length() > 0)
    	     {
    	    	 resultXmlString += " " +startPrefixes;
    	    	 startPrefixes = "";
    	    		 
    	     }
    	     
    	       if(atts!=null)
    	       {
    	        for( int i=0; i<atts.getLength(); i++ )
    	        {
    	         String aName = atts.getLocalName(i); // Attr name
    	        
    	        resultXmlString += " ";
    	         
    	        resultXmlString+=aName +"=\"" +atts.getValue(i)+ "\"";
    	        }
    	        }
    	   
    	       resultXmlString+=">";
    	     
    	    
    }

    // Methode wird aufgerufen wenn der Parser zu einem End-Tag kommt
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
    	
    	
    	
    	 if(qName == "")
	    	 	resultXmlString+="</"+localName+">";
	     else
	    		resultXmlString+="</"+qName+">";
    	
    	System.out.println("</" + localName + ">");
    }

    public void endDocument() throws SAXException {}
    public void endPrefixMapping(String prefix) throws SAXException {
    	//resultXmlString = resultXmlString + prefix;
    	System.out.println("XXX "+prefix+" ");
    }
    public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {}
    public void processingInstruction(String target, String data)
        throws SAXException {
    	resultXmlString = resultXmlString + target + data;
    	System.out.println(" "+target+" " + data);
    }
    public void setDocumentLocator(Locator locator) {}
    public void skippedEntity(String name) throws SAXException {}
    public void startDocument() throws SAXException {
    	resultXmlString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    }
    public void startPrefixMapping(String prefix, String uri)
      throws SAXException {
    	//resultXmlString += "xmlns:" + prefix + "=\"" + uri + "\"\n";
    	
    	startPrefixes = startPrefixes + "xmlns:" + prefix + "=\"" + uri + "\"\n";
    	System.out.println("xmlns:" + prefix + "=\"" + uri + "\"");
    }
	
    }
    
    
   private class DataFilter extends XMLFilterImpl
   {
	   private int kind;
	   private boolean delete_pairs;
	   private String filteredMemoryXML;
	   
	   public DataFilter(String filteredMemoryXML)
	   {
		   this.kind=0;	  
		   this.delete_pairs=false;
		   this.filteredMemoryXML=filteredMemoryXML;
	   }
	   
	   private void set_delete_pairs()
	   {
		   this.delete_pairs=true;   
	   }
	   
	   private void unset_delete_pairs()
	   {	   
		   this.delete_pairs=false;
	   }
	   
	   private boolean delete_pairs()
	   {
		   return this.delete_pairs;	   
	   }
	   	   
	   private void increase_kindelement()
	   {
		   this.kind++;
	   }
	   
	   private void decrease_kindelement()
	   {
		   this.kind--;
	   }
	   
	   private boolean is_game_child()
	   {
		   if(this.kind==1)
		   {
			   return true;
			   
		   }
		   else
		   {
			   return false;
		   }	   
	   }
	   
	   
	   public void startElement (String uri, String localName, String qName, 
			   Attributes attributes) throws SAXException {
		   
		   
		   	if(localName.equals("covered-pairs"))
		   	{
		   		//set_delete_pairs();
		   	}  
		   	else if(localName.equals("uncovered-pairs") && is_game_child())
		   	{	   		
		   		//set_delete_pairs();
		   	}
		   	else if(localName.equals("pair") && delete_pairs())
		   	{
		   		//DO NOTHING (DELETE)
		   	}
		   	else
		   	{
		   		super.startElement(uri, localName, qName, attributes);
		   	}	
		   	
		    increase_kindelement();
		   	
      }
	   
	   
	   public void endElement(String uri, String localName, String qName)
        	throws SAXException {
	    	
	    	decrease_kindelement();
	    	
		   	if(localName.equals("covered-pairs"))
		   	{
		   		unset_delete_pairs();
		   	}  
		   	else if(localName.equals("uncovered-pairs") && is_game_child())
		   	{	   		
		   		unset_delete_pairs();
		   	}
		   	else if(delete_pairs() && localName.equals("pair"))
		   	{
		   		//DO NOTHING (DELETE)
		   	}
		   	else
		   	{
		   		super.endElement(uri, localName, qName);
		   	}  	
	    }   
	   
	  
	   public void processingInstruction(String target, String data)
        throws SAXException {
	    	super.processingInstruction(target, data);
    }
   
	   
	   public void characters(char[] ch, int start, int length)
       		throws SAXException {
		   
		   	if(delete_pairs())
		   	{
			   //DO NOTHING (DELETE)
		   	}
		   	else
		   	{
		   		super.characters(ch, start, length);
		   	}
	}
	   
	   public void startPrefixMapping(String prefix, String uri)
	      throws SAXException {
		   super.startPrefixMapping(prefix, uri);
	    }
   
   
   }
    
}
