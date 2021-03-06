import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { IntlProvider } from 'react-intl';

import {
    BrowserRouter as Router,
    Route,
    Switch
} from "react-router-dom";

import { withStyles } from '@material-ui/core/styles';

import HeaderAppBar from './common/HeaderAppBar';
import ActionNotifier from './common/ActionNotifier'
import withAuthenticationService from './authentication/withAuthenticationService';
import ErrorNotifier from './error/ErrorNotifier';


import Home  from './home/Home';
import Events  from './events/Events';
import Event  from './events/Event';
import EventTeams  from './events/teams/EventTeams';
import EventTeam  from './events/teams/EventTeam';
import Users  from './users/Users';
import User  from './users/User';

const styles = theme => ({
    root: {
        flexGrow: 1,
        zIndex: 1,
        overflow: 'hidden',
        position: 'relative',
        display: 'flex',
    },
    toolbar: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'flex-end',
        padding: '0 8px',
        ...theme.mixins.toolbar,
    },
    content: {
        flexGrow: 1,
        backgroundColor: theme.palette.background.default,
        padding: theme.spacing.unit * 3,
    },
});    

// This component is used when the user is authenticated (indicated by the AuthenticationService check)
class App extends Component {
    constructor(props) {
        super();
        this.classes = props.classes;
    }
    
    render() {
        return(
            <div className="App">
                <IntlProvider locale={navigator.language}>
                    <Router>
                        <div className={this.classes.root}>
                            <ErrorNotifier />
                            <ActionNotifier />
                            <header>
                                <HeaderAppBar history={this.props.history} />
                            </header>
                            <main className={this.classes.content}>
                                <div className={this.classes.toolbar} />
                                <Switch>
                                    <Route exact path="/" component={Home} />
                                    <Route exact path="/users" component={Users} />
                                    <Route exact path="/users/:userId" component={User} />
                                    <Route exact path="/events" component={Events} />
                                    <Route exact path="/events/:eventId" component={Event} />
                                    <Route exact path="/events/:eventId/teams" component={EventTeams} />
                                    <Route exact path="/event-teams/:eventTeamId" component={EventTeam} />
                                </Switch>
                            </main>
                        </div>
                    </Router>
                </IntlProvider>
            </div>
        );
    }
}

App.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withAuthenticationService(App));
