import React from 'react';

// material ui components
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import Avatar from '@material-ui/core/Avatar';
import EmailIcon from '@material-ui/icons/Email';
import HomeIcon from '@material-ui/icons/Home';
import PhoneIcon from '@material-ui/icons/Phone';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'
import ErrorNotifier from '../error/ErrorNotifier';
import HeadlineWithAction from '../common/HeadlineWithAction.js'

import UserEdit from './UserEdit.js'

const styles = theme => ({
    root: {
        flexGrow: 1,
    },
    editButton: {
        marginLeft: 'auto',
    },
});    

class User extends React.Component {
    constructor(props) {
        super();
        this.classes = props.classes;
        this.AuthService = new AuthenticationService();
    }   

    state = {
        edit: false,
    };

    componentDidMount() {
        const { match: { params } } = this.props;
        this.fetchUser(params.userId);
    }

    fetchUser(userId) {
        this.AuthService.fetch('/api/users/' + userId, {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                    this.setState({
                        user: json,
                    });
                });
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else {
                alert("Request failed with error code: " + response.status);
            }
        });
    }

    openEditDialog() {
        this.setState({edit: true});
    }

    onCloseDialog() {
        this.setState({edit: false});   
    }

    // close the dialog, re-fetch the data
    onUpdated() {
        this.setState({edit: false});

        const { match: { params } } = this.props;
        this.fetchUser(params.userId);
    }

    render() {
        const { classes } = this.props;
        const { user, reauthenticate, edit } = this.state;

        if(reauthenticate) {
            return (
                <div className={classes.root}>
                    <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />
                </div>
            );
        }

        if(!user) {
            return (<div style={{position: 'relative'}}><CircularProgress size={50} left={-25} style={{marginLeft: '50%'}} /></div>);
        }

        return (
            <div className={classes.root}>
                {reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
                <ErrorNotifier />
                {edit && <UserEdit 
                    user={user} 
                    onClosed={this.onCloseDialog.bind(this)} 
                    onUpdated={this.onUpdated.bind(this)} />
                }
                <HeadlineWithAction 
                    headline={user.firstName + " " + user.lastName}
                    buttonLabel="Edit"
                    buttonOnClick={this.openEditDialog.bind(this)} />
                <Grid container spacing={24}>
                    <Grid item xs={12}>
                        <List>
                            {user.congregation && 
                                <ListItem disableGutters>
                                    <Avatar>
                                        <HomeIcon />
                                    </Avatar>
                                    <ListItemText primary={user.congregation.name} />
                                </ListItem>
                            }
                            <ListItem disableGutters>
                                <Avatar>
                                    <EmailIcon />
                                </Avatar>
                                <ListItemText primary={user.email} />
                            </ListItem>
                            <li>
                                <Divider inset />
                            </li>
                            <ListItem disableGutters>
                                <Avatar>
                                    <PhoneIcon />
                                </Avatar>
                                {user.mobilePhone && <ListItemText primary={user.mobilePhone} secondary="Mobile" />}
                                {!user.mobilePhone && <ListItemText primary="-" secondary="Mobile" />}
                            </ListItem>
                            <li>
                                <Divider inset />
                            </li>
                            <ListItem disableGutters>
                                <Avatar>
                                    <PhoneIcon />
                                </Avatar>
                                {user.homePhone && <ListItemText primary={user.homePhone} secondary="Home" />}
                                {!user.homePhone && <ListItemText primary="-" secondary="Home" />}
                            </ListItem>
                        </List>
                    </Grid>
                </Grid>
            </div>
        );
    }
}

export default withStyles(styles)(User);