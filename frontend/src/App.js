import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

import AuthenticationService from './authentication/AuthenticationService';
import withAuthenticationService from './authentication/withAuthenticationService';

import AppRoutes from './AppRoutes'


const AuthService = new AuthenticationService();

class Header extends Component {
    render() {
        return(null);
    }
}

class App extends Component {
   
    handleLogout(){
        AuthService.logout()
        this.props.history.replace('/login');
    }

    render() {
        return(
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h2>Welcome {this.props.user}</h2>
                </header>
                <Header />
                <p className="App-intro">
                    <button type="button" className="form-submit" onClick={this.handleLogout.bind(this)}>Logout</button>
                </p>
                <AppRoutes /> 
            </div>
        );
    }
}

export default withAuthenticationService(App);
