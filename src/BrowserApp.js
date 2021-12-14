import React, { Component } from 'react'
import { BrowserRouter,HashRouter, Routes, Route } from 'react-router-dom'


import Polygon from "./polygon";
import Ethereum from './ethereum';


class App extends Component {
  render() {
    return (<div className="app">
              <HashRouter>
                <Routes>
                  <Route path="/" element={<Ethereum />} />
                  <Route  path="/polygon" element={<Polygon />} />
                  <Route  path="/ethereum" element={<Ethereum />} />
                </Routes>
              </HashRouter>
            </div>
           )
     }
}
export default App;

