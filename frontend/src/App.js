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
    // render() {
    //     return(
    //         <div>      
    //             <AuthConsumer> 
    //                 {({userInfo, isLoading, error}) => ( 
    //                     userInfo ? (<span>Hi {userInfo.username}</span>): (null)         
    //                 )}
    //             </AuthConsumer>
    //         </div>
    //     );
    // }
}

class App extends Component {
   
    handleLogout(){
        AuthService.logout()
        this.props.history.replace('/login');
    }

    // state = {};

    // componentDidMount() {
    //     //  setInterval(this.hello, 250000);

    // }

    // hello = () => {
    //     fetch('/api/hello', {
    //         credentials: 'same-origin'
    //     })
    //     .then(response => {
    //         if(response.ok) {
    //             return response.text();
    //         }
    //         return `Request rejected with status ${response.status}`;
    //     })
    //     .then(message => {
    //         this.setState({message: message});
    //     });
    // };

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
        // return (
        //         <div className="App">
        //             <header className="App-header">
        //                 <img src={logo} className="App-logo" alt="logo"/>
        //                 <h1 className="App-title">{this.state.message}</h1>
        //             </header>
        //             <AuthProvider authUrl={'/api/authentication'}>
        //                 <div>
        //                     <Header />
        //                     <AppRoutes />
        //                 </div>
        //             </AuthProvider>                    
        //         </div>
        // );
    }
}

export default withAuthenticationService(App);
