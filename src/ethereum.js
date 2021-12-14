import React, { useState , useEffect } from "react";
import moralis from "moralis";
import Web3 from 'web3';
import './App.css';
import axios from 'axios';
import Banner from './images/banner.png';
import Row from './images/row.png';
import rank1 from './images/1st.png';
import rank2 from './images/2nd.png';
import rank3 from './images/3rd.png';
import follow from './images/follow.png';
import address from './images/Address.png';
import rank from './images/Rank.png';
import buys from './images/buys.png';
import avgprice from './images/avgprice.png';
import sold from './images/sold.png';
import profit from './images/Profit.png';
import roi from './images/ROI.png';
import genesis from './images/Genesis.png';
import ethereum from './images/eth.png';
import polygon from './images/polygon.png';
import polygonfade from './images/polygon-fade.png';
import solana from './images/solana-cs.png';
import bnb from './images/bnb.png';
import exportimg from './images/export.png';
import dune from './images/dune.png';
import opensea from './images/opensea.png';
import followtitle from './images/followtitle.png';
import moralislogo from './images/moralis.png';
var web3 = new Web3(Web3.givenProvider);
function App() {

   const [ data,          setData       ] = useState([]);
   const [usdConversion,  setUsdPrice   ] = useState(0);
   
  const serverUrl = process.env.REACT_APP_MORALIS_SERVER_URL;
  const appId = process.env.REACT_APP_MORALIS_APPLICATION_ID;
  moralis.start({ serverUrl, appId });
  
  const getUSDValue = async () => {
  const options = {
    address: "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2",
    chain: "eth",
    exchange: "uniswap-v3"
  };
  const price = await moralis.Web3API.token.getTokenPrice(options);
  setUsdPrice(price.usdPrice);
 }
 
 function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
 
 
    const gettop50 = async () => {
 //     const resMint = await axios.get( 'http://localhost:8080/Top100?Action=Top100Polygon', {
     const resMint = await axios.get( 'eth100.json', {
       headers: { "Content-Type": "application/json",
        },   
      });
     // console.log(resMint.data[0]);
      let accountMetaData = [];

      for( let i = 0; i <  resMint.data.length; i++ ) {
        accountMetaData.push(resMint.data[i]);
      }
      const rows = accountMetaData.map((row,index) => 
        
          <li className='row' ><span className='rank' ><div className='contentDiv'>{row.rank == 1 ? <div className="imgspan">&nbsp;</div>: row.rank == 2 ? <div className="imgspan2">&nbsp;</div> : row.rank == 3 ? <div className="imgspan3">&nbsp;</div> : row.rank}</div></span><span className='link' ><div className='contentDiv'><a target='_blank' href={row.link}>{row.link.replace('https://opensea.io/','').substring(0,6)}...{row.link.replace('https://opensea.io/','').substring(37)}</a></div></span><span className='buy' ><div className='contentDiv'>{row.buys}</div></span><span className='avgprice' ><div className='contentDiv'>$ {( row.avgprice * 1 ).toFixed(2) }</div></span><span className='sold' ><div className='contentDiv'>{ row.sold }</div></span><span className='profit' ><div className='contentDiv'>$ { (numberWithCommas( (row.profit * 1 ).toFixed(2) ) ) }</div></span><span className='roi' ><div className='contentDiv'>{(row.roi*1).toFixed(0)} %</div></span><span className='genesis' ><div className='contentDiv'>{ row.genesis }</div></span><span className='follow' ><div className='contentDiv'><a target='_blank' href={row.link}><img className='followimg' src={ follow }/></a></div></span></li>
            
      );
      setData(rows);
  }
  
  
   useEffect(() => {
       getUSDValue();
    gettop50()
            
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [usdConversion])
  

  return (
    <div className="App">
    <div><img src={Banner} /></div>

  <div>
    <ul>
      <li id='toprow' ><span id='topleft' ><span id='polygon'><a href="#/polygon" ><img src={polygonfade} /></a></span><span id='eth'><img src={ethereum} title="Ethereum"/></span><span id='solana'><img title="Coming Soon!" src={solana} /></span><span id='bnb'><img title="Coming Soon!" src={bnb} /></span></span><span><img src={exportimg} /></span></li>
<li className='title row' ><span className='rank' ><div className='contentDiv'><img src={rank} /></div></span><span className='link' ><div className='contentDiv'><img src={address} /></div></span><span className='buy' ><div className='contentDiv'><img src={buys} /></div></span><span className='avgprice' ><div className='contentDiv'><img src={avgprice} /></div></span><span className='sold' ><div className='contentDiv'><img src={sold} /></div></span><span className='profit' ><div className='contentDiv'><img src={profit} /></div></span><span className='roi' ><div className='contentDiv'><img src={roi} /></div></span><span className='genesis' ><div className='contentDiv'><img src={genesis} /></div></span><span className='follow' ><div className='contentDiv'><img  src={followtitle} /></div></span></li>
    {data}
    </ul>    
    <div id="footer"><img src={dune} />&nbsp;&nbsp;&nbsp;<img src={moralislogo} /></div></div>



    </div>
  );
}

export default App;
