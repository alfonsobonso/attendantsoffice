import React from 'react';

// material ui components
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import Avatar from '@material-ui/core/Avatar';

import EmailIcon from '@material-ui/icons/Email';
import PhoneIcon from '@material-ui/icons/Phone';



// components
import ReauthenticateModal from '../login/ReauthenticateModal.js'
import AuthenticationService from '../authentication/AuthenticationService.js'


export default class User extends React.Component {
    constructor(props) {
        super();
        this.AuthService = new AuthenticationService();
        this.componentDidMount = this.componentDidMount.bind(this);     // called in onReauthenticated 
    }   

    state = {
        value: 0,
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

    render() {
        const { user, reauthenticate } = this.state;

        if(!user) {
            return (<div style={{position: 'relative'}}><CircularProgress size={50} left={-25} style={{marginLeft: '50%'}} /></div>);
        }

        return (
            <React.Fragment>
                {reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount} />}
                <Typography variant="headline" color="inherit" noWrap gutterBottom>
                    {user.firstName} {user.lastName}
                </Typography>
                {user.congregation && <Typography variant="subheading" gutterBottom>{user.congregation.name}</Typography>}
                <List>
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
            </React.Fragment>
        );
    }
}