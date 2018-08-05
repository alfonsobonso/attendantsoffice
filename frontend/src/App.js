import React from 'react';
import {
    BrowserRouter as Router,
    Redirect
} from "react-router-dom";
import AppRoutes from './AppRoutes.js'
import Login  from './login/Login.js';
import logo from './logo.svg';
import './App.css';

class App extends React.Component {

    constructor(props) {
        super(props)
        // initally unchecked
        this.state = {authentiated : false };
    }

    state = {};

    componentDidMount() {
        setInterval(this.hello, 250000);
        this.validateAuthentication();
    }

    hello = () => {
        fetch('/api/hello', {
            credentials: 'same-origin'
        })
        .then(response => {
            if(response.ok) {
                return response.text();
            }
            return `Request rejected with status ${response.status}`;
        })
        .then(message => {
            this.setState({message: message});
        });
    };

    validateAuthentication = () => {
        // hardwire for now
        this.setState({authentiated : false });

    }

    render() {
        return (
                <div className="App">
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo"/>
                        <h1 className="App-title">{this.state.message}</h1>
                    </header>
                    <AppRoutes authenticated={this.state.authenticated} />
                </div>
        );
    }
}

export default App;
