import React, { Component } from 'react';
import AuthenticationService from './AuthenticationService';

// decorator around the components represending the pages
// if the user is not logged in at this point we redirect to the login page
export default function withAuthenticationService(AuthComponent) {
    const AuthService = new AuthenticationService();

	return class AuthWrapped extends Component {
	    constructor() {
	 	   	super();
	    	this.state = {
	        	user: null
	    	}
		}

		componentWillMount() {
		    if (!AuthService.isLoggedIn()) {
		    	this.props.history.replace('/login')
		    } else {
		        try {
		            const profile = AuthService.getProfile()
		            this.setState({
		                user: profile
		            })
		        }
		        catch(err){
		            AuthService.logout()
		            this.props.history.replace('/login')
		        }
		    }
		}

		render() {
		    if (this.state.user) {
		        return (
		            <AuthComponent history={this.props.history} user={this.state.user} classes={this.props.classes} />
		        )
		    } else {
		        return null;
		    }
		}
	}
}
