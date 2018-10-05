import React from 'react';
import 'typeface-roboto'

import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'

import Headline from '../common/Headline'
import InformationCard from './InformationCard'

class Home extends React.Component {
	constructor() {
        super();
        this.AuthService = new AuthenticationService();
    }

	state = {};

    render() {
        return (
            <React.Fragment>
                <Headline 
                    headline="'Be Courageous' - 2018 Regional Convention, The SSE Hydro, Glasgow July 20-22"
                    subheading="Welcome to the Attendants Department Online Resource"
                />
                {this.state.reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
                <InformationCard />
            </React.Fragment>
        );
    }
}

export default Home;