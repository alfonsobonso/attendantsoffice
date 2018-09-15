import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { withStyles } from '@material-ui/core/styles';

import {
    BrowserRouter as Router,
    Route,
    Switch
} from "react-router-dom";

import HeaderAppBar from './common/HeaderAppBar';
import withAuthenticationService from './authentication/withAuthenticationService';

import Home  from './home/Home.js';
import Events  from './events/Events.js';
import Users  from './users/Users.js';

const styles = theme => ({
    root: {
        flexGrow: 1,
        height: 440,
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
                <Router>
                    <div className={this.classes.root}>
                        <header>
                            <HeaderAppBar history={this.props.history} />
                        </header>
                        <main className={this.classes.content}>
                            <div className={this.classes.toolbar} />
                            <Switch>
                                <Route exact path="/" component={Home} />
                                <Route exact path="/users" component={Users} />
                                <Route exact path="/events" component={Events} />
                            </Switch>
                        </main>
                    </div>
                </Router>
            </div>
        );
    }
}

App.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(withAuthenticationService(App));
