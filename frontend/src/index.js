import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {
    BrowserRouter as Router,
    Route,  
    Switch
} from "react-router-dom";
import registerServiceWorker from './registerServiceWorker';

import Login  from './login/Login.js';

// the render here is only used to differentiate the app, 
// which uses a higher-order component (HOC) for authentication, and the pages around authentication
// the app component then does the remaining routing.
ReactDOM.render(
    <Router>
        <Switch>           
            <Route exact path="/login" component={Login} />
            <Route path="/" component={App} />
        </Switch>
    </Router>
    , document.getElementById('root')
);

registerServiceWorker();
