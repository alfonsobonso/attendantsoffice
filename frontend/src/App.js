import React, { Component } from 'react';
import logo from './logo.svg';

import HeaderAppBar from './common/HeaderAppBar';

import AuthenticationService from './authentication/AuthenticationService';
import withAuthenticationService from './authentication/withAuthenticationService';

import AppRoutes from './AppRoutes'

const AuthService = new AuthenticationService();

class App extends Component {
   
    handleLogout(){
        AuthService.logout()
        this.props.history.replace('/login');
    }

    render() {
        return(
            <div className="App">
                <header>
                    <HeaderAppBar history={this.props.history} />
                </header>
                <AppRoutes />                
            </div>
        );
    }
}

export default withAuthenticationService(App);
