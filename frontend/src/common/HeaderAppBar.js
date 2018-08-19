import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';

import AuthenticationService from '../authentication/AuthenticationService.js'

const styles = {
    root: {
        flexGrow: 1,
    },
    flex: {
        flexGrow: 1,
    },
    menuButton: {
        marginLeft: -12,
        marginRight: 20,
    },
};

class HeaderAppBar extends Component {
    constructor(props) {
        super();
        this.classes = props.classes;
        this.AuthService = new AuthenticationService();
    }

    handleLogout(){
        this.AuthService.logout()
        this.props.history.replace('/login');
    }

    render() {
        return (
            <div className={this.classes.root}>
                <AppBar position="static">
                    <Toolbar>
                        <IconButton className={this.classes.menuButton} color="inherit" aria-label="Menu">
                            <MenuIcon />
                        </IconButton>
                        <Typography variant="title" color="inherit" className={this.classes.flex}>
                            Attendants Office
                        </Typography>
                        <Button color="inherit" onClick={this.handleLogout.bind(this)}>Logout</Button>
                    </Toolbar>
                </AppBar>
            </div>
        );
    }
}

HeaderAppBar.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(HeaderAppBar);