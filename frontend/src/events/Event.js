import React from 'react';

// material ui components
import Grid from '@material-ui/core/Grid';
import CircularProgress from '@material-ui/core/CircularProgress';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

import Avatar from '@material-ui/core/Avatar';
import TimelineIcon from '@material-ui/icons/Timeline';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../login/ReauthenticateModal'
import AuthenticationService from '../authentication/AuthenticationService'
import { displayErrorMessage } from '../error/ErrorNotifier';
import HeadlineWithAction from '../common/HeadlineWithAction'
import DateFormat from '../common/DateFormat'

import EventEdit from './EventEdit'
import EventTeamList from './teams/EventTeamList'


const styles = theme => ({
    root: {
        flexGrow: 1,
    },
    editButton: {
        marginLeft: 'auto',
    },
});    


// display information about a single event
class Event extends React.Component {
    constructor(props) {
        super();
        this.classes = props.classes;
        this.AuthService = new AuthenticationService();
    }   

    state = {
        editDialogOpen: false
    };

    componentDidMount() {
        const { match: { params } } = this.props;
        this.eventId = params.eventId;
        this.fetchEvent();
    }

    fetchEvent() {
        this.AuthService.fetch('/api/events/' + this.eventId, {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                    this.setState({
                        event: json,
                    });
                });
            } else if (response.status === 401) {
                this.setState({reauthenticate: true})
            } else {
                displayErrorMessage({ message: 'Request failed with error code: ' + response.status });
            }
        });
    }

	openEditDialog() {
        this.setState({editDialogOpen: true});
    }

    onCloseDialog() {
        this.setState({editDialogOpen: false});   
    }

    // close the dialog, re-fetch the data
    onUpdated() {
        this.setState({editDialogOpen: false});
        this.fetchEvent();
    }

	render() {
        const { classes } = this.props;
        const { event, reauthenticate, editDialogOpen } = this.state;

        if(reauthenticate) {
            return (
                <div className={classes.root}>
                    <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />
                </div>
            );
        }

        if(!event) {
            return (<div style={{position: 'relative'}}><CircularProgress size={50} left={-25} style={{marginLeft: '50%'}} /></div>);
        }

        var status
        if(event.current) {
            status = event.eventStatus + " (current event)"
        } else {
            status = event.eventStatus
        }

        return (
            <div className={classes.root}>
                {reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
                {editDialogOpen && <EventEdit 
                    event={event} 
                    onClosed={this.onCloseDialog.bind(this)} 
                    onUpdated={this.onUpdated.bind(this)} />
                }
                <HeadlineWithAction 
                    headline={event.location + ": " + event.name}
                    subheading={
                        <React.Fragment>
                            <DateFormat date={event.startDate} /> - <DateFormat date={event.endDate} />
                        </React.Fragment>
                    }
                    buttonLabel="Edit"
                    buttonOnClick={this.openEditDialog.bind(this)} />
                <Grid container spacing={24}>
                    <Grid item xs={12}>
                        <List>
                            <ListItem disableGutters>
                                <Avatar>
                                    <TimelineIcon />
                                </Avatar>
                                <ListItemText primary={status} />
                            </ListItem>
                        </List>
                    </Grid>
                </Grid>              
                <EventTeamList eventId={event.eventId} />
            </div>
        );
    }
}

export default withStyles(styles)(Event);
