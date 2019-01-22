import React from 'react';

// material ui components
import CircularProgress from '@material-ui/core/CircularProgress';

import { withStyles } from '@material-ui/core/styles';

// components
import ReauthenticateModal from '../../login/ReauthenticateModal'
import AuthenticationService from '../../authentication/AuthenticationService'
import { displayErrorMessage } from '../../error/ErrorNotifier';
import HeadlineWithAction from '../../common/HeadlineWithAction'

import EventTeamEdit from './EventTeamEdit'

const styles = theme => ({
    root: {
        flexGrow: 1,
    },
    editButton: {
        marginLeft: 'auto',
    },
});    


// display information about a single event
class EventTeam extends React.Component {
    constructor(props) {
        super();
        this.classes = props.classes;
        this.AuthService = new AuthenticationService();
    }   

    state = {
        editDialogOpen: false,
    };

    componentDidMount() {
        const { match: { params } } = this.props;
        this.fetchEventTeam(params.eventTeamId);
    }

    fetchEventTeam(eventTeamId) {
        this.AuthService.fetch('/api/event-teams/' + eventTeamId, {})
        .then(response => {
            if(response.ok) {
                return response.json().then((json) => {
                    this.setState({
                        eventTeam: json,
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

        const { match: { params } } = this.props;
        this.fetchEventTeam(params.eventTeamId);
    }

	render() {
        const { classes } = this.props;
        const { eventTeam, reauthenticate, editDialogOpen } = this.state;

        if(reauthenticate) {
            return (
                <div className={classes.root}>
                    <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />
                </div>
            );
        }

        if(!eventTeam) {
            return (<div style={{position: 'relative'}}><CircularProgress size={50} left={-25} style={{marginLeft: '50%'}} /></div>);
        }

        return (
            <div className={classes.root}>
                {reauthenticate && <ReauthenticateModal onReauthenticated={this.componentDidMount.bind(this)} />}
                {editDialogOpen && <EventTeamEdit 
                    eventTeam={eventTeam} 
                    onClosed={this.onCloseDialog.bind(this)} 
                    onUpdated={this.onUpdated.bind(this)} />
                }
                <HeadlineWithAction 
                    headline={eventTeam.name}
                    subheading={eventTeam.event.name}
                    buttonLabel="Edit"
                    buttonOnClick={this.openEditDialog.bind(this)} />                
            </div>
        );
    }
}

export default withStyles(styles)(EventTeam);
