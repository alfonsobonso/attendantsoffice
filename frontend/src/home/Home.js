import React from 'react';
import 'typeface-roboto'

import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'

class Home extends React.Component {
	constructor() {
        super();
        this.AuthService = new AuthenticationService();
        this.componentDidMount = this.componentDidMount.bind(this);     // called in onReauthenticated 
    }

	state = {};

	componentDidMount() {
		this.hello();
    }

    hello = () => {
        this.AuthService.fetch('/api/hello', {})
        .then(response => {
            if(response.ok) {
                return response.text();
            } else if (response.status === 401) {
            	this.setState({reauthenticate: true})
            } else {
                alert("Request failed with error code: " + response.status);
            }
        })
        .then(message => {
            this.setState({message: message});
        });
    };

    render() {
        return (
            <React.Fragment>
                {this.state.reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount} />}
			    <div>Hi {this.AuthService.getProfile()}:  This is the home page {this.state.message}</div>
            </React.Fragment>
        );
    }
}

export default Home;