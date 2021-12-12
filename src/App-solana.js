import React, { useState , useEffect } from "react";

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
import solana from './images/solana.png';
import bnb from './images/bnb.png';
import exportimg from './images/export.png';

function App() {

   const [ data,     setData ] = useState([]);
    const gettop50 = async () => {
//      const resMint = await axios.get( 'http://localhost:8080/Top100?Action=Top100', {
     const resMint = await axios.get( 'data.json', {
       headers: { "Content-Type": "application/json",
        },   
      });
     // console.log(resMint.data[0]);
      let accountMetaData = [];

      for( let i = 0; i <  resMint.data.length; i++ ) {
        accountMetaData.push(resMint.data[i]);
      }
      const rows = accountMetaData.map((row,index) => 
        
          <li className='row' ><span className='rank' ><div className='contentDiv'>{row.rank == 1 ? <img src={rank1} />: row.rank == 2 ? <img src={rank2} /> : row.rank == 3 ? <img src={rank3} /> : row.rank}</div></span><span className='link' ><div className='contentDiv'><a target='_blank' href={row.link}>{row.link.replace('https://opensea.io/','').substring(0,6)}...{row.link.replace('https://opensea.io/','').substring(37)}</a></div></span><span className='buy' ><div className='contentDiv'>{row.buys}</div></span><span className='avgprice' ><div className='contentDiv'>$ {row.avgprice}</div></span><span className='sold' ><div className='contentDiv'>{row.sold}</div></span><span className='profit' ><div className='contentDiv'>$ {row.profit}</div></span><span className='roi' ><div className='contentDiv'>{row.roi}</div></span><span className='genesis' ><div className='contentDiv'>{row.genesis.substring(0,10)}</div></span></li>
            
      );
      setData(rows);
  }
  
  
   useEffect(() => {
   
    gettop50()
            
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])
  

  return (
    <div className="App">
    <div><img src={Banner} /></div>
  <div>
    <ul>
      <li id='toprow' ><span id='topleft' ><span id='eth'><img src={ethereum} /></span><span id='solana'><img src={solana} /></span><span id='bnb'><img src={bnb} /></span></span><span><img src={exportimg} /></span></li>
<li className='title row' ><span className='ranktitle' ><div className='contentDiv'><img src={rank} /></div></span><span className='link' ><div className='contentDiv'><img src={address} /></div></span><span className='buy' ><div className='contentDiv'><img src={buys} /></div></span><span className='avgprice' ><div className='contentDiv'><img src={avgprice} /></div></span><span className='sold' ><div className='contentDiv'><img src={sold} /></div></span><span className='profit' ><div className='contentDiv'><img src={profit} /></div></span><span className='roi' ><div className='contentDiv'><img src={roi} /></div></span><span className='genesis' ><div className='contentDiv'><img src={genesis} /></div></span></li>
    {data}
    </ul>    </div>



    </div>
  );
}

export default App;
