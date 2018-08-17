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

import ErrorBoundary from './error/ErrorBoundary.js'
import Login  from './login/Login.js';
import TokenAccess  from './login/TokenAccess.js';

// the render here is only used to differentiate the app, 
// which uses a higher-order component (HOC) for authentication, and the pages around authentication
// the app component then does the remaining routing.
ReactDOM.render(
    <ErrorBoundary>
        <Router>
            <Switch>           
                <Route exact path="/login" component={Login} />
                <Route exact path="/token-access/:token" component={TokenAccess} />
                <Route path="/" component={App} />
            </Switch>
        </Router>
    </ErrorBoundary>
    , document.getElementById('root')

);

registerServiceWorker();
