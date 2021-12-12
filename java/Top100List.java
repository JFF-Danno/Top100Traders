package mvpnft;

import dashboard.Balances;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mvpnft.opensea.AssetServlet;
import mvpnft.opensea.TraderRecord;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.utils.Convert;

/**
 *
 * @author danielo
 */
public class Top100List  extends HttpServlet
	
	/*
	currently this is a manual process to be run once per day. Results are saved in a json file and served from that.
	
	*/
		
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
	Enumeration<String> paramNames = req.getParameterNames();
	String sAction = req.getParameter( "Action" );
	if ( sAction != null ) {
	     if ( sAction.equals( "localTop100" ) ) {
		LinkedList<TraderRecord> lTraders = TraderRecord.getTradersTop50();//new LinkedList<TraderRecord>();
		String sJson = "[";
		int items = 0;
		for( TraderRecord tr : lTraders ) {
		    if ( items++ > 0 ) {
		        sJson += ",";
		    }
		    sJson += "{ \"rank\": \"" + items + "\",\"link\":\"" + "https://opensea.io/" + tr.sAddress +"\",\"sold\":\"" + tr.sTotalSells +"\",\"genesis\":\"" + tr.block1stTrade +"\",\"avgprice\":\"" + tr.getAverage() +"\",\"roi\":\"" + tr.getROI() + "\",\"buys\":\"" + tr.iBuys +"\",\"profit\":\"" +  tr.bTotal.toPlainString()  +  "\"}";
	   	}
		
		
		sJson += "]";
		resp.addHeader("Access-Control-Allow-Origin", "*" );

		PrintWriter out = resp.getWriter();
		out.print( sJson );
		out.flush();
	     } else if ( sAction.equals( "Top100" ) ) {
		       // File path is passed as parameter
		File file = new File( "/home/danielo/scrapes/top25.txt" );

		// Creating an object of BufferedReader class
		BufferedReader br
		    = new BufferedReader(new FileReader(file));

		String st;
		String sJson = "[";

		int id = 0;
		int linectr = 0;
		String sLink = "";
		String sProfit = "";
		String sBuy = "";
		String sAvgPrice = "";
		String sTotalSold = "";
		String sROI = "";
		String sGenesis = "";
		
		int items = 0;
		while ((st = br.readLine()) != null) {
		   
		    if ( linectr > 12 ) {
			linectr = 0;
			id = 0;
			System.out.println( "#################" );
		    }
		   // System.out.println( st + " " + );
			    linectr++;
		    try {
			    if ( id == 0 ) {
				System.out.println( st );
				id = Integer.parseInt( st );
				System.out.println( "id " + id );

			    }
		    }			
		    catch ( Exception e ) {
    			System.out.println( "error " + e.getMessage() );

			    }
		    if ( linectr == 2 ) {
			sLink = st;
		    }
		    if ( linectr == 3 ) {
			sBuy = st;
		    }
		    if ( linectr == 4 ) {
			sAvgPrice = st;
		    }
		    if ( linectr == 6 ) {
			sTotalSold = st;
		    }
		    if ( linectr == 9 ) {
			sProfit = st;
		    }
		    if ( linectr == 10 ) {
			sROI = st;
		    }
		     if ( linectr == 12 ) {
			sGenesis = st;
		    }
		    
		
		    
		    if ( id > 0 && linectr == 12 ) {
			System.out.println( "link " + sLink + " profit " + sProfit + " buys "  +sBuy );
			items++;
			if ( items > 1 ) {
			    sJson += ",";
			}
			sJson += "{ \"rank\": \"" + id + "\",\"link\":\"" + sLink +"\",\"sold\":\"" + sTotalSold+"\",\"genesis\":\"" + sGenesis +"\",\"avgprice\":\"" + sAvgPrice +"\",\"roi\":\"" + sROI + "\",\"buys\":\"" + sBuy +"\",\"profit\":\"" + sProfit +  "\"}";
		    }
		    
		    
	        }
		
		sJson += "]";
		resp.addHeader("Access-Control-Allow-Origin", "*" );

		PrintWriter out = resp.getWriter();
		out.print( sJson );
		out.flush();

	    } else if ( sAction.equals( "Top100Polygon" ) ) {
		       // File path is passed as parameter
		File file = new File( "/home/danielo/scrapes/polytop100_1.txt" );

		// Creating an object of BufferedReader class
		BufferedReader br
		    = new BufferedReader(new FileReader(file));

		String st;
		String sJson = "[";

		int id = 0;
		int linectr = 0;
		String sLink = "";
		String sProfit = "";
		String sBuy = "0";
		String sAvgPrice = "0";
		String sTotalSold = "0";
		String sROI = "0";
		String sGenesis = "0";
		String sSpent = "0";
		String sAddress = "";
		int items = 0;
		while ((st = br.readLine()) != null) {
		   
		    if ( linectr > 3 ) {
			linectr = 0;
			System.out.println( "#################" );
		    }
		   // System.out.println( st + " " + );
		    linectr++;
		    
			    
		    if ( linectr == 1 ) {
			sAddress = st;
			id++;
		    }
		    if ( linectr == 2 ) {
			sProfit = st;
		    }
		    if ( linectr == 3 ) {
			sTotalSold = st;
		    }
		    if ( linectr == 4 ) {
			sSpent = st;
		    }
		    
		    if ( id > 0 && linectr == 4 ) {

			TradeData data = getTradeData( sAddress.replace( "\\", "0" ) );
			
			BigDecimal bAverage = new BigDecimal( sSpent ).divide( new BigDecimal( data.iBuys ), 2, RoundingMode.HALF_UP );
			sGenesis = data.sDate;
			BigDecimal roi = new BigDecimal( sTotalSold ).divide( new BigDecimal( sSpent ), 2, RoundingMode.HALF_UP );
			roi = roi.multiply( new BigDecimal( 100 ) );
			items++;
			
			if ( items > 1 ) {
			    sJson += ",";
			}
			sJson += "{ \"rank\": \"" + id + "\",\"link\":\"" + "https://opensea.io/" + sAddress.replace( "\\", "0" ) 
				+ "\",\"sold\":\"" + data.iSold +"\",\"genesis\":\"" + sGenesis.substring( 0, 10 ) +"\",\"avgprice\":\"" + bAverage +"\",\"roi\":\"" 
				+ roi.intValue() + "\",\"buys\":\"" + data.iBuys +"\",\"profit\":\"" + sProfit +  "\"}";
			
			System.out.println( sJson );

		    }
		    
		    
	        }
		
		sJson += "]";
		resp.addHeader("Access-Control-Allow-Origin", "*" );

		PrintWriter out = resp.getWriter();
		out.print( sJson );
		out.flush();

	    }
	}
    }
    
    public TradeData getTradeData( String sAddress ) throws IOException {
	
	int offset = 0;
	int iBuys = 0;
	int iReceived =0;
	String sGenDate = "";
	int iGenBlock = 0;
	boolean hasMore = true;
	int iMints = 0;
	int iSold = 0;
	while ( hasMore ) {
	   
	    try {
		Thread.sleep(2000);
	    } catch (InterruptedException ex) {
		Logger.getLogger(AssetServlet.class.getName()).log(Level.SEVERE, null, ex);
	    }
	
	    String sJson = "";
	    CloseableHttpClient httpclient = HttpClients.createDefault();
	    try {
                HttpGet httpget;
                httpget = new HttpGet( "https://deep-index.moralis.io/api/v2/" + sAddress + "/nft/transfers?chain=polygon&format=decimal&direction=both&offset=" + offset );
		httpget.addHeader( "accept", "application/json" );
		httpget.addHeader( "X-API-Key", "QeJPv8y6Vn670g7iVEUuZAXmy4ewEOZ1MCwhfdgdejRXyQkt2C4EJbsvRcsot9IC" );

		CloseableHttpResponse response = null;
		try {
		    response = httpclient.execute( httpget );

		    HttpEntity entity = response.getEntity();
		    if (entity != null) {
			sJson = EntityUtils.toString(entity);
		    }
		    } catch ( IOException ex ) {
			Logger.getLogger( Balances.class.getName() ).log( Level.SEVERE, null, ex );
		    }finally {
			response.close();
		    }
		} catch ( Exception e ) {
		System.out.println( "error in httpclient " + e.getMessage() );
		} finally {
		    httpclient.close();
		}

		long lTime = 0;
		try {
			JSONObject jo		= new JSONObject( sJson );
			JSONArray tradeArray	= jo.getJSONArray( "result" );
			int iTotal = jo.getInt( "total" );			    
			int iPage = jo.getInt( "page" );
			offset += 500;
			hasMore = iTotal > offset; 
			if ( ! hasMore ) {
			    offset = 0;
			}
			System.out.println( "offset " + offset + " hasmore " + hasMore );
			System.out.println( "size " + jo.getInt( "page_size" ) + " total " + jo.getInt( "total" )  );
			

			for( int i = 0; i < tradeArray.length(); i++ ) {   
                            try {
				
				    JSONObject assetObject	    = tradeArray.getJSONObject( i );
				  //  System.out.println( assetObject.getString( "value" ) + " : " +  assetObject.getString( "value" ).equals( "0" ) );
				  
				    String sValue = "";
				    try {
					sValue = assetObject.get( "value" ) + "";
				    } catch ( Exception e ) {
					sValue = "0";
				    }
				  
				    if ( assetObject.has( "value" ) && sValue.equals( "0" ) ) {
					iMints++;
				    }
				    if ( assetObject.getString( "to_address" ).equals( sAddress ) && ! sValue.equals( "0" ) ) {
					iBuys++;
					//System.out.println( iGenBlock + " : " +  assetObject.getInt( "block_number" )  );
						
					if ( iGenBlock > assetObject.getInt( "block_number" ) || iGenBlock == 0 ) {
					    iGenBlock = assetObject.getInt( "block_number" );
					    sGenDate = assetObject.getString( "block_timestamp" );
					}
					
				    } else if ( assetObject.getString( "from_address" ).equals( sAddress ) ) {
					iSold++;
					//System.out.println( iGenBlock + " : " +  assetObject.getInt( "block_number" )  );
						
					if ( iGenBlock > assetObject.getInt( "block_number" ) || iGenBlock == 0 ) {
					    iGenBlock = assetObject.getInt( "block_number" );
					    sGenDate = assetObject.getString( "block_timestamp" );
					}
					
				    }
				    
				 
                                 }
                            catch ( Exception e ) {
                                System.out.println( " " + e.getMessage() );
                            }
			}
				
                    } catch( Exception e ) {
			System.out.println( " " + e.getMessage() );
                    }
	}
	System.out.println( " mints" + iMints );
	System.out.println( " buys" + iBuys );
	System.out.println( " sold" + iSold );

	return new TradeData( iBuys + iMints, iSold, sGenDate );
		
    }
}
    
class TradeData {
    
    public int iBuys;
    public String sDate;
    public int iSold;
        
    public TradeData( int iBuys, int iSold, String sGenDate ) {
	this.iBuys = iBuys;
	this.sDate   = sGenDate;
	this.iSold  = iSold;
	System.out.println( "td " + iBuys + " " + sGenDate );
    }
}