import React from 'react';
import AuthenticationService from '../authentication/AuthenticationService.js'

class Home extends React.Component {
	constructor() {
        super();
        this.AuthService = new AuthenticationService();
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
            	this.setState({redirecting: true})
            }
            return `Request rejected with status ${response.status}`;
        })
        .then(message => {
            this.setState({message: message});
        });
    };

    render() {
    	if(this.state.redirecting) {
    		this.AuthService.logout();
			this.props.history.replace('/login');
			window.location.reload();
    	}
        return (
			<div>This is the home page {this.state.message}</div>
        );
    }
}

export default Home;